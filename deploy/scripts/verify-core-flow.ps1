param(
  [string]$BaseUrl = "http://localhost:8080/api"
)

$ErrorActionPreference = "Stop"
Add-Type -AssemblyName System.Net.Http

function Invoke-Api {
  param(
    [string]$Method = "Get",
    [string]$Path,
    [object]$Body = $null,
    [string]$Token = ""
  )

  $client = [System.Net.Http.HttpClient]::new()
  $request = [System.Net.Http.HttpRequestMessage]::new(
    [System.Net.Http.HttpMethod]::new($Method.ToUpperInvariant()),
    "$BaseUrl$Path"
  )
  if ($Token) {
    $request.Headers.TryAddWithoutValidation("Authorization", "Bearer $Token") | Out-Null
  }
  if ($null -ne $Body) {
    $json = $Body | ConvertTo-Json -Depth 8
    $request.Content = [System.Net.Http.StringContent]::new($json, [System.Text.Encoding]::UTF8, "application/json")
  }

  $response = $client.SendAsync($request).GetAwaiter().GetResult()
  $content = $response.Content.ReadAsStringAsync().GetAwaiter().GetResult()
  if (-not $response.IsSuccessStatusCode) {
    throw "HTTP $([int]$response.StatusCode) $($response.ReasonPhrase): $content"
  }
  if ([string]::IsNullOrWhiteSpace($content)) {
    return $null
  }
  try {
    $content | ConvertFrom-Json
  } finally {
    $response.Dispose()
    $request.Dispose()
    $client.Dispose()
  }
}

function Login {
  param([string]$Username)
  (Invoke-Api -Method Post -Path "/auth/login" -Body @{ username = $Username; password = "123456" }).data.token
}

function Assert-Ok {
  param([bool]$Condition, [string]$Message)
  if (-not $Condition) {
    throw $Message
  }
  Write-Host "OK $Message"
}

function Assert-Fails {
  param(
    [string]$Method = "Get",
    [string]$Path,
    [object]$Body = $null,
    [string]$Token = "",
    [string]$Message
  )
  try {
    Invoke-Api -Method $Method -Path $Path -Body $Body -Token $Token | Out-Null
    throw "unexpected success"
  } catch {
    if ($_.Exception.Message -eq "unexpected success") {
      throw $Message
    }
    Write-Host "OK $Message"
  }
}

$admin = Login "test3"
$user = Login "test1"
$vendor = Login "test4"

$oldTownVendorName = -join ([char[]](0x8001, 0x57CE, 0x8C46, 0x82B1, 0x644A))
$oldTownKeyword = [System.Uri]::EscapeDataString($oldTownVendorName)
$pickupTime = (-join ([char[]](0x4ECA, 0x5929))) + " 20:00"

$before = Invoke-Api -Path "/stalls/search?keyword=$oldTownKeyword&pageNo=1&pageSize=10" -Token $user
Write-Host "Before approval old town count: $($before.data.total)"

$vendors = (Invoke-Api -Path "/admin/vendors/applications" -Token $admin).data.records
$oldVendor = $vendors | Where-Object { $_.vendorName -eq $oldTownVendorName } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $oldVendor) -Message "found old town vendor application"
if ($oldVendor.status -ne "approved") {
  Invoke-Api -Method Put -Path "/admin/vendors/applications/$($oldVendor.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
}

$quals = (Invoke-Api -Path "/admin/qualifications" -Token $admin).data.records | Where-Object { $_.vendorName -eq $oldTownVendorName }
foreach ($qual in $quals) {
  if ($qual.status -ne "approved") {
    Invoke-Api -Method Put -Path "/admin/qualifications/$($qual.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
  }
}

$identities = (Invoke-Api -Path "/admin/special-identities" -Token $admin).data.records | Where-Object { $_.vendorName -eq $oldTownVendorName }
foreach ($identity in $identities) {
  if ($identity.status -ne "approved") {
    Invoke-Api -Method Put -Path "/admin/special-identities/$($identity.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
  }
}

$reservations = (Invoke-Api -Path "/admin/stall-reservations" -Token $admin).data.records | Where-Object { $_.vendorName -eq $oldTownVendorName }
$reservation = $reservations | Select-Object -First 1
Assert-Ok -Condition ($null -ne $reservation) -Message "found old town stall reservation"
if ($reservation.status -ne "approved") {
  Invoke-Api -Method Put -Path "/admin/stall-reservations/$($reservation.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
}

$after = Invoke-Api -Path "/stalls/search?keyword=$oldTownKeyword&pageNo=1&pageSize=10" -Token $user
Assert-Ok -Condition ($after.data.total -ge 1) -Message "old town stall is visible after approval"
$stall = $after.data.records[0]

$products = Invoke-Api -Path "/stalls/$($stall.id)/products" -Token $user
Assert-Ok -Condition ($products.data.total -ge 1) -Message "old town stall has products"
$product = $products.data.records[0]

