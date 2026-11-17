# Ronda 1 - "El atajo": copia sobre el proyecto la implementacion de la alerta
# de stock bajo tal como la dejaria un agente apurado.
#
# Uso (desde la carpeta del taller, en PowerShell):
#   powershell -ExecutionPolicy Bypass -File scripts\aplicar-atajo.ps1
#   powershell -ExecutionPolicy Bypass -File scripts\aplicar-atajo.ps1 -Destino ruta\a\mi-proyecto
param(
    [string]$Destino = (Join-Path (Split-Path -Parent $PSScriptRoot) "inventarium-pedidos")
)
$ErrorActionPreference = "Stop"

$raiz = Split-Path -Parent $PSScriptRoot
$origen = Join-Path $raiz "atajo\src"

if (-not (Test-Path (Join-Path $Destino "pom.xml"))) {
    Write-Error "No encuentro $Destino\pom.xml. Indique la carpeta del proyecto con -Destino."
}

$destinoSrc = Join-Path (Resolve-Path $Destino) "src"
Write-Host "Atajo aplicado en $Destino. Archivos tocados:"
Get-ChildItem -Path $origen -Recurse -File | ForEach-Object {
    $relativa = $_.FullName.Substring($origen.Length).TrimStart('\', '/')
    $final = Join-Path $destinoSrc $relativa
    New-Item -ItemType Directory -Force -Path (Split-Path -Parent $final) | Out-Null
    Copy-Item -Force -Path $_.FullName -Destination $final
    Write-Host "  src\$relativa"
}
Write-Host ""
Write-Host "Siguiente paso: cd `"$Destino`"; mvn -B clean verify"
