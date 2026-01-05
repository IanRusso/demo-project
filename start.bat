@echo off
REM Demo Project Startup Script for Windows
REM This script builds and starts both the backend and frontend servers

echo ==========================================
echo   Demo Project Startup
echo ==========================================
echo.

REM Step 1: Build Backend
echo [1/3] Building backend...
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo Backend build failed!
    exit /b 1
)
echo [OK] Backend built successfully
echo.

REM Step 2: Build Frontend
echo [2/3] Building frontend...
cd ui
call npm run build
if %ERRORLEVEL% NEQ 0 (
    echo Frontend build failed!
    exit /b 1
)
cd ..
echo [OK] Frontend built successfully
echo.

REM Step 3: Start servers
echo [3/3] Starting servers...
echo.

REM Start backend server in new window
echo Starting backend server...
start "Demo Backend Server" java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml
echo Backend server started in new window

REM Wait a moment for backend to start
timeout /t 3 /nobreak > nul

REM Start frontend server in new window
echo Starting frontend server...
cd ui
start "Demo Frontend Server" npm start
cd ..
echo Frontend server started in new window

echo.
echo ==========================================
echo [OK] All servers started successfully!
echo ==========================================
echo.
echo Backend Server:
echo   - Application: http://localhost:8080
echo   - Admin/Health: http://localhost:8081
echo.
echo Frontend Server:
echo   - UI: http://localhost:3000
echo.
echo Close the server windows to stop the servers
echo ==========================================
echo.

pause