$agentSearch = Invoke-Api -Method Post -Path "/agent/chat" -Token $user -Body @{
  message = "找地方特色摊位"
  history = @()
  context = @{ currentRoute = "/agent-order"; role = "consumer" }
}
Assert-Ok -Condition ($agentSearch.data.intent -eq "search_stalls") -Message "agent search intent recognized"
Assert-Ok -Condition (@($agentSearch.data.cards).Count -ge 1) -Message "agent search returns real stall cards"
Assert-Ok -Condition (-not [string]::IsNullOrWhiteSpace($agentSearch.data.cards[0].name)) -Message "agent stall card has stall name"

$agentComplaint = Invoke-Api -Method Post -Path "/agent/chat" -Token $user -Body @{
  message = "投诉$($stall.stallName)卫生问题"
  history = @()
  context = @{ currentRoute = "/agent-order"; role = "consumer" }
}
Assert-Ok -Condition ($agentComplaint.data.intent -eq "submit_complaint") -Message "agent complaint intent recognized"
$agentComplaintPayload = $agentComplaint.data.action.payload
Assert-Ok -Condition (-not [string]::IsNullOrWhiteSpace($agentComplaintPayload.description) -or -not [string]::IsNullOrWhiteSpace($agentComplaintPayload.type)) -Message "agent complaint payload has issue detail"
$agentComplaintCreated = Invoke-Api -Method Post -Path "/complaints" -Token $user -Body @{
  vendorId = $agentComplaintPayload.vendorId
  type = $agentComplaintPayload.type
  description = $agentComplaintPayload.description
}
Assert-Ok -Condition ($agentComplaintCreated.data.id -gt 0) -Message "agent complaint payload can be persisted"

try {
  Invoke-Api -Method Put -Path "/vendor/products/$($product.id)" -Token $vendor -Body @{ status = "off_sale" } | Out-Null
  Assert-Fails -Method Post -Path "/orders" -Token $user -Body @{
    stallId = $stall.id
    pickupTime = $pickupTime
    contactPhone = "13900000001"
    items = @(@{ productId = $product.id; quantity = 1 })
  } -Message "consumer cannot order off-sale product"
} finally {
  Invoke-Api -Method Put -Path "/vendor/products/$($product.id)" -Token $vendor -Body @{ status = "on_sale" } | Out-Null
}

$nearby = Invoke-Api -Path "/stalls/nearby?pageNo=1&pageSize=10" -Token $user
$otherStall = $nearby.data.records | Where-Object { $_.id -ne $stall.id } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $otherStall) -Message "found another visible stall"
$otherProducts = Invoke-Api -Path "/stalls/$($otherStall.id)/products" -Token $user
Assert-Ok -Condition ($otherProducts.data.total -ge 1) -Message "other visible stall has products"
$oldProductNames = ($products.data.records | ForEach-Object { $_.productName }) -join "|"
$otherProductNames = ($otherProducts.data.records | ForEach-Object { $_.productName }) -join "|"
Assert-Ok -Condition ($oldProductNames -ne $otherProductNames) -Message "different stalls expose different product lists"

$order = Invoke-Api -Method Post -Path "/orders" -Token $user -Body @{
  stallId = $stall.id
  pickupTime = $pickupTime
  contactPhone = "13900000001"
  items = @(@{ productId = $product.id; quantity = 1 })
}
$createdOrder = $order.data.order
$createdOrderItems = @($order.data.items)
Assert-Ok -Condition ($createdOrder.id -gt 0) -Message "consumer created real order"
Assert-Ok -Condition ($createdOrderItems.Count -ge 1) -Message "created order returns item details"
Assert-Ok -Condition (-not [string]::IsNullOrWhiteSpace($createdOrderItems[0].productName)) -Message "created order item has product name"
Assert-Ok -Condition ([long]$createdOrderItems[0].productId -eq [long]$product.id) -Message "created order item matches selected product"
Assert-Ok -Condition ($createdOrderItems[0].quantity -eq 1) -Message "created order item has quantity"
Assert-Ok -Condition ([decimal]$createdOrderItems[0].price -eq [decimal]$product.price) -Message "created order item has unit price"

$userOrderDetail = Invoke-Api -Path "/orders/$($createdOrder.id)" -Token $user
$userOrderDetailItems = @($userOrderDetail.data.items)
Assert-Ok -Condition ($userOrderDetailItems.Count -ge 1) -Message "consumer order detail returns item details"
Assert-Ok -Condition (-not [string]::IsNullOrWhiteSpace($userOrderDetailItems[0].productName)) -Message "consumer order detail has product name"
Assert-Ok -Condition ([long]$userOrderDetailItems[0].productId -eq [long]$product.id) -Message "consumer order detail matches selected product"

$vendorOrders = Invoke-Api -Path "/vendor/orders" -Token $vendor
$seen = $vendorOrders.data.records | Where-Object { $_.id -eq $createdOrder.id } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $seen) -Message "vendor can see consumer order"

