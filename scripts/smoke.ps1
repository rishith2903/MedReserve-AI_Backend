$ErrorActionPreference = 'Stop'
param(
  [string]$BaseUrl = 'http://localhost:8080',
  [string]$Origin = 'http://localhost:3000',
  [string]$PathForCors = '/doctors'
)

Write-Host 'Checking health...' -ForegroundColor Cyan
$health = Invoke-WebRequest -UseBasicParsing -Uri ($BaseUrl + '/actuator/health') -TimeoutSec 5
if ($health.Content -notmatch 'UP') {
  Write-Error 'Health not UP'
  exit 1
} else {
  Write-Host 'Health OK' -ForegroundColor Green
}

Write-Host 'Checking CORS preflight...' -ForegroundColor Cyan
$headers = @{ 'Origin' = $Origin; 'Access-Control-Request-Method' = 'GET' }
try {
  $pre = Invoke-WebRequest -UseBasicParsing -Method Options -Headers $headers -Uri ($BaseUrl + $PathForCors) -TimeoutSec 5
  Write-Host ("Preflight status: {0}" -f $pre.StatusCode) -ForegroundColor Green
} catch {
  Write-Warning 'Preflight request failed, inspect server CORS config or choose a different path'
}

Write-Host 'Smoke tests completed' -ForegroundColor Green
