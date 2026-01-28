#!/bin/bash

# Demo Project Stop Script
# This script stops all running backend and frontend servers

echo "=========================================="
echo "  Stopping Demo Project Servers"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to kill processes on a specific port
kill_port() {
    local port=$1
    local name=$2
    
    echo -n "Stopping $name (port $port)... "
    
    # Find and kill process on the port
    local pid=$(lsof -ti:$port 2>/dev/null)
    
    if [ -z "$pid" ]; then
        echo -e "${YELLOW}not running${NC}"
    else
        kill -9 $pid 2>/dev/null
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}stopped (PID: $pid)${NC}"
        else
            echo -e "${RED}failed${NC}"
        fi
    fi
}

# Stop backend application server (port 8080)
kill_port 8080 "Backend Application"

# Stop backend admin server (port 8081)
kill_port 8081 "Backend Admin"

# Stop frontend server (port 3000)
kill_port 3000 "Frontend"

# Also kill any Java processes running the demo-project JAR
echo -n "Stopping any remaining backend processes... "
pkill -f "demo-project-1.0-SNAPSHOT.jar" 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}stopped${NC}"
else
    echo -e "${YELLOW}none found${NC}"
fi

# Kill any npm start processes in the ui directory
echo -n "Stopping any remaining frontend processes... "
pkill -f "npm.*start" 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}stopped${NC}"
else
    echo -e "${YELLOW}none found${NC}"
fi

echo ""
echo "=========================================="
echo -e "${GREEN}✓ All servers stopped${NC}"
echo "=========================================="

