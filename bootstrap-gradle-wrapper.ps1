$ErrorActionPreference = "Stop"

$gradleVersion = "9.7.1"

$bootstrapRoot = Join-Path `
    $env:TEMP `
    "ExtendedItems-GradleBootstrap"

$zipPath = Join-Path `
    $bootstrapRoot `
    "gradle-$gradleVersion-bin.zip"

$extractRoot = Join-Path `
    $bootstrapRoot `
    "gradle-$gradleVersion"

$gradleHome = Join-Path `
    $extractRoot `
    "gradle-$gradleVersion"

$gradleBat = Join-Path `
    $gradleHome `
    "bin\gradle.bat"

Write-Host "Preparing Gradle $gradleVersion bootstrap..."

if (Test-Path $bootstrapRoot)
{
    try
    {
        Remove-Item `
            -Recurse `
            -Force `
            $bootstrapRoot `
            -ErrorAction Stop
    }
    catch
    {
        Write-Warning `
            "Could not completely remove the previous bootstrap directory. Continuing with a fresh directory name."

        $bootstrapRoot = Join-Path `
            $env:TEMP `
            "ExtendedItems-GradleBootstrap-$([Guid]::NewGuid().ToString('N'))"

        $zipPath = Join-Path `
            $bootstrapRoot `
            "gradle-$gradleVersion-bin.zip"

        $extractRoot = Join-Path `
            $bootstrapRoot `
            "gradle-$gradleVersion"

        $gradleHome = Join-Path `
            $extractRoot `
            "gradle-$gradleVersion"

        $gradleBat = Join-Path `
            $gradleHome `
            "bin\gradle.bat"
    }
}

New-Item `
    -ItemType Directory `
    -Force `
    -Path $bootstrapRoot `
    | Out-Null

$gradleUrl = `
    "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"

Write-Host "Downloading Gradle $gradleVersion..."

Invoke-WebRequest `
    -Uri $gradleUrl `
    -OutFile $zipPath

Write-Host "Extracting Gradle..."

New-Item `
    -ItemType Directory `
    -Force `
    -Path $extractRoot `
    | Out-Null

Expand-Archive `
    -Path $zipPath `
    -DestinationPath $extractRoot `
    -Force

if (-not (Test-Path $gradleBat))
{
    throw "Gradle executable was not found at '$gradleBat'."
}

Write-Host "Generating Gradle wrapper..."

& $gradleBat `
    --no-daemon `
    wrapper `
    "--gradle-version=$gradleVersion" `
    "--distribution-type=bin"

if ($LASTEXITCODE -ne 0)
{
    throw "Gradle wrapper generation failed with exit code $LASTEXITCODE."
}

$requiredFiles = @(
    "gradlew",
    "gradlew.bat",
    "gradle\wrapper\gradle-wrapper.jar",
    "gradle\wrapper\gradle-wrapper.properties"
)

foreach ($requiredFile in $requiredFiles)
{
    if (-not (Test-Path $requiredFile))
    {
        throw "Gradle wrapper generation completed, but '$requiredFile' was not created."
    }
}

Write-Host ""
Write-Host "Gradle wrapper generated successfully."
Write-Host ""

Write-Host "Cleaning up temporary Gradle files..."

$cleanupSucceeded = $false

for ($attempt = 1; $attempt -le 5; $attempt++)
{
    try
    {
        Remove-Item `
            -Recurse `
            -Force `
            $bootstrapRoot `
            -ErrorAction Stop

        $cleanupSucceeded = $true
        break
    }
    catch
    {
        if ($attempt -lt 5)
        {
            Start-Sleep -Seconds 2
        }
    }
}

if (-not $cleanupSucceeded)
{
    Write-Warning `
        "The Gradle wrapper was generated successfully, but Windows is still using files in '$bootstrapRoot'. The temporary directory can be deleted later."
}

Write-Host ""
Write-Host "Run:"
Write-Host "  .\gradlew.bat --version"
Write-Host "  .\gradlew.bat build"