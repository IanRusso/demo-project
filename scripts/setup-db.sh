#!/bin/bash

# Database Setup Script for Gainfully Application
# This script creates the PostgreSQL database and user

set -e  # Exit on error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Database configuration
DB_NAME="gainfully_db"
DB_USER="gainfully_user"
DB_PASSWORD="gainfully_password"
DB_HOST="localhost"
DB_PORT="5432"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Gainfully Database Setup${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if PostgreSQL is installed
if ! command -v psql &> /dev/null; then
    echo -e "${RED}Error: PostgreSQL is not installed or not in PATH${NC}"
    echo ""
    echo "To install PostgreSQL:"
    echo "  macOS:   brew install postgresql@15"
    echo "  Ubuntu:  sudo apt install postgresql postgresql-contrib"
    echo ""
    exit 1
fi

echo -e "${GREEN}✓ PostgreSQL is installed${NC}"

# Check if PostgreSQL is running
if ! pg_isready -h $DB_HOST -p $DB_PORT &> /dev/null; then
    echo -e "${YELLOW}⚠ PostgreSQL is not running${NC}"
    echo ""
    echo "To start PostgreSQL:"
    echo "  macOS:   brew services start postgresql@15"
    echo "  Ubuntu:  sudo systemctl start postgresql"
    echo ""
    read -p "Would you like to try starting PostgreSQL now? (y/n) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        if [[ "$OSTYPE" == "darwin"* ]]; then
            brew services start postgresql@15 || brew services start postgresql
        else
            sudo systemctl start postgresql
        fi
        sleep 2
        if ! pg_isready -h $DB_HOST -p $DB_PORT &> /dev/null; then
            echo -e "${RED}Failed to start PostgreSQL${NC}"
            exit 1
        fi
    else
        exit 1
    fi
fi

echo -e "${GREEN}✓ PostgreSQL is running${NC}"
echo ""

# Determine the PostgreSQL superuser
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - use current user
    PG_USER=$(whoami)
else
    # Linux - use postgres user
    PG_USER="postgres"
fi

echo -e "${BLUE}Creating database and user...${NC}"
echo ""

# Create SQL commands
SQL_COMMANDS=$(cat <<EOF
-- Drop existing database and user if they exist (for clean setup)
DROP DATABASE IF EXISTS $DB_NAME;
DROP USER IF EXISTS $DB_USER;

-- Create the user
CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';

-- Create the database
CREATE DATABASE $DB_NAME OWNER $DB_USER;

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;

-- Connect to the database and grant schema privileges
\c $DB_NAME

-- Grant schema privileges (PostgreSQL 15+)
GRANT ALL ON SCHEMA public TO $DB_USER;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO $DB_USER;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO $DB_USER;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO $DB_USER;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO $DB_USER;
EOF
)

# Execute SQL commands
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - connect as current user
    echo "$SQL_COMMANDS" | psql -U $PG_USER postgres 2>&1 | grep -v "NOTICE:" || true
else
    # Linux - connect as postgres user
    echo "$SQL_COMMANDS" | sudo -u postgres psql 2>&1 | grep -v "NOTICE:" || true
fi

# Check if database was created successfully
if [[ "$OSTYPE" == "darwin"* ]]; then
    DB_EXISTS=$(psql -U $PG_USER -lqt | cut -d \| -f 1 | grep -w $DB_NAME | wc -l)
else
    DB_EXISTS=$(sudo -u postgres psql -lqt | cut -d \| -f 1 | grep -w $DB_NAME | wc -l)
fi

if [ "$DB_EXISTS" -eq 0 ]; then
    echo -e "${RED}✗ Failed to create database${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Database '$DB_NAME' created${NC}"
echo -e "${GREEN}✓ User '$DB_USER' created${NC}"
echo -e "${GREEN}✓ Privileges granted${NC}"
echo ""

# Test connection
echo -e "${BLUE}Testing database connection...${NC}"
if PGPASSWORD=$DB_PASSWORD psql -U $DB_USER -d $DB_NAME -h $DB_HOST -c "SELECT version();" > /dev/null 2>&1; then
    echo -e "${GREEN}✓ Connection successful${NC}"
else
    echo -e "${RED}✗ Connection failed${NC}"
    echo ""
    echo "You may need to configure PostgreSQL to allow password authentication."
    echo "Edit pg_hba.conf and ensure there's a line like:"
    echo "  host    all             all             127.0.0.1/32            md5"
    exit 1
fi

# Enable PostGIS extensions (requires superuser privileges)
echo -e "${BLUE}Enabling PostGIS extensions...${NC}"
if [[ "$OSTYPE" == "darwin"* ]]; then
    # macOS - connect as current user (who has superuser privileges)
    POSTGIS_ERROR=$(psql -U $PG_USER -d $DB_NAME -c "CREATE EXTENSION IF NOT EXISTS postgis; CREATE EXTENSION IF NOT EXISTS postgis_topology;" 2>&1) || true
else
    # Linux - connect as postgres superuser
    POSTGIS_ERROR=$(sudo -u postgres psql -d $DB_NAME -c "CREATE EXTENSION IF NOT EXISTS postgis; CREATE EXTENSION IF NOT EXISTS postgis_topology;" 2>&1) || true
fi
POSTGIS_EXIT_CODE=$?

# Check if there was an error (either non-zero exit code or ERROR in output)
if [ $POSTGIS_EXIT_CODE -ne 0 ] || echo "$POSTGIS_ERROR" | grep -q "ERROR:" 2>/dev/null; then
    echo -e "${RED}✗ Failed to enable PostGIS extensions${NC}"
    echo ""
    echo "Error details:"
    echo "$POSTGIS_ERROR"
    echo ""
    echo "PostGIS is not installed on your system."
    echo ""
    echo "To install PostGIS:"
    echo "  macOS:   brew install postgresql@17 (PostGIS is included)"
    echo "  Ubuntu:  sudo apt install postgresql-17-postgis-3"
    echo ""
    echo "After installing PostGIS, run this script again."
    exit 1
fi

# Verify PostGIS installation
POSTGIS_VERSION=$(PGPASSWORD=$DB_PASSWORD psql -U $DB_USER -d $DB_NAME -h $DB_HOST -t -c "SELECT PostGIS_Version();" 2>&1)
echo -e "${GREEN}✓ PostGIS extensions enabled${NC}"
echo "  Version: $(echo $POSTGIS_VERSION | xargs)"

echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}  Database Setup Complete!${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo "Database Configuration:"
echo "  Database: $DB_NAME"
echo "  User:     $DB_USER"
echo "  Password: $DB_PASSWORD"
echo "  Host:     $DB_HOST"
echo "  Port:     $DB_PORT"
echo ""
echo "Next steps:"
echo "  1. Build the application:  mvn clean package -DskipTests"
echo "  2. Run the application:    ./start.sh"
echo "  3. Flyway will automatically create the schema tables"
echo ""
echo "To connect manually:"
echo "  psql -U $DB_USER -d $DB_NAME -h $DB_HOST"
echo ""

