#!/bin/bash

# Demo Project Development Startup Script
# This script starts both servers without rebuilding (faster for development)

set -e  # Exit on error

echo "=========================================="
echo "  Demo Project - Development Mode"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to cleanup background processes on exit
cleanup() {
    echo ""
    echo -e "${YELLOW}Shutting down servers...${NC}"
    if [ ! -z "$BACKEND_PID" ]; then
        kill $BACKEND_PID 2>/dev/null || true
        echo "Backend server stopped"
    fi
    if [ ! -z "$FRONTEND_PID" ]; then
        kill $FRONTEND_PID 2>/dev/null || true
        echo "Frontend server stopped"
    fi
    exit 0
}

# Trap SIGINT (Ctrl+C) and SIGTERM
trap cleanup SIGINT SIGTERM

# Check if backend JAR exists
if [ ! -f "target/demo-project-1.0-SNAPSHOT.jar" ]; then
    echo -e "${YELLOW}Backend JAR not found. Building...${NC}"
    mvn clean package -DskipTests
    echo -e "${GREEN}✓ Backend built successfully${NC}"
    echo ""
fi

# Start backend server in background
echo -e "${GREEN}Starting backend server...${NC}"
java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml > backend.log 2>&1 &
BACKEND_PID=$!
echo "Backend PID: $BACKEND_PID"
echo "Backend logs: backend.log"

# Wait a moment for backend to start
sleep 3

# Start frontend server in background
echo -e "${GREEN}Starting frontend server...${NC}"
cd ui
npm start > ../frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..
echo "Frontend PID: $FRONTEND_PID"
echo "Frontend logs: frontend.log"

echo ""
echo "=========================================="
echo -e "${GREEN}✓ All servers started successfully!${NC}"
echo "=========================================="
echo ""
echo "Backend Server:"
echo "  - Application: http://localhost:8080"
echo "  - Admin/Health: http://localhost:8081"
echo ""
echo "Frontend Server:"
echo "  - UI: http://localhost:3000"
echo ""
echo "Press Ctrl+C to stop all servers"
echo "=========================================="
echo ""

# Wait for user to press Ctrl+C
wait

