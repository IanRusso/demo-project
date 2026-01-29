#!/bin/bash
# Database Migration Script for Mac/Linux
# This script runs Flyway migrations to update the database schema

# Color codes for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo "=========================================="
echo "  Database Migration Script"
echo "=========================================="
echo ""

# Database connection settings
DB_URL="jdbc:postgresql://localhost:5432/gainfully_db"
DB_USER="gainfully_user"
DB_PASSWORD="gainfully_password"

echo -e "${BLUE}[1/2] Checking database connection...${NC}"

# Test database connection using psql
if command -v psql &> /dev/null; then
    # Test connection
    if PGPASSWORD="$DB_PASSWORD" psql -h localhost -U "$DB_USER" -d gainfully_db -c "SELECT 1;" > /dev/null 2>&1; then
        echo -e "${GREEN}✓ Database connection successful${NC}"
    else
        echo -e "${YELLOW}⚠ Warning: Could not verify database connection${NC}"
        echo "  Make sure PostgreSQL is running and the database exists"
        echo ""
        read -p "Continue anyway? (y/n) " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
else
    echo -e "${YELLOW}⚠ psql not found, skipping connection test${NC}"
fi

echo ""
echo -e "${BLUE}[2/2] Running database migrations...${NC}"
echo ""

# Run migrations using Maven Flyway plugin
# baselineVersion=3 means Flyway will assume V1, V2, V3 are already applied
# and will only run V4 and later migrations
mvn flyway:migrate \
    -Dflyway.url="$DB_URL" \
    -Dflyway.user="$DB_USER" \
    -Dflyway.password="$DB_PASSWORD" \
    -Dflyway.locations=filesystem:src/main/resources/db/migration \
    -Dflyway.baselineOnMigrate=true \
    -Dflyway.baselineVersion=3

MIGRATION_EXIT_CODE=$?

echo ""
if [ $MIGRATION_EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}=========================================="
    echo -e "  ✓ Migrations completed successfully!"
    echo -e "==========================================${NC}"
    echo ""
    echo "Your database schema is now up to date."
    echo ""

    # Show current migration status
    echo -e "${BLUE}Current migration status:${NC}"
    mvn flyway:info \
        -Dflyway.url="$DB_URL" \
        -Dflyway.user="$DB_USER" \
        -Dflyway.password="$DB_PASSWORD" \
        -Dflyway.locations=filesystem:src/main/resources/db/migration \
        -Dflyway.baselineOnMigrate=true \
        -Dflyway.baselineVersion=3 \
        2>/dev/null | grep -A 20 "Schema version:"
else
    echo -e "${RED}=========================================="
    echo -e "  ✗ Migration failed!"
    echo -e "==========================================${NC}"
    echo ""
    echo "Please check the error messages above."
    echo ""
    echo "Common issues:"
    echo "  - Database is not running"
    echo "  - Database does not exist (run ./scripts/setup-db.sh)"
    echo "  - PostGIS extension not installed"
    echo "  - Migration SQL has syntax errors"
    exit 1
fi

