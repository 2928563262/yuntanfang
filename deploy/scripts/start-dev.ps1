param(
  [switch]$SkipInfra
)

$ErrorActionPreference = "Stop"

$Root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$Deploy = Join-Path $Root "deploy"
$Frontend = Join-Path $Root "frontend"
$Backend = Join-Path $Root "backend"
$LogDir = Join-Path $Deploy "logs"
$PidFile = Join-Path $Deploy ".dev-pids.json"

New-Item -ItemType Directory -Force -Path $LogDir | Out-Null

function Test-PortOpen {
  param([string]$HostName, [int]$Port)
  try {
    $client = [System.Net.Sockets.TcpClient]::new()
    $task = $client.ConnectAsync($HostName, $Port)
    if (-not $task.Wait(1000)) {
      $client.Dispose()
      return $false
    }
    $client.Dispose()
    return $true
  } catch {
    return $false
  }
}

function Wait-Port {
  param([string]$Name, [string]$HostName, [int]$Port, [int]$TimeoutSeconds = 90)
  $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
  while ((Get-Date) -lt $deadline) {
    if (Test-PortOpen -HostName $HostName -Port $Port) {
      Write-Host "$Name is ready on $HostName`:$Port"
      return
    }
    Start-Sleep -Seconds 2
  }
  throw "$Name did not become ready on $HostName`:$Port"
}

function Resolve-Maven {
  $mvn = Get-Command mvn -ErrorAction SilentlyContinue
  if ($mvn) {
    return $mvn.Source
  }

  $mavenDir = Join-Path $env:TEMP "apache-maven-3.9.9"
  $mavenCmd = Join-Path $mavenDir "bin\mvn.cmd"
  if (Test-Path $mavenCmd) {
    return $mavenCmd
  }

  $zip = Join-Path $env:TEMP "apache-maven-3.9.9-bin.zip"
  if (-not (Test-Path $zip)) {
    Write-Host "Downloading Maven 3.9.9 to $zip"
    Invoke-WebRequest -Uri "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip" -OutFile $zip
  }
  Expand-Archive -LiteralPath $zip -DestinationPath $env:TEMP -Force
  return $mavenCmd
}

function Start-LoggedProcess {
  param(
    [string]$Name,
    [string]$FilePath,
    [string]$Arguments,
    [string]$WorkingDirectory,
    [hashtable]$Environment = @{}
  )

  $log = Join-Path $LogDir "$Name.log"
  $workingDirectoryLiteral = $WorkingDirectory.Replace("'", "''")
  $filePathLiteral = $FilePath.Replace("'", "''")
  $logLiteral = $log.Replace("'", "''")
  $envLines = $Environment.GetEnumerator() | ForEach-Object {
    '$env:{0} = ''{1}''' -f $_.Key, ($_.Value -replace "'", "''")
  }
  $scriptLines = @()
  $scriptLines += "Set-Location -LiteralPath '$workingDirectoryLiteral'"
  $scriptLines += $envLines
  $scriptLines += "& '$filePathLiteral' $Arguments *> '$logLiteral'"
  $script = $scriptLines -join "`r`n"
  $encodedCommand = [Convert]::ToBase64String([Text.Encoding]::Unicode.GetBytes($script))
  $process = Start-Process -FilePath "powershell.exe" `
    -ArgumentList "-NoProfile -ExecutionPolicy Bypass -EncodedCommand $encodedCommand" `
    -WorkingDirectory $WorkingDirectory `
    -WindowStyle Hidden `
    -PassThru

  Write-Host "$Name started, pid=$($process.Id), log=$log"
  return @{ name = $Name; pid = $process.Id; log = $log }
}

if (-not $SkipInfra) {
  Push-Location $Deploy
  docker compose up -d mysql redis
  Pop-Location
}

Wait-Port -Name "MySQL" -HostName "127.0.0.1" -Port 13307
Wait-Port -Name "Redis" -HostName "127.0.0.1" -Port 6379

Push-Location $Frontend
corepack pnpm install
Pop-Location

$mvn = Resolve-Maven
$processes = @()
$processes += Start-LoggedProcess -Name "backend" -FilePath $mvn -Arguments "spring-boot:run" -WorkingDirectory $Backend -Environment @{
  "APP_ENV" = "dev"
  "MYSQL_HOST" = "127.0.0.1"
  "MYSQL_PORT" = "13307"
  "MYSQL_DATABASE" = "yun_tan_fang"
  "MYSQL_USERNAME" = "yuntanfang"
  "MYSQL_PASSWORD" = "change_me"
  "REDIS_HOST" = "127.0.0.1"
  "REDIS_PORT" = "6379"
  "DEEPSEEK_BASE_URL" = $env:DEEPSEEK_BASE_URL
  "DEEPSEEK_API_KEY" = $env:DEEPSEEK_API_KEY
  "DEEPSEEK_MODEL" = $env:DEEPSEEK_MODEL
}
$processes += Start-LoggedProcess -Name "h5" -FilePath "corepack" -Arguments "pnpm --filter @yuntanfang/h5 dev" -WorkingDirectory $Frontend
$processes += Start-LoggedProcess -Name "admin" -FilePath "corepack" -Arguments "pnpm --filter @yuntanfang/admin dev" -WorkingDirectory $Frontend

$processes | ConvertTo-Json | Set-Content -LiteralPath $PidFile -Encoding UTF8

Write-Host ""
Write-Host "Yuntanfang dev environment is starting:"
Write-Host "Backend: http://localhost:8080"
Write-Host "Swagger: http://localhost:8080/swagger-ui/index.html"
Write-Host "H5:      http://localhost:5173"
Write-Host "Admin:   http://localhost:5174"
Write-Host "Logs:    $LogDir"
