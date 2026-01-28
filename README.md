# Gainfully

A job site driven by reciprocal communication and transparency.

> **🚀 New to this project?** See [QUICK_START.md](QUICK_START.md) for a 3-step setup guide!

## Overview

Gainfully is a full-stack job marketplace application designed to promote transparency and meaningful communication between job seekers and employers. The platform features comprehensive employee and employer profiles, detailed job postings, and a transparent application process with required communication.

### Technology Stack

- **Backend**: Dropwizard 4.0.0 (Java 17)
  - RESTful API with JAX-RS
  - PostgreSQL database with JDBI3
  - Flyway database migrations
  - Built-in health monitoring and metrics
  - CORS-enabled for frontend integration

- **Frontend**: React 18 + TypeScript + Material UI v5
  - Modern, responsive Material Design interface
  - Real-time health monitoring
  - Type-safe development with TypeScript

- **Database**: PostgreSQL 12+
  - 15-table schema covering employees, employers, jobs, and applications
  - Automatic schema migrations with Flyway
  - See [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for details

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+
- Node.js 14+
- PostgreSQL 12+

### Setup

1. **Setup Database**: Run `./setup-db.sh` (Mac/Linux) or `setup-db.bat` (Windows)
2. **Start Application**: Run `./start.sh` (Mac/Linux) or `start.bat` (Windows)
3. **Access**: Open http://localhost:3000

For detailed setup instructions, see [QUICK_START.md](QUICK_START.md).

For manual database setup, see [DATABASE_SETUP.md](DATABASE_SETUP.md).

## Application URLs

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:3000 | React UI application |
| Backend API | http://localhost:8080 | REST API endpoints |
| Admin Console | http://localhost:8081 | Dropwizard admin interface |
| Health Check | http://localhost:8081/healthcheck | Server health status |

## API Reference

### User Management (Demo Endpoints)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Health Monitoring

| Method | Endpoint | Port | Description |
|--------|----------|------|-------------|
| GET | `/healthcheck` | 8081 | Server health status (includes deadlock detection) |

### Example API Calls

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
├── src/main/java/com/irusso/demoserver/
│   ├── application/              # Main application & configuration
│   │   ├── GainfullyServerApplication.java
│   │   └── model/                # Configuration models
│   ├── api/                      # API models (DTOs)
│   ├── db/                       # Database layer
│   │   ├── dao/                  # Data Access Objects
│   │   │   ├── StandardDao.java  # Abstract base DAO
│   │   │   ├── EmployeeDao.java  # Example DAO implementation
│   │   │   └── README.md         # DAO documentation
│   │   └── model/                # Database entity models
│   │       └── Employee.java     # Example entity
│   ├── resources/                # REST API endpoints
│   │   ├── UserResource.java
│   │   └── HealthCheckResource.java
│   └── service/                  # Business logic layer
├── src/main/resources/
│   └── db/migration/             # Flyway SQL migrations
│       └── V1__Initial_Schema.sql
├── ui/                           # React frontend
│   ├── src/                      # TypeScript source code
│   ├── public/                   # Static assets
│   └── package.json              # npm dependencies
├── config.yml                    # Dropwizard configuration
├── pom.xml                       # Maven dependencies
├── setup-db.sh / setup-db.bat    # Database setup scripts
├── start.sh / start.bat          # Application startup scripts
├── start-dev.sh                  # Quick dev start (Mac/Linux)
├── stop.sh / stop.bat            # Application stop scripts
├── QUICK_START.md                # Quick setup guide
├── DATABASE_SETUP.md             # Database setup documentation
└── DATABASE_SCHEMA.md            # Database schema documentation
```

## Development

### Backend Development

**Technology:**
- Java 17
- Dropwizard 4.0.0 (Jersey, Jetty, Jackson)
- JDBI3 for database access
- Flyway for migrations
- Maven for build and dependency management

**Style Guide:** [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

**Development Workflow:**
```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/demo-project-1.0-SNAPSHOT.jar server config.yml

# Run tests
mvn test

# Check migration status
java -jar target/demo-project-1.0-SNAPSHOT.jar db status config.yml
```

### Frontend Development

**Technology:**
- React 18 with TypeScript
- Material UI v5
- Webpack 5

**Development Workflow:**
```bash
cd ui

# Install dependencies
npm install

# Start dev server (auto-reload on changes)
npm start

# Build for production
npm run build

# Run tests
npm test
```

### Database Development

**Creating Migrations:**

1. Create a new migration file in `src/main/resources/db/migration/`
2. Follow naming convention: `V{version}__{description}.sql`
   - Example: `V2__Add_User_Preferences.sql`
3. Write SQL DDL statements
4. Restart application to apply migration

**Useful Commands:**
```bash
# Check migration status
java -jar target/demo-project-1.0-SNAPSHOT.jar db status config.yml

# Manually run migrations
java -jar target/demo-project-1.0-SNAPSHOT.jar db migrate config.yml

# Connect to database
psql -U gainfully_user -d gainfully_db -h localhost
```

See [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for schema documentation.

### Logs

Startup scripts write logs to:
- `backend.log` - Backend server output
- `frontend.log` - Frontend server output

### Scripts Reference

| Script | Platform | Purpose |
|--------|----------|---------|
| `setup-db.sh` | Mac/Linux | Setup PostgreSQL database |
| `setup-db.bat` | Windows | Setup PostgreSQL database |
| `start.sh` | Mac/Linux | Full build and start |
| `start-dev.sh` | Mac/Linux | Quick start (skip build) |
| `start.bat` | Windows | Full build and start |
| `stop.sh` | Mac/Linux | Stop all servers |
| `stop.bat` | Windows | Stop all servers |

## Troubleshooting

See [QUICK_START.md](QUICK_START.md) for common troubleshooting steps.

**Port conflicts:**
```bash
# Mac/Linux - Kill processes on ports
lsof -ti:8080,8081,3000 | xargs kill -9

# Or use the stop script
./stop.sh
```

**Database connection issues:**
```bash
# Verify PostgreSQL is running
pg_isready -h localhost -p 5432

# Re-run database setup
./setup-db.sh

# Test connection manually
psql -U gainfully_user -d gainfully_db -h localhost
```

**Build failures:**
```bash
# Clean and rebuild
mvn clean package -DskipTests

# Clear npm cache
cd ui && rm -rf node_modules package-lock.json && npm install
```

## Documentation

- **[QUICK_START.md](%20documentation/QUICK_START.md)** - 3-step setup guide for new users
- **[DATABASE_SETUP.md](%20documentation/DATABASE_SETUP.md)** - Detailed database setup instructions
- **[DATABASE_SCHEMA.md](%20documentation/DATABASE_SCHEMA.md)** - Complete database schema documentation
- **[APP_DESIGN.md](%20documentation/APP_DESIGN.md)** - Application design and requirements

## Architecture

### Backend Architecture

```
Request → Jersey (JAX-RS) → Resource → DAO → JDBI3 → PostgreSQL
                                ↓
                          Health Checks
                          Metrics
```

**Key Components:**
- **Resources**: REST API endpoints (JAX-RS)
- **DAOs**: Data Access Objects (to be implemented)
- **JDBI3**: SQL mapping and database access
- **Flyway**: Database migration management
- **Dropwizard**: Framework providing Jersey, Jetty, Jackson, Metrics

### Frontend Architecture

```
React Components → API Client → Backend REST API
       ↓
   Material UI
   TypeScript
```
 
**Key Components:**
- **Components**: React functional components with hooks
- **TypeScript**: Type-safe development
- **Material UI**: Pre-built UI components
- **Webpack**: Module bundling and dev server

### Database Schema

The application uses a comprehensive 15-table schema:

**Employee Tables:**
- `employees` - Core employee profiles
- `employee_skills` - Skills with proficiency levels
- `employee_fields_of_interest` - Industry preferences
- `employee_geographical_interests` - Location preferences
- `employee_job_type_interests` - Job type preferences
- `employment_history` - Work history

**Employer Tables:**
- `employers` - Company profiles
- `employer_history` - Hiring/layoff events

**Job Posting Tables:**
- `job_postings` - Job listings
- `job_requirements` - Requirements (hard/soft/preference)
- `saved_jobs` - Employee saved jobs

**Application Tables:**
- `applications` - Job applications with status tracking
- `application_messages` - Communication thread

See [DATABASE_SCHEMA.md](DATABASE_SCHEMA.md) for complete details.

## Contributing

This project follows the Google Java Style Guide for backend code. Use modularized code and avoid colossal classes in favor of smaller, single-purpose classes.

## License

Gainfully - For demonstration purposes
