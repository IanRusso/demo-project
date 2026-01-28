# Gainfully Database Schema

This document describes the PostgreSQL database schema for the Gainfully job site application.

## Overview

The schema is designed to support a transparent, reciprocal job marketplace with comprehensive tracking of users, employers, job postings, and applications.

## Tables

### User-Related Tables

#### `users`
Core user profile information.

**Key Fields:**
- Personal info: name, email, phone, location
- Professional: education_level, summary, employment_status
- Ratings: user_rating, communication_rating (0-5 scale)
- Job search: salary_expectations, actively_seeking
- Verification: background_check_status

#### `user_skills`
Skills associated with users, including proficiency levels and years of experience.

#### `user_fields_of_interest`
Fields/industries the user is interested in, with optional hard requirement flag.

#### `user_geographical_interests`
Locations where the user is willing to work, with optional hard requirement flag.

#### `user_job_type_interests`
Types of jobs the user is interested in (full-time, part-time, contract, etc.), with optional hard requirement flag.

#### `employment_history`
Past and current employment records for users.

### Employer-Related Tables

#### `employers`
Core employer/company profile information.

**Key Fields:**
- Company info: name, email, phone, location
- Details: company_size, company_type, description
- Ratings: employer_rating, communication_rating (0-5 scale)

#### `employer_history`
Hiring and layoff events, tracked both from this platform and external sources.

**Event Types:**
- `HIRING` - Company hiring events
- `LAYOFF` - Company layoff events

### Job Posting Tables

#### `job_postings`
Active and historical job postings.

**Key Fields:**
- Basic info: title, description, responsibilities
- Location and field
- Compensation: salary_min, salary_max
- Status: ACTIVE, CLOSED, FILLED

#### `job_requirements`
Requirements for each job posting, categorized by type.

**Requirement Types:**
- `HARD` - Must-have requirements
- `SOFT` - Nice-to-have requirements
- `PREFERENCE` - Preferred qualifications

**Requirement Categories:**
- SKILL, EDUCATION, EXPERIENCE, etc.

#### `saved_jobs`
Jobs saved by users for later review.

### Application Tables

#### `applications`
Job applications submitted by users.

**Status Flow:**
1. `SUBMITTED` - Initial application
2. `UNDER_REVIEW` - Employer reviewing
3. `INTERVIEW` - Interview stage
4. `ACCEPTED` - Offer accepted
5. `REJECTED` - Application rejected

**Key Features:**
- Required cover_letter
- response_deadline for guaranteed response times
- Unique constraint: one application per user per job

#### `application_messages`
Communication between users and employers regarding applications.

**Message Types:**
- `MESSAGE` - General communication
- `STATUS_UPDATE` - Application status changes
- `REJECTION_JUSTIFICATION` - Required explanation for rejections

**Sender Types:**
- `USER`
- `EMPLOYER`

## Key Design Decisions

### Transparency & Communication
- **Ratings**: Both users and employers have rating and communication_rating fields
- **Required Justification**: Rejection messages must be provided (enforced at application level)
- **Message Tracking**: All communications are logged with timestamps and read receipts
- **History Tracking**: Both employment history and employer hiring/layoff history are maintained

### Flexibility
- **Preferences**: Users can mark preferences as hard requirements
- **Requirements**: Jobs can have HARD, SOFT, and PREFERENCE requirements
- **Multi-valued Attributes**: Skills, interests, and requirements are in separate tables for flexibility

### Data Integrity
- **Cascading Deletes**: Related records are deleted when parent records are removed
- **Unique Constraints**: Prevent duplicate applications, skills, interests
- **Check Constraints**: Ensure valid status values and rating ranges
- **Foreign Keys**: Maintain referential integrity

### Performance
- **Indexes**: Strategic indexes on frequently queried fields:
  - Email addresses (for login/lookup)
  - Status fields (for filtering)
  - Foreign keys (for joins)
  - Date fields (for sorting/filtering)
  - Location and field (for job search)

## Timestamps

All tables include:
- `created_at`: When the record was created
- `updated_at`: When the record was last modified (where applicable)

## Future Enhancements

Potential additions not in V1:
- User authentication tables (users, roles, permissions)
- Resume/document storage references
- Interview scheduling
- Offer management
- Reviews/testimonials
- Company verification
- Notification preferences
- Search history
- Analytics/metrics tables

## Migration

This schema is implemented as Flyway migration `V1__Initial_Schema.sql`.

To apply the migration:
1. Configure database connection in `config.yml`
2. Add Flyway dependency to `pom.xml`
3. Run the application - Flyway will automatically apply migrations

## Naming Conventions

- **Tables**: Plural, lowercase with underscores (e.g., `job_postings`)
- **Columns**: Lowercase with underscores (e.g., `salary_min`)
- **Indexes**: Prefixed with `idx_` followed by table and column(s)
- **Constraints**: Prefixed with `chk_` for check constraints
- **Primary Keys**: `id` (BIGSERIAL)
- **Foreign Keys**: `{table}_id` (e.g., `user_id`)

