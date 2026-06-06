$ErrorActionPreference = "Continue"

$Root = Resolve-Path (Join-Path $PSScriptRoot "..\..")
$Deploy = Join-Path $Root "deploy"
$PidFile = Join-Path $Deploy ".dev-pids.json"

if (Test-Path $PidFile) {
  $processes = Get-Content -LiteralPath $PidFile -Raw | ConvertFrom-Json
  foreach ($process in $processes) {
    $pidValue = [int]$process.pid
    $running = Get-Process -Id $pidValue -ErrorAction SilentlyContinue
    if ($running) {
      taskkill /PID $pidValue /T /F | Out-Null
      Write-Host "Stopped $($process.name), pid=$pidValue"
    }
  }
  Remove-Item -LiteralPath $PidFile -Force
}

Push-Location $Deploy
docker compose stop backend mysql redis
Pop-Location
