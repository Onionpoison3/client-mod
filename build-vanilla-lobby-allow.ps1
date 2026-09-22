$proj = Join-Path $PSScriptRoot "vanilla-lobby-allow"
$destDir = Join-Path (Split-Path $PSScriptRoot -Parent) "server\client-only-mods"
Set-Location $proj
& .\gradlew.bat build --no-daemon
if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
$jar = Get-ChildItem (Join-Path $proj "build\libs") -Filter "vanillalobbyallow-*.jar" |
    Where-Object { $_.Name -notlike "*-sources*" -and $_.Name -notlike "*-javadoc*" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1
if (-not (Test-Path (Split-Path $destDir -Parent))) {
    Write-Host "Built: $($jar.FullName)"
    Write-Host "隣に server フォルダがないので、jar は build\libs に残しています。"
    exit 0
}
New-Item -ItemType Directory -Force -Path $destDir | Out-Null
Get-ChildItem $destDir -Filter "vanillalobbyallow-*.jar" -ErrorAction SilentlyContinue | Remove-Item -Force
Copy-Item $jar.FullName -Destination (Join-Path $destDir $jar.Name) -Force
Write-Host "Installed: $(Join-Path $destDir $jar.Name)"
Write-Host "Next: run server\copy-client-mods.bat and copy client-mods into your Forge profile."
