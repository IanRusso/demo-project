#!/bin/bash

# Demo Project Startup Script
# This script builds and starts both the backend and frontend servers

set -e  # Exit on error

echo "=========================================="
echo "  Demo Project Startup"
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

# Step 1: Build Backend
echo -e "${BLUE}[1/3] Building backend...${NC}"
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "Backend build failed!"
    exit 1
fi
echo -e "${GREEN}✓ Backend built successfully${NC}"
echo ""

# Step 2: Build Frontend
echo -e "${BLUE}[2/3] Building frontend...${NC}"
cd ui
npm run build
if [ $? -ne 0 ]; then
    echo "Frontend build failed!"
    exit 1
fi
cd ..
echo -e "${GREEN}✓ Frontend built successfully${NC}"
echo ""

# Step 3: Start servers
echo -e "${BLUE}[3/3] Starting servers...${NC}"
echo ""

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

