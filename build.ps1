param([string[]]$Tasks = @('assembleDebug'))
$ErrorActionPreference = 'Stop'
# Use Java 17/21. Calling the wrapper from PowerShell avoids cmd.exe Unicode path issues.
$taskJdks = @(
    "$env:USERPROFILE\.jdks\jbr-21.0.11",
    "$env:USERPROFILE\.jdks\jbr-17.0.14",
    $env:JAVA_HOME
)
$taskJdk = $taskJdks | Where-Object { $_ -and (Test-Path (Join-Path $_ 'bin\java.exe')) } | Select-Object -First 1
if (-not $taskJdk) { throw 'Cần cài JDK 17 hoặc 21 và cấu hình JAVA_HOME.' }
$env:JAVA_HOME = $taskJdk
Push-Location $PSScriptRoot
try {
    & "$taskJdk\bin\java.exe" -classpath 'gradle\wrapper\gradle-wrapper.jar' org.gradle.wrapper.GradleWrapperMain @Tasks --console=plain
    if ($LASTEXITCODE -ne 0) { throw "Gradle thất bại (exit $LASTEXITCODE)." }
} finally { Pop-Location }
