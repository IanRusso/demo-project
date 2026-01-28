-- Gainfully Job Site - Initial Database Schema
-- This migration creates the core tables for employees, employers, job postings, and applications

-- ============================================
-- EMPLOYEE PROFILES
-- ============================================

CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    location VARCHAR(255),
    education_level VARCHAR(100),
    summary TEXT,
    profile_picture_url VARCHAR(500),
    employment_status VARCHAR(50),
    background_check_status VARCHAR(50),
    employee_rating DECIMAL(3,2) DEFAULT 0.00,
    communication_rating DECIMAL(3,2) DEFAULT 0.00,
    salary_expectations_min DECIMAL(12,2),
    salary_expectations_max DECIMAL(12,2),
    actively_seeking BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_employee_rating CHECK (employee_rating >= 0 AND employee_rating <= 5),
    CONSTRAINT chk_communication_rating CHECK (communication_rating >= 0 AND communication_rating <= 5)
);

CREATE INDEX idx_employees_email ON employees(email);
CREATE INDEX idx_employees_actively_seeking ON employees(actively_seeking);
CREATE INDEX idx_employees_location ON employees(location);

-- ============================================
-- EMPLOYEE SKILLS
-- ============================================

CREATE TABLE employee_skills (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    skill_name VARCHAR(100) NOT NULL,
    proficiency_level VARCHAR(50),
    years_of_experience INTEGER,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, skill_name)
);

CREATE INDEX idx_employee_skills_employee_id ON employee_skills(employee_id);
CREATE INDEX idx_employee_skills_skill_name ON employee_skills(skill_name);

-- ============================================
-- EMPLOYEE FIELDS OF INTEREST
-- ============================================

CREATE TABLE employee_fields_of_interest (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    field_name VARCHAR(100) NOT NULL,
    is_hard_requirement BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, field_name)
);

CREATE INDEX idx_employee_fields_employee_id ON employee_fields_of_interest(employee_id);

-- ============================================
-- EMPLOYEE GEOGRAPHICAL AREAS OF INTEREST
-- ============================================

CREATE TABLE employee_geographical_interests (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    location VARCHAR(255) NOT NULL,
    is_hard_requirement BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, location)
);

CREATE INDEX idx_employee_geo_employee_id ON employee_geographical_interests(employee_id);

-- ============================================
-- EMPLOYEE JOB TYPE INTERESTS
-- ============================================

CREATE TABLE employee_job_type_interests (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    job_type VARCHAR(100) NOT NULL,
    is_hard_requirement BOOLEAN DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, job_type)
);

CREATE INDEX idx_employee_job_type_employee_id ON employee_job_type_interests(employee_id);

-- ============================================
-- EMPLOYMENT HISTORY
-- ============================================

