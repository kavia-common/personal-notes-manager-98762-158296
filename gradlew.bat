@echo off
REM Proxy Gradle wrapper at repo root to delegate to the android_frontend/gradlew

setlocal
set ROOT=%~dp0
pushd "%ROOT%android_frontend"
call gradlew %*
set EXITCODE=%ERRORLEVEL%
popd
exit /b %EXITCODE%
