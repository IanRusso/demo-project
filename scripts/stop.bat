@echo off
REM Demo Project Stop Script for Windows
REM This script stops all running backend and frontend servers

echo ==========================================
echo   Stopping Demo Project Servers
echo ==========================================
echo.

REM Stop backend server (port 8080)
echo Stopping Backend Application (port 8080)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8080" ^| find "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo [OK] Backend Application stopped
    )
)

REM Stop backend admin server (port 8081)
echo Stopping Backend Admin (port 8081)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":8081" ^| find "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo [OK] Backend Admin stopped
    )
)

REM Stop frontend server (port 3000)
echo Stopping Frontend (port 3000)...
for /f "tokens=5" %%a in ('netstat -aon ^| find ":3000" ^| find "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo [OK] Frontend stopped
    )
)

REM Kill any Java processes running demo-project
echo Stopping any remaining backend processes...
taskkill /F /FI "WINDOWTITLE eq Demo Backend Server*" >nul 2>&1

REM Kill any npm processes
echo Stopping any remaining frontend processes...
taskkill /F /FI "WINDOWTITLE eq Demo Frontend Server*" >nul 2>&1

echo.
echo ==========================================
echo [OK] All servers stopped
echo ==========================================
echo.

pause

