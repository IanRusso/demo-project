-- Gainfully Job Site - Test Data Migration
-- This migration adds comprehensive test data for all entities
-- Password for all users: "password" (BCrypt hash with 12 rounds)
-- Hash: $2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2

-- ============================================
-- CLEAN UP EXISTING TEST DATA (if any)
-- ============================================
-- Truncate all tables to start fresh (CASCADE will handle foreign key dependencies)

TRUNCATE TABLE application_messages, applications, saved_jobs, job_requirements, job_postings,
                employer_history, employers, user_experiences, employment_history,
                user_job_type_interests, user_geographical_interests, user_fields_of_interest,
                user_skills, users
RESTART IDENTITY CASCADE;

-- ============================================
-- USERS
-- ============================================

INSERT INTO users (name, email, phone_number, location, education_level, summary, employment_status, 
                   user_rating, communication_rating, salary_expectations_min, salary_expectations_max, 
                   actively_seeking, password_hash, created_at, updated_at)
VALUES 
    ('Ian Russo', 'ian.russo@example.com', '555-0101', 'San Francisco, CA, USA', 'Bachelor''s Degree',
     'Experienced software engineer with a passion for building scalable systems and mentoring junior developers.',
     'EMPLOYED', 4.8, 4.9, 120000, 160000, true,
     '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Sarah Chen', 'sarah.chen@example.com', '555-0102', 'New York, NY, USA', 'Master''s Degree',
     'Product manager with 8 years of experience in tech startups. Specialized in B2B SaaS products.',
     'EMPLOYED', 4.7, 4.8, 130000, 170000, false,
     '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Marcus Johnson', 'marcus.j@example.com', '555-0103', 'Austin, TX, USA', 'Bachelor''s Degree',
     'Full-stack developer specializing in React and Node.js. Love building user-friendly applications.',
     'ACTIVELY_SEEKING', 4.5, 4.6, 90000, 120000, true,
     '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('Emily Rodriguez', 'emily.r@example.com', '555-0104', 'Seattle, WA, USA', 'PhD',
     'Data scientist with expertise in machine learning and natural language processing.',
     'EMPLOYED', 4.9, 4.9, 140000, 180000, true,
     '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

    ('David Kim', 'david.kim@example.com', '555-0105', 'Boston, MA, USA', 'Bachelor''s Degree',
     'DevOps engineer passionate about automation and cloud infrastructure.',
     'ACTIVELY_SEEKING', 4.6, 4.7, 110000, 145000, true,
     '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- USER SKILLS
-- ============================================

INSERT INTO user_skills (user_id, skill_name, proficiency_level, years_of_experience, created_at)
VALUES 
    -- Ian Russo's skills
    (1, 'Java', 'Expert', 8, CURRENT_TIMESTAMP),
    (1, 'Python', 'Advanced', 6, CURRENT_TIMESTAMP),
    (1, 'PostgreSQL', 'Advanced', 7, CURRENT_TIMESTAMP),
    (1, 'React', 'Intermediate', 4, CURRENT_TIMESTAMP),
    (1, 'AWS', 'Advanced', 5, CURRENT_TIMESTAMP),
    
    -- Sarah Chen's skills
    (2, 'Product Management', 'Expert', 8, CURRENT_TIMESTAMP),
    (2, 'Agile/Scrum', 'Expert', 8, CURRENT_TIMESTAMP),
    (2, 'User Research', 'Advanced', 6, CURRENT_TIMESTAMP),
    (2, 'Data Analysis', 'Advanced', 5, CURRENT_TIMESTAMP),
    
    -- Marcus Johnson's skills
    (3, 'JavaScript', 'Expert', 5, CURRENT_TIMESTAMP),
    (3, 'React', 'Expert', 5, CURRENT_TIMESTAMP),
    (3, 'Node.js', 'Advanced', 4, CURRENT_TIMESTAMP),
    (3, 'TypeScript', 'Advanced', 3, CURRENT_TIMESTAMP),
    (3, 'MongoDB', 'Intermediate', 3, CURRENT_TIMESTAMP),
    
    -- Emily Rodriguez's skills
    (4, 'Python', 'Expert', 10, CURRENT_TIMESTAMP),
    (4, 'Machine Learning', 'Expert', 7, CURRENT_TIMESTAMP),
    (4, 'TensorFlow', 'Advanced', 5, CURRENT_TIMESTAMP),
    (4, 'PyTorch', 'Advanced', 4, CURRENT_TIMESTAMP),
    (4, 'SQL', 'Advanced', 8, CURRENT_TIMESTAMP),
    
    -- David Kim's skills
    (5, 'Docker', 'Expert', 6, CURRENT_TIMESTAMP),
    (5, 'Kubernetes', 'Advanced', 4, CURRENT_TIMESTAMP),
    (5, 'AWS', 'Expert', 7, CURRENT_TIMESTAMP),
    (5, 'Terraform', 'Advanced', 3, CURRENT_TIMESTAMP),
    (5, 'CI/CD', 'Expert', 6, CURRENT_TIMESTAMP);

-- ============================================
-- USER FIELDS OF INTEREST
-- ============================================

INSERT INTO user_fields_of_interest (user_id, field_name, is_hard_requirement, created_at)
VALUES 
    (1, 'Software Engineering', true, CURRENT_TIMESTAMP),
    (1, 'Cloud Computing', false, CURRENT_TIMESTAMP),
    (1, 'Artificial Intelligence', false, CURRENT_TIMESTAMP),
    (2, 'Product Management', true, CURRENT_TIMESTAMP),
    (2, 'SaaS', true, CURRENT_TIMESTAMP),
    (3, 'Web Development', true, CURRENT_TIMESTAMP),
    (3, 'Mobile Development', false, CURRENT_TIMESTAMP),
    (4, 'Data Science', true, CURRENT_TIMESTAMP),
    (4, 'Machine Learning', true, CURRENT_TIMESTAMP),
    (4, 'Research', false, CURRENT_TIMESTAMP),
    (5, 'DevOps', true, CURRENT_TIMESTAMP),
    (5, 'Cloud Infrastructure', true, CURRENT_TIMESTAMP);

-- ============================================
-- USER GEOGRAPHICAL INTERESTS
-- ============================================

INSERT INTO user_geographical_interests (user_id, location, is_hard_requirement, created_at)
VALUES 
    (1, 'San Francisco, CA, USA', false, CURRENT_TIMESTAMP),
    (1, 'Remote', true, CURRENT_TIMESTAMP),
    (2, 'New York, NY, USA', true, CURRENT_TIMESTAMP),
    (3, 'Austin, TX, USA', false, CURRENT_TIMESTAMP),
    (3, 'Remote', true, CURRENT_TIMESTAMP),
    (4, 'Seattle, WA, USA', false, CURRENT_TIMESTAMP),
    (4, 'Remote', false, CURRENT_TIMESTAMP),
    (5, 'Boston, MA, USA', false, CURRENT_TIMESTAMP),
    (5, 'Remote', true, CURRENT_TIMESTAMP);

-- ============================================
-- USER JOB TYPE INTERESTS
-- ============================================

INSERT INTO user_job_type_interests (user_id, job_type, is_hard_requirement, created_at)
VALUES 
    (1, 'Full-time', true, CURRENT_TIMESTAMP),
    (1, 'Contract', false, CURRENT_TIMESTAMP),
    (2, 'Full-time', true, CURRENT_TIMESTAMP),
    (3, 'Full-time', true, CURRENT_TIMESTAMP),
    (3, 'Part-time', false, CURRENT_TIMESTAMP),
    (4, 'Full-time', true, CURRENT_TIMESTAMP),
    (5, 'Full-time', true, CURRENT_TIMESTAMP),
    (5, 'Contract', false, CURRENT_TIMESTAMP);

-- ============================================
-- EMPLOYMENT HISTORY
-- ============================================

INSERT INTO employment_history (user_id, employer_name, job_title, location, start_date, end_date, is_current, description, created_at, updated_at)
VALUES 
    -- Ian Russo's history
    (1, 'TechCorp Inc', 'Senior Software Engineer', 'San Francisco, CA', '2020-03-01', NULL, true,
     'Leading development of microservices architecture. Mentoring junior developers and conducting code reviews.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (1, 'StartupXYZ', 'Software Engineer', 'San Francisco, CA', '2017-06-01', '2020-02-28', false,
     'Built scalable backend services using Java and PostgreSQL. Implemented CI/CD pipelines.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Sarah Chen's history
    (2, 'CloudSoft Solutions', 'Senior Product Manager', 'New York, NY', '2019-01-01', NULL, true,
     'Managing product roadmap for enterprise SaaS platform. Leading cross-functional teams of 15+ people.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'InnovateTech', 'Product Manager', 'New York, NY', '2016-04-01', '2018-12-31', false,
     'Launched 3 major product features that increased user engagement by 40%.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Marcus Johnson's history
    (3, 'WebDev Agency', 'Full Stack Developer', 'Austin, TX', '2019-08-01', '2024-01-15', false,
     'Developed custom web applications for clients using React, Node.js, and MongoDB.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- Emily Rodriguez's history
    (4, 'AI Research Lab', 'Senior Data Scientist', 'Seattle, WA', '2021-02-01', NULL, true,
     'Leading research on NLP models. Published 5 papers in top-tier conferences.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'DataCorp', 'Data Scientist', 'Seattle, WA', '2018-05-01', '2021-01-31', false,
     'Built predictive models for customer churn and recommendation systems.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    -- David Kim's history
    (5, 'CloudOps Inc', 'DevOps Engineer', 'Boston, MA', '2018-09-01', '2024-02-01', false,
     'Managed AWS infrastructure for high-traffic applications. Implemented Kubernetes clusters.',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- USER EXPERIENCES
-- ============================================

INSERT INTO user_experiences (user_id, title, description, experience_type, start_date, end_date, is_current, created_at, updated_at)
VALUES 
    (1, 'Led Migration to Microservices', 
     'Successfully led team of 5 engineers to migrate monolithic application to microservices architecture, reducing deployment time by 70%.',
     'LEADERSHIP', '2021-01-01', '2022-06-30', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    (2, 'Product Launch: Enterprise Dashboard',
     'Led cross-functional team to launch enterprise analytics dashboard, resulting in $2M ARR in first year.',
     'PROJECT', '2020-03-01', '2021-02-28', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    (3, 'Open Source Contribution',
     'Core contributor to popular React component library with 10k+ GitHub stars.',
     'TECHNICAL', '2020-01-01', NULL, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    (4, 'Published Research Paper',
     'First author on paper about transformer models published at NeurIPS 2023.',
     'ACHIEVEMENT', '2022-06-01', '2023-12-01', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    (5, 'Cost Optimization Initiative',
     'Reduced AWS infrastructure costs by 45% through optimization and right-sizing.',
     'ACHIEVEMENT', '2022-01-01', '2022-12-31', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================
-- EMPLOYERS (Companies)
-- ============================================

INSERT INTO employers (name, email, phone_number, location, company_size, company_type, description, 
                       employer_rating, communication_rating, created_at, updated_at)
VALUES 
    ('Acme Corporation', 'jobs@acmecorp.com', '555-1001', 'San Francisco, CA, USA', '1000-5000', 'Technology',
     'Leading provider of cloud-based enterprise solutions. We help businesses transform digitally.',
     4.5, 4.6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('InnovateLabs', 'careers@innovatelabs.com', '555-1002', 'New York, NY, USA', '100-500', 'Startup',
     'Fast-growing AI startup focused on natural language processing and machine learning.',
     4.7, 4.8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('Global Tech Solutions', 'hr@globaltech.com', '555-1003', 'Austin, TX, USA', '5000+', 'Enterprise',
     'Fortune 500 company providing IT consulting and software development services worldwide.',
     4.3, 4.4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('StartupHub', 'jobs@startuphub.com', '555-1004', 'Seattle, WA, USA', '50-100', 'Startup',
     'Early-stage startup building the next generation of collaboration tools.',
     4.6, 4.7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    
    ('CloudFirst Inc', 'recruiting@cloudfirst.com', '555-1005', 'Boston, MA, USA', '500-1000', 'Technology',
     'Cloud infrastructure company helping businesses migrate to and optimize cloud environments.',
     4.4, 4.5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

