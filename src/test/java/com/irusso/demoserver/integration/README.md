# Integration Tests

This directory contains integration tests for the Gainfully Server application. These tests verify that the Dropwizard server starts correctly and that the REST API endpoints work as expected with a live server and database.

## Test Configuration

The integration tests use a separate test configuration file located at:
- `src/test/resources/test-config.yml`

This configuration:
- Uses random ports (port: 0) to avoid conflicts with running servers
- Connects to a separate test database: `gainfully_db_test`
- Ensures tests don't interfere with development data

## Prerequisites

Before running integration tests, ensure:

1. **PostgreSQL is running** on localhost:5432
2. **Test database exists**: `gainfully_db_test`
3. **Database schema is up to date** (run migrations on test database)

### Setting Up the Test Database

```bash
# Create the test database (if it doesn't exist)
psql -U ian.russo -d postgres -c "CREATE DATABASE gainfully_db_test OWNER gainfully_user;"

# Run migrations on the test database
psql -U gainfully_user -d gainfully_db_test -f src/main/resources/db/migration/V1__Initial_Schema.sql
```

### Cleaning Test Data

To clean test data between runs:

```bash
psql -U gainfully_user -d gainfully_db_test -c "TRUNCATE users, employers, job_postings, applications, application_messages, job_requirements, saved_jobs, employment_history, employer_history, user_skills, user_fields_of_interest, user_geographical_interests, user_job_type_interests CASCADE;"
```

## Running Integration Tests

### Run all integration tests:
```bash
mvn test -Dtest="*IntegrationTest"
```

### Run a specific integration test:
```bash
mvn test -Dtest=BasicServerIntegrationTest
```

### Run all tests (unit + integration):
```bash
mvn test
```

## Test Coverage

### BasicServerIntegrationTest
Tests basic server functionality and CRUD operations:
- **testServerIsRunning** - Verifies the server started successfully
- **testHealthCheck** - Tests the health check endpoint
- **testUserCrudOperations** - Tests Create, Read, Update, Delete operations for Users
- **testEmployerCrudOperations** - Tests Create, Read, List operations for Employers
- **testGetNonExistentResource** - Tests 404 error handling

## Notes

- Integration tests use unique email addresses (with UUIDs) to avoid database constraint violations
- Tests are ordered using `@Order` annotations to ensure predictable execution
- The `DropwizardAppExtension` automatically starts and stops the server for each test class
- Jackson's `JavaTimeModule` is registered to handle Java 8 date/time types (e.g., `java.time.Instant`)

