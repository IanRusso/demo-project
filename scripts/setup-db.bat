@echo off
REM Database Setup Script for Gainfully Application (Windows)
REM This script creates the PostgreSQL database and user

setlocal enabledelayedexpansion

REM Database configuration
set DB_NAME=gainfully_db
set DB_USER=gainfully_user
set DB_PASSWORD=gainfully_password
set DB_HOST=localhost
set DB_PORT=5432

echo ========================================
echo   Gainfully Database Setup
echo ========================================
echo.

REM Check if PostgreSQL is installed
where psql >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] PostgreSQL is not installed or not in PATH
    echo.
    echo To install PostgreSQL:
    echo   Download from: https://www.postgresql.org/download/windows/
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL is installed
echo.

REM Check if PostgreSQL is running
pg_isready -h %DB_HOST% -p %DB_PORT% >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] PostgreSQL is not running
    echo.
    echo Please start PostgreSQL service:
    echo   1. Open Services (services.msc)
    echo   2. Find "postgresql-x64-XX" service
    echo   3. Start the service
    echo.
    pause
    exit /b 1
)

echo [OK] PostgreSQL is running
echo.

echo Creating database and user...
echo.

REM Create temporary SQL file
set TEMP_SQL=%TEMP%\gainfully_setup.sql

(
echo -- Drop existing database and user if they exist (for clean setup^)
echo DROP DATABASE IF EXISTS %DB_NAME%;
echo DROP USER IF EXISTS %DB_USER%;
echo.
echo -- Create the user
echo CREATE USER %DB_USER% WITH PASSWORD '%DB_PASSWORD%';
echo.
echo -- Create the database
echo CREATE DATABASE %DB_NAME% OWNER %DB_USER%;
echo.
echo -- Grant privileges
echo GRANT ALL PRIVILEGES ON DATABASE %DB_NAME% TO %DB_USER%;
) > "%TEMP_SQL%"

REM Execute SQL commands as postgres superuser
REM Note: You may need to enter the postgres user password
echo Connecting to PostgreSQL (you may be prompted for the postgres password^)...
psql -U postgres -f "%TEMP_SQL%" 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to create database. Make sure you have the postgres user password.
    del "%TEMP_SQL%" 2>nul
    pause
    exit /b 1
)

REM Create schema privileges SQL
set TEMP_SQL2=%TEMP%\gainfully_setup2.sql

(
echo -- Grant schema privileges (PostgreSQL 15+^)
echo GRANT ALL ON SCHEMA public TO %DB_USER%;
echo GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO %DB_USER%;
echo GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO %DB_USER%;
echo.
echo -- Set default privileges for future tables
echo ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO %DB_USER%;
echo ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO %DB_USER%;
) > "%TEMP_SQL2%"

REM Execute schema privileges
psql -U postgres -d %DB_NAME% -f "%TEMP_SQL2%" 2>nul

REM Clean up temp files
del "%TEMP_SQL%" 2>nul
del "%TEMP_SQL2%" 2>nul

echo [OK] Database '%DB_NAME%' created
echo [OK] User '%DB_USER%' created
echo [OK] Privileges granted
echo.

REM Test connection
echo Testing database connection...
set PGPASSWORD=%DB_PASSWORD%
psql -U %DB_USER% -d %DB_NAME% -h %DB_HOST% -c "SELECT version();" >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo [OK] Connection successful
) else (
    echo [ERROR] Connection failed
    echo.
    echo You may need to configure PostgreSQL to allow password authentication.
    echo Edit pg_hba.conf and ensure there's a line like:
    echo   host    all             all             127.0.0.1/32            md5
    pause
    exit /b 1
)

echo.
echo ========================================
echo   Database Setup Complete!
echo ========================================
echo.
echo Database Configuration:
echo   Database: %DB_NAME%
echo   User:     %DB_USER%
echo   Password: %DB_PASSWORD%
echo   Host:     %DB_HOST%
echo   Port:     %DB_PORT%
echo.
echo Next steps:
echo   1. Build the application:  mvn clean package -DskipTests
echo   2. Run the application:    start.bat
echo   3. Flyway will automatically create the schema tables
echo.
echo To connect manually:
echo   psql -U %DB_USER% -d %DB_NAME% -h %DB_HOST%
echo.
pause

