# Database Setup Guide

This guide will help you set up PostgreSQL for the Gainfully application.

## Prerequisites

- PostgreSQL 12 or higher installed
- Access to create databases and users

## Installation

### macOS (using Homebrew)

```bash
# Install PostgreSQL
brew install postgresql@15

# Start PostgreSQL service
brew services start postgresql@15

# Or start manually
pg_ctl -D /opt/homebrew/var/postgresql@15 start
```

### Ubuntu/Debian

```bash
# Install PostgreSQL
sudo apt update
sudo apt install postgresql postgresql-contrib

# Start PostgreSQL service
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

### Windows

1. Download PostgreSQL installer from https://www.postgresql.org/download/windows/
2. Run the installer and follow the setup wizard
3. Remember the password you set for the `postgres` user

## Quick Setup (Automated)

### Using Setup Script (Recommended)

The easiest way to set up the database is to use the provided setup script:

**Mac/Linux:**
```bash
./setup-db.sh
```

**Windows:**
```cmd
setup-db.bat
```

The script will:
- ✅ Check if PostgreSQL is installed and running
- ✅ Create the `gainfully_db` database
- ✅ Create the `gainfully_user` user with password
- ✅ Grant all necessary privileges
- ✅ Test the database connection
- ✅ Display configuration summary

If PostgreSQL is not running, the script will prompt you to start it.

---

## Manual Setup (Alternative)

If you prefer to set up the database manually:

### 1. Create Database and User

Connect to PostgreSQL as the postgres user:

```bash
# macOS/Linux
psql postgres

# Windows (from Command Prompt)
psql -U postgres
```

Then run the following SQL commands:

```sql
-- Create the database
CREATE DATABASE gainfully_db;

-- Create the user
CREATE USER gainfully_user WITH PASSWORD 'gainfully_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE gainfully_db TO gainfully_user;

-- Connect to the database
\c gainfully_db

-- Grant schema privileges (PostgreSQL 15+)
GRANT ALL ON SCHEMA public TO gainfully_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO gainfully_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO gainfully_user;

-- Set default privileges for future tables
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gainfully_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO gainfully_user;

-- Exit psql
\q
```

### 2. Verify Connection

Test the connection with the new user:

```bash
psql -U gainfully_user -d gainfully_db -h localhost
```

If prompted for a password, enter: `gainfully_password`

You should see the PostgreSQL prompt. Type `\q` to exit.

## Configuration

The application is configured to connect to PostgreSQL with these settings (in `config.yml`):

```yaml
database:
  driverClass: org.postgresql.Driver
  user: gainfully_user
  password: gainfully_password
  url: jdbc:postgresql://localhost:5432/gainfully_db
```

### Customizing Database Configuration

If you want to use different credentials or database name:

1. Update the SQL commands above with your preferred values
2. Update `config.yml` with the same values

**For production**, you should:
- Use strong passwords
- Store credentials in environment variables
- Use a separate database user with minimal required privileges

### Using Environment Variables (Recommended for Production)

Update `config.yml` to use environment variables:

```yaml
database:
  driverClass: org.postgresql.Driver
  user: ${DB_USER:-gainfully_user}
  password: ${DB_PASSWORD:-gainfully_password}
  url: ${DB_URL:-jdbc:postgresql://localhost:5432/gainfully_db}
```

Then set environment variables:

```bash
export DB_USER=gainfully_user
export DB_PASSWORD=your_secure_password
export DB_URL=jdbc:postgresql://localhost:5432/gainfully_db
```

## Running Migrations

Flyway migrations will run automatically when you start the application.

### Manual Migration Commands

You can also run migrations manually using Dropwizard's migration commands:

```bash
# Check migration status
java -jar target/demo-project-1.0-SNAPSHOT.jar db status config.yml

# Run pending migrations
java -jar target/demo-project-1.0-SNAPSHOT.jar db migrate config.yml

# Validate migrations
java -jar target/demo-project-1.0-SNAPSHOT.jar db validate config.yml

# Generate a SQL script for migrations (dry run)
java -jar target/demo-project-1.0-SNAPSHOT.jar db dump config.yml
```

### First Run

When you first start the application:

1. Flyway will create a `flyway_schema_history` table to track migrations
2. It will run `V1__Initial_Schema.sql` to create all tables
3. You'll see log output showing the migration progress

Example output:
```
INFO  [2024-01-05 10:00:00,000] org.flywaydb.core.internal.command.DbValidate: Successfully validated 1 migration (execution time 00:00.012s)
INFO  [2024-01-05 10:00:00,100] org.flywaydb.core.internal.command.DbMigrate: Current version of schema "public": << Empty Schema >>
INFO  [2024-01-05 10:00:00,200] org.flywaydb.core.internal.command.DbMigrate: Migrating schema "public" to version "1 - Initial Schema"
INFO  [2024-01-05 10:00:00,500] org.flywaydb.core.internal.command.DbMigrate: Successfully applied 1 migration to schema "public" (execution time 00:00.400s)
```

## Verifying the Schema

After the application starts successfully, you can verify the tables were created:

```bash
# Connect to the database
psql -U gainfully_user -d gainfully_db -h localhost

# List all tables
\dt

# Describe a specific table
\d employees

# View all columns in a table
\d+ job_postings

# Exit
\q
```

You should see 15 tables:
- employees
- employee_skills
- employee_fields_of_interest
- employee_geographical_interests
- employee_job_type_interests
- employment_history
- employers
- employer_history
- job_postings
- job_requirements
- saved_jobs
- applications
- application_messages
- flyway_schema_history (created by Flyway)

## Troubleshooting

### Connection Refused

If you get "connection refused" errors:

1. Check if PostgreSQL is running:
   ```bash
   # macOS
   brew services list | grep postgresql
   
   # Linux
   sudo systemctl status postgresql
   ```

2. Check PostgreSQL is listening on port 5432:
   ```bash
   sudo lsof -i :5432
   ```

3. Check `pg_hba.conf` allows local connections:
   ```bash
   # macOS (Homebrew)
   cat /opt/homebrew/var/postgresql@15/pg_hba.conf
   
   # Linux
   sudo cat /etc/postgresql/15/main/pg_hba.conf
   ```

   Should have a line like:
   ```
   host    all             all             127.0.0.1/32            md5
   ```

### Authentication Failed

If you get "password authentication failed":

1. Reset the user password:
   ```sql
   psql postgres
   ALTER USER gainfully_user WITH PASSWORD 'gainfully_password';
   \q
   ```

2. Verify the password in `config.yml` matches

### Permission Denied

If you get permission errors when creating tables:

```sql
psql postgres
\c gainfully_db
GRANT ALL ON SCHEMA public TO gainfully_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gainfully_user;
\q
```

## Resetting the Database

If you need to start fresh:

```bash
# Connect as postgres user
psql postgres

# Drop and recreate
DROP DATABASE gainfully_db;
CREATE DATABASE gainfully_db;
GRANT ALL PRIVILEGES ON DATABASE gainfully_db TO gainfully_user;

\c gainfully_db
GRANT ALL ON SCHEMA public TO gainfully_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO gainfully_user;

\q
```

Then restart the application to run migrations again.

## Next Steps

After setting up the database:

1. Start the application: `./start.sh` or `./start-dev.sh`
2. Check the logs for successful migration
3. Verify tables were created using the commands above
4. Start building DAOs to interact with the database

See `DATABASE_SCHEMA.md` for detailed information about the database structure.