$vendorOrderDetail = Invoke-Api -Path "/vendor/orders/$($createdOrder.id)" -Token $vendor
$vendorOrderDetailItems = @($vendorOrderDetail.data.items)
Assert-Ok -Condition ($vendorOrderDetailItems.Count -ge 1) -Message "vendor order detail returns item details"
Assert-Ok -Condition (-not [string]::IsNullOrWhiteSpace($vendorOrderDetailItems[0].productName)) -Message "vendor order detail has product name"
Assert-Ok -Condition ([long]$vendorOrderDetailItems[0].productId -eq [long]$product.id) -Message "vendor order detail matches selected product"
Assert-Ok -Condition ([decimal]$vendorOrderDetailItems[0].price -eq [decimal]$product.price) -Message "vendor order detail has unit price"

try {
  Invoke-Api -Method Put -Path "/vendor/orders/$($createdOrder.id)/status" -Token $vendor -Body @{ status = "completed" } | Out-Null
  throw "illegal transition unexpectedly passed"
} catch {
  if ($_.Exception.Message -eq "illegal transition unexpectedly passed") {
    throw
  }
  Write-Host "OK illegal transition rejected"
}

Invoke-Api -Method Put -Path "/vendor/orders/$($createdOrder.id)/status" -Token $vendor -Body @{ status = "accepted" } | Out-Null
Invoke-Api -Method Put -Path "/vendor/orders/$($createdOrder.id)/status" -Token $vendor -Body @{ status = "preparing" } | Out-Null
Invoke-Api -Method Put -Path "/vendor/orders/$($createdOrder.id)/status" -Token $vendor -Body @{ status = "completed" } | Out-Null
Write-Host "OK order accepted -> preparing -> completed"

$review = Invoke-Api -Method Post -Path "/orders/$($createdOrder.id)/reviews" -Token $user -Body @{
  rating = 5
  content = "verify review content"
}
Assert-Ok -Condition ($review.data.id -gt 0) -Message "consumer reviewed completed order"
$vendorReviews = Invoke-Api -Path "/vendor/reviews" -Token $vendor
$seenReview = $vendorReviews.data.records | Where-Object { $_.id -eq $review.data.id } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $seenReview) -Message "vendor can see consumer review"
$reply = Invoke-Api -Method Post -Path "/vendor/reviews/$($review.data.id)/reply" -Token $vendor -Body @{ reply = "verify reply content" }
Assert-Ok -Condition ($reply.data.status -eq "replied") -Message "vendor replied review"
$stallReviews = Invoke-Api -Path "/stalls/$($stall.id)/reviews" -Token $user
$repliedReview = $stallReviews.data.records | Where-Object { $_.id -eq $review.data.id -and $_.reply } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $repliedReview) -Message "stall detail reviews include vendor reply"

$complaint = Invoke-Api -Method Post -Path "/complaints" -Token $user -Body @{
  vendorId = $createdOrder.vendorId
  orderId = $createdOrder.id
  type = "service"
  description = "verify complaint flow"
}
Assert-Ok -Condition ($complaint.data.id -gt 0) -Message "consumer created complaint"
$myComplaints = Invoke-Api -Path "/complaints/my" -Token $user
$seenComplaint = $myComplaints.data.records | Where-Object { $_.id -eq $complaint.data.id } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $seenComplaint) -Message "consumer can see submitted complaint"
Invoke-Api -Method Put -Path "/admin/complaints/$($complaint.data.id)/assign" -Token $admin -Body @{} | Out-Null
$processedComplaint = Invoke-Api -Method Put -Path "/admin/complaints/$($complaint.data.id)/process" -Token $admin -Body @{ status = "processed"; result = "verify process result" }
Assert-Ok -Condition ($processedComplaint.data.status -eq "processed") -Message "admin can process complaint"
$myComplaintsAfterProcess = Invoke-Api -Path "/complaints/my" -Token $user
$processedSeen = $myComplaintsAfterProcess.data.records | Where-Object { $_.id -eq $complaint.data.id -and $_.processResult } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $processedSeen) -Message "consumer complaint list shows process result"

$userMessages = Invoke-Api -Path "/messages/my" -Token $user
Assert-Ok -Condition ($userMessages.data.total -ge 1) -Message "consumer can see generated messages"
$unreadMessage = $userMessages.data.records | Where-Object { $_.status -eq "unread" } | Select-Object -First 1
Assert-Ok -Condition ($null -ne $unreadMessage) -Message "consumer has unread message"
$readMessage = Invoke-Api -Method Put -Path "/messages/$($unreadMessage.id)/read" -Token $user -Body @{}
Assert-Ok -Condition ($readMessage.data.status -eq "read") -Message "consumer can mark message read"
$vendorMessages = Invoke-Api -Path "/messages/my" -Token $vendor
Assert-Ok -Condition ($vendorMessages.data.total -ge 1) -Message "vendor can see generated messages"

Assert-Fails -Path "/vendor/orders" -Token $user -Message "consumer cannot call vendor orders"
Assert-Fails -Path "/orders/my" -Token $vendor -Message "vendor cannot call consumer orders"
Assert-Fails -Path "/admin/complaints" -Token $user -Message "consumer cannot call admin complaints"

Write-Host "Core flow verification passed."