CREATE TABLE employment_history (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    employer_name VARCHAR(255) NOT NULL,
    job_title VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT false,
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_employment_history_employee_id ON employment_history(employee_id);
CREATE INDEX idx_employment_history_is_current ON employment_history(is_current);

-- ============================================
-- EMPLOYER PROFILES
-- ============================================

CREATE TABLE employers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(50),
    location VARCHAR(255),
    company_size VARCHAR(50),
    company_type VARCHAR(100),
    description TEXT,
    company_picture_url VARCHAR(500),
    employer_rating DECIMAL(3,2) DEFAULT 0.00,
    communication_rating DECIMAL(3,2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_employer_rating CHECK (employer_rating >= 0 AND employer_rating <= 5),
    CONSTRAINT chk_employer_communication_rating CHECK (communication_rating >= 0 AND communication_rating <= 5)
);

CREATE INDEX idx_employers_email ON employers(email);
CREATE INDEX idx_employers_location ON employers(location);

-- ============================================
-- EMPLOYER HIRING/LAYOFF HISTORY
-- ============================================

CREATE TABLE employer_history (
    id BIGSERIAL PRIMARY KEY,
    employer_id BIGINT NOT NULL REFERENCES employers(id) ON DELETE CASCADE,
    event_type VARCHAR(50) NOT NULL, -- 'HIRING' or 'LAYOFF'
    event_date DATE NOT NULL,
    number_of_positions INTEGER,
    source VARCHAR(100), -- 'INTERNAL' or external website name
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_event_type CHECK (event_type IN ('HIRING', 'LAYOFF'))
);

CREATE INDEX idx_employer_history_employer_id ON employer_history(employer_id);
CREATE INDEX idx_employer_history_event_type ON employer_history(event_type);
CREATE INDEX idx_employer_history_event_date ON employer_history(event_date);

-- ============================================
-- JOB POSTINGS
-- ============================================

CREATE TABLE job_postings (
    id BIGSERIAL PRIMARY KEY,
    employer_id BIGINT NOT NULL REFERENCES employers(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    responsibilities TEXT,
    location VARCHAR(255),
    field VARCHAR(100),
    experience_level VARCHAR(50),
    salary_min DECIMAL(12,2),
    salary_max DECIMAL(12,2),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- 'ACTIVE', 'CLOSED', 'FILLED'
    posted_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    closed_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_job_status CHECK (status IN ('ACTIVE', 'CLOSED', 'FILLED'))
);

CREATE INDEX idx_job_postings_employer_id ON job_postings(employer_id);
CREATE INDEX idx_job_postings_status ON job_postings(status);
CREATE INDEX idx_job_postings_field ON job_postings(field);
CREATE INDEX idx_job_postings_location ON job_postings(location);
CREATE INDEX idx_job_postings_posted_date ON job_postings(posted_date);

-- ============================================
-- JOB REQUIREMENTS
-- ============================================

CREATE TABLE job_requirements (
    id BIGSERIAL PRIMARY KEY,
    job_posting_id BIGINT NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    requirement_type VARCHAR(50) NOT NULL, -- 'HARD', 'SOFT', 'PREFERENCE'
    requirement_category VARCHAR(100), -- 'SKILL', 'EDUCATION', 'EXPERIENCE', etc.
    requirement_text TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_requirement_type CHECK (requirement_type IN ('HARD', 'SOFT', 'PREFERENCE'))
);

CREATE INDEX idx_job_requirements_job_posting_id ON job_requirements(job_posting_id);
CREATE INDEX idx_job_requirements_type ON job_requirements(requirement_type);

-- ============================================
-- SAVED JOBS
-- ============================================

CREATE TABLE saved_jobs (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    job_posting_id BIGINT NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    saved_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    UNIQUE(employee_id, job_posting_id)
);

CREATE INDEX idx_saved_jobs_employee_id ON saved_jobs(employee_id);
CREATE INDEX idx_saved_jobs_job_posting_id ON saved_jobs(job_posting_id);

-- ============================================
-- APPLICATIONS
-- ============================================

CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    job_posting_id BIGINT NOT NULL REFERENCES job_postings(id) ON DELETE CASCADE,
    cover_letter TEXT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'SUBMITTED', -- 'SUBMITTED', 'UNDER_REVIEW', 'INTERVIEW', 'ACCEPTED', 'REJECTED'
    applied_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP,
    response_deadline TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(employee_id, job_posting_id),
    CONSTRAINT chk_application_status CHECK (status IN ('SUBMITTED', 'UNDER_REVIEW', 'INTERVIEW', 'ACCEPTED', 'REJECTED'))
);

CREATE INDEX idx_applications_employee_id ON applications(employee_id);
CREATE INDEX idx_applications_job_posting_id ON applications(job_posting_id);
CREATE INDEX idx_applications_status ON applications(status);
CREATE INDEX idx_applications_applied_at ON applications(applied_at);

-- ============================================
-- APPLICATION MESSAGES
-- ============================================

CREATE TABLE application_messages (
    id BIGSERIAL PRIMARY KEY,
    application_id BIGINT NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    sender_type VARCHAR(50) NOT NULL, -- 'EMPLOYEE' or 'EMPLOYER'
    message_type VARCHAR(50) NOT NULL, -- 'MESSAGE', 'STATUS_UPDATE', 'REJECTION_JUSTIFICATION'
    message_text TEXT NOT NULL,
    sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP,
    CONSTRAINT chk_sender_type CHECK (sender_type IN ('EMPLOYEE', 'EMPLOYER')),
    CONSTRAINT chk_message_type CHECK (message_type IN ('MESSAGE', 'STATUS_UPDATE', 'REJECTION_JUSTIFICATION'))
);

CREATE INDEX idx_application_messages_application_id ON application_messages(application_id);
CREATE INDEX idx_application_messages_sent_at ON application_messages(sent_at);
CREATE INDEX idx_application_messages_read_at ON application_messages(read_at);

