#!/bin/bash

# City Import Script for Gainfully Application
# This script calls the REST API to import city data from the local tar file

set -e  # Exit on error

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
API_URL="http://localhost:8080/api/cities/import"
DATA_FILE="data/cities15000.tar"
WORKSPACE_DIR="/Users/ian.russo/git/demo-project"

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  City Data Import Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if data file exists
if [ ! -f "$DATA_FILE" ]; then
    echo -e "${RED}✗ Data file not found: $DATA_FILE${NC}"
    echo ""
    echo "Please ensure the cities data file exists at:"
    echo "  $WORKSPACE_DIR/$DATA_FILE"
    exit 1
fi

echo -e "${GREEN}✓ Data file found: $DATA_FILE${NC}"
FILE_SIZE=$(ls -lh "$DATA_FILE" | awk '{print $5}')
echo "  Size: $FILE_SIZE"
echo ""

# Check if server is running
echo -e "${BLUE}Checking if server is running...${NC}"
if ! curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/cities > /dev/null 2>&1; then
    echo -e "${RED}✗ Server is not running${NC}"
    echo ""
    echo "Please start the server first:"
    echo "  mvn clean package -DskipTests"
    echo "  java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml"
    echo ""
    echo "Or use the start script:"
    echo "  ./start.sh"
    exit 1
fi

echo -e "${GREEN}✓ Server is running${NC}"
echo ""

# Construct file:// URL
FILE_URL="file://$WORKSPACE_DIR/$DATA_FILE"

echo -e "${BLUE}Starting city import...${NC}"
echo "  URL: $FILE_URL"
echo ""

# Make the API call
RESPONSE=$(curl -s -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{\"url\": \"$FILE_URL\"}" \
    -w "\n%{http_code}")

# Extract HTTP status code (last line)
HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
# Extract response body (all but last line)
RESPONSE_BODY=$(echo "$RESPONSE" | sed '$d')

# Check if request was successful
if [ "$HTTP_CODE" -eq 202 ]; then
    echo -e "${GREEN}✓ Import request accepted${NC}"
    echo ""
    echo "Response:"
    echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
    echo ""
    echo -e "${YELLOW}Note: The import is running in the background.${NC}"
    echo "Check the server logs for progress and completion status."
    echo ""
    echo "To monitor the logs:"
    echo "  tail -f logs/application.log"
    echo ""
    echo "To check imported cities:"
    echo "  curl http://localhost:8080/api/cities | python3 -m json.tool"
elif [ "$HTTP_CODE" -eq 400 ]; then
    echo -e "${RED}✗ Bad request${NC}"
    echo ""
    echo "Response:"
    echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
    exit 1
elif [ "$HTTP_CODE" -eq 500 ]; then
    echo -e "${RED}✗ Server error${NC}"
    echo ""
    echo "Response:"
    echo "$RESPONSE_BODY" | python3 -m json.tool 2>/dev/null || echo "$RESPONSE_BODY"
    exit 1
else
    echo -e "${RED}✗ Unexpected response (HTTP $HTTP_CODE)${NC}"
    echo ""
    echo "Response:"
    echo "$RESPONSE_BODY"
    exit 1
fi

echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}  Import request completed!${NC}"
echo -e "${BLUE}========================================${NC}"

