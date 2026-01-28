# Quick Start Guide

Get the Gainfully application up and running in 3 simple steps!

## Prerequisites

Make sure you have these installed:
- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ Node.js 14+
- ✅ PostgreSQL 12+

## Step 1: Setup Database

Run the database setup script:

**Mac/Linux:**
```bash
./setup-db.sh
```

**Windows:**
```cmd
setup-db.bat
```

This will create the PostgreSQL database and user automatically.

## Step 2: Start the Application

**Mac/Linux:**
```bash
./start.sh
```

**Windows:**
```cmd
start.bat
```

This will:
- Build the backend (Maven)
- Run Flyway migrations (create database tables)
- Start the backend server (ports 8080, 8081)
- Install frontend dependencies (npm)
- Start the frontend server (port 3000)

## Step 3: Access the Application

Open your browser to:
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api
- **Health Check**: http://localhost:8081/healthcheck

## Stopping the Application

**Mac/Linux:**
```bash
./stop.sh
```

**Windows:**
```cmd
stop.bat
```

## Troubleshooting

### PostgreSQL Not Running

**Mac:**
```bash
brew services start postgresql@15
```

**Linux:**
```bash
sudo systemctl start postgresql
```

**Windows:**
- Open Services (services.msc)
- Find "postgresql-x64-XX" service
- Start the service

### Port Already in Use

If ports 8080, 8081, or 3000 are already in use:

1. Stop the application: `./stop.sh` or `stop.bat`
2. Check for processes: `lsof -ti:8080,8081,3000` (Mac/Linux)
3. Kill processes if needed

### Build Errors

Clean and rebuild:
```bash
mvn clean package -DskipTests
```

### Database Connection Errors

1. Verify PostgreSQL is running
2. Re-run the database setup: `./setup-db.sh` or `setup-db.bat`
3. Check credentials in `config.yml`

## Development Workflow

### Quick Restart (No Rebuild)

**Mac/Linux:**
```bash
./start-dev.sh
```

This skips the Maven build and npm install, starting servers immediately.

### Manual Database Migrations

Check migration status:
```bash
java -jar target/demo-project-1.0-SNAPSHOT.jar db status config.yml
```

Run migrations manually:
```bash
java -jar target/demo-project-1.0-SNAPSHOT.jar db migrate config.yml
```

### Connect to Database

```bash
psql -U gainfully_user -d gainfully_db -h localhost
```

Password: `gainfully_password`

List tables:
```sql
\dt
```

## Next Steps

- 📖 Read [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) to understand the database structure
- 📖 Read [DATABASE_SETUP.md](DATABASE_SETUP.md) for detailed database setup options
- 📖 Read [README.md](README.md) for complete documentation
- 🔧 Start building DAOs to interact with the database
- 🧪 Write tests for your new features

## Useful Commands

### Backend

```bash
# Build only
mvn clean package -DskipTests

# Run backend only
java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml

# Run tests
mvn test
```

### Frontend

```bash
# Install dependencies
cd ui && npm install

# Start dev server
cd ui && npm start

# Build for production
cd ui && npm run build
```

### Database

```bash
# Reset database (WARNING: Deletes all data!)
./setup-db.sh  # Re-run setup script

# Backup database
pg_dump -U gainfully_user -d gainfully_db -h localhost > backup.sql

# Restore database
psql -U gainfully_user -d gainfully_db -h localhost < backup.sql
```

## Project Structure

```
demo-project/
├── src/main/java/              # Backend Java code
│   └── com/irusso/demoserver/
│       ├── application/        # Main application & config
│       └── resources/          # REST API endpoints
├── src/main/resources/         # Backend resources
│   └── db/migration/           # Flyway SQL migrations
├── ui/                         # React frontend
│   ├── src/                    # Frontend source code
│   └── public/                 # Static assets
├── config.yml                  # Application configuration
├── setup-db.sh                 # Database setup (Mac/Linux)
├── setup-db.bat                # Database setup (Windows)
├── start.sh                    # Start all (Mac/Linux)
├── start.bat                   # Start all (Windows)
├── start-dev.sh                # Quick start (Mac/Linux)
├── stop.sh                     # Stop all (Mac/Linux)
└── stop.bat                    # Stop all (Windows)
```

## Support

For detailed information, see:
- [README.md](README.md) - Complete documentation
- [DATABASE_SETUP.md](DATABASE_SETUP.md) - Database setup guide
- [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) - Database schema documentation

