# Demo Project

A full-stack demo application with a Dropwizard REST API backend and React + TypeScript + Material UI frontend.

## Features

- **Backend (Dropwizard 4.0.0)**
  - RESTful API with user management endpoints
  - Health check monitoring
  - CORS support for frontend integration
  - Admin interface on port 8081

- **Frontend (React 18 + TypeScript + Material UI)**
  - Modern Material Design UI
  - Real-time backend health monitoring
  - Auto-refresh health status every 5 seconds
  - Interactive counter demo

## Quick Start

### Option 1: Using Startup Scripts (Recommended)

**For Mac/Linux:**
```bash
# Full build and start (first time or after code changes)
./start.sh

# Development mode (faster, skips rebuild if JAR exists)
./start-dev.sh

# Stop all servers
./stop.sh
```

**For Windows:**
```cmd
# Full build and start
start.bat

# Stop all servers
stop.bat
```

The start scripts will:
1. Build the backend (Maven)
2. Build the frontend (npm)
3. Start both servers
4. Display URLs and status

**To stop servers:**
- Press `Ctrl+C` (Mac/Linux with start scripts)
- Run `./stop.sh` (Mac/Linux) or `stop.bat` (Windows)
- Close the server windows (Windows when using start.bat)

### Option 2: Manual Start

**Backend:**
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml
```

**Frontend:**
```bash
cd ui

# Install dependencies (first time only)
npm install

# Development server
npm start

# Production build
npm run build
```

## Access Points

Once started, you can access:

- **Frontend UI**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **Backend Admin/Health**: http://localhost:8081
- **Health Check Endpoint**: http://localhost:8081/healthcheck

## API Endpoints

### User Management
- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Health Monitoring
- `GET /healthcheck` (port 8081) - Get server health status

## Example API Calls

```bash
# Get all users
curl http://localhost:8080/api/users

# Create a user
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","role":"User"}'

# Get health status
curl http://localhost:8081/healthcheck
```

## Project Structure

```
demo-project/
├── src/main/java/com/irusso/demoserver/    # Backend source code
│   ├── application/                         # Main application
│   ├── api/                                 # API models
│   └── resources/                           # REST endpoints
├── ui/                                      # Frontend application
│   ├── src/                                 # React source code
│   ├── public/                              # Static assets
│   └── dist/                                # Production build
├── config.yml                               # Dropwizard configuration
├── pom.xml                                  # Maven configuration
├── start.sh                                 # Unix/Mac startup script
├── start-dev.sh                             # Unix/Mac dev startup script
├── start.bat                                # Windows startup script
├── stop.sh                                  # Unix/Mac stop script
└── stop.bat                                 # Windows stop script
```

## Development

### Backend Development
- Java 17 required
- Uses Dropwizard 4.0.0 framework
- Maven for dependency management
- Follows Google Java Style Guide

### Frontend Development
- Node.js and npm required
- React 18 with TypeScript
- Material UI v5 for components
- Webpack 5 for bundling

### Making Changes

1. **Backend changes**: Rebuild with `mvn clean package -DskipTests` and restart
2. **Frontend changes**: Webpack dev server auto-reloads on file changes

## Logs

When using the startup scripts, logs are written to:
- `backend.log` - Backend server logs
- `frontend.log` - Frontend server logs

## Requirements

- **Java**: JDK 17 or higher
- **Maven**: 3.6 or higher
- **Node.js**: 14 or higher
- **npm**: 6 or higher

## Troubleshooting

**Port already in use:**
```bash
# Kill process on port 8080 (backend)
lsof -ti:8080 | xargs kill -9

# Kill process on port 3000 (frontend)
lsof -ti:3000 | xargs kill -9

# Kill process on port 8081 (admin)
lsof -ti:8081 | xargs kill -9
```

**Backend won't start:**
- Check Java version: `java -version`
- Verify config.yml exists
- Check backend.log for errors

**Frontend won't start:**
- Run `npm install` in the ui directory
- Check frontend.log for errors
- Verify backend is running (frontend needs it for health checks)

## License

Demo Project - For demonstration purposes

