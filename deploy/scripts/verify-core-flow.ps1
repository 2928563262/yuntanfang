param(
  [string]$BaseUrl = "http://localhost:8080/api"
)

$ErrorActionPreference = "Stop"

function Invoke-Api {
  param(
    [string]$Method = "Get",
    [string]$Path,
    [object]$Body = $null,
    [string]$Token = ""
  )

  $headers = @{}
  if ($Token) {
    $headers.Authorization = "Bearer $Token"
  }
  $args = @{
    Uri = "$BaseUrl$Path"
    Method = $Method
    Headers = $headers
  }
  if ($null -ne $Body) {
    $args.ContentType = "application/json"
    $args.Body = ($Body | ConvertTo-Json -Depth 8)
  }
  Invoke-RestMethod @args
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

$admin = Login "test3"
$user = Login "test1"
$vendor = Login "test4"

$before = Invoke-Api -Path "/stalls/search?keyword=%E8%80%81%E5%9F%8E%E8%B1%86%E8%8A%B1%E6%91%8A&pageNo=1&pageSize=10" -Token $user
Write-Host "Before approval old town count: $($before.data.total)"

$vendors = (Invoke-Api -Path "/admin/vendors/applications" -Token $admin).data.records
$oldVendor = $vendors | Where-Object { $_.vendorName -eq "老城豆花摊" } | Select-Object -First 1
Assert-Ok ($null -ne $oldVendor) "found old town vendor application"
if ($oldVendor.status -ne "approved") {
  Invoke-Api -Method Put -Path "/admin/vendors/applications/$($oldVendor.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
}

$quals = (Invoke-Api -Path "/admin/qualifications" -Token $admin).data.records | Where-Object { $_.vendorName -eq "老城豆花摊" }
foreach ($qual in $quals) {
  if ($qual.status -ne "approved") {
    Invoke-Api -Method Put -Path "/admin/qualifications/$($qual.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
  }
}

$identities = (Invoke-Api -Path "/admin/special-identities" -Token $admin).data.records | Where-Object { $_.vendorName -eq "老城豆花摊" }
foreach ($identity in $identities) {
  if ($identity.status -ne "approved") {
    Invoke-Api -Method Put -Path "/admin/special-identities/$($identity.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
  }
}

$reservations = (Invoke-Api -Path "/admin/stall-reservations" -Token $admin).data.records | Where-Object { $_.vendorName -eq "老城豆花摊" }
$reservation = $reservations | Select-Object -First 1
Assert-Ok ($null -ne $reservation) "found old town stall reservation"
if ($reservation.status -ne "approved") {
  Invoke-Api -Method Put -Path "/admin/stall-reservations/$($reservation.id)/audit" -Token $admin -Body @{ status = "approved" } | Out-Null
}

$after = Invoke-Api -Path "/stalls/search?keyword=%E8%80%81%E5%9F%8E%E8%B1%86%E8%8A%B1%E6%91%8A&pageNo=1&pageSize=10" -Token $user
Assert-Ok ($after.data.total -ge 1) "old town stall is visible after approval"
$stall = $after.data.records[0]

$products = Invoke-Api -Path "/stalls/$($stall.id)/products" -Token $user
Assert-Ok ($products.data.total -ge 1) "old town stall has products"
$product = $products.data.records[0]

$order = Invoke-Api -Method Post -Path "/orders" -Token $user -Body @{
  stallId = $stall.id
  pickupTime = "今天 20:00"
  contactPhone = "13900000001"
  items = @(@{ productId = $product.id; quantity = 1 })
}
Assert-Ok ($order.data.id -gt 0) "consumer created real order"

$vendorOrders = Invoke-Api -Path "/vendor/orders" -Token $vendor
$seen = $vendorOrders.data.records | Where-Object { $_.id -eq $order.data.id } | Select-Object -First 1
Assert-Ok ($null -ne $seen) "vendor can see consumer order"

try {
  Invoke-Api -Method Put -Path "/vendor/orders/$($order.data.id)/status" -Token $vendor -Body @{ status = "completed" } | Out-Null
  throw "illegal transition unexpectedly passed"
} catch {
  if ($_.Exception.Message -eq "illegal transition unexpectedly passed") {
    throw
  }
  Write-Host "OK illegal transition rejected"
}

Invoke-Api -Method Put -Path "/vendor/orders/$($order.data.id)/status" -Token $vendor -Body @{ status = "accepted" } | Out-Null
Invoke-Api -Method Put -Path "/vendor/orders/$($order.data.id)/status" -Token $vendor -Body @{ status = "preparing" } | Out-Null
Invoke-Api -Method Put -Path "/vendor/orders/$($order.data.id)/status" -Token $vendor -Body @{ status = "completed" } | Out-Null
Write-Host "OK order accepted -> preparing -> completed"

Write-Host "Core flow verification passed."
