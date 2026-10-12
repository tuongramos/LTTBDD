# PowerShell preserves Unicode project paths on Windows.
$ErrorActionPreference = 'Stop'
$taskJava = if ($env:JAVA_HOME) { Join-Path $env:JAVA_HOME 'bin/java.exe' } else {
    $taskJdk = Get-ChildItem -LiteralPath (Join-Path $env:USERPROFILE '.jdks') -Directory -ErrorAction SilentlyContinue |
        Where-Object Name -Match '21' | Select-Object -First 1
    if ($taskJdk) { Join-Path $taskJdk.FullName 'bin/java.exe' } else { 'java' }
}
Push-Location -LiteralPath $PSScriptRoot
try {
    & $taskJava -classpath 'gradle/wrapper/gradle-wrapper.jar' org.gradle.wrapper.GradleWrapperMain @args
    $taskExitCode = $LASTEXITCODE
} finally {
    Pop-Location
}
exit $taskExitCode
