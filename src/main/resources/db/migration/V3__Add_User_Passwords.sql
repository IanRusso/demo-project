-- Add password support to users table
-- This migration adds password hashing and last login tracking

ALTER TABLE users
    ADD COLUMN password_hash VARCHAR(255),
    ADD COLUMN last_login_at TIMESTAMP;

-- Create index for faster lookups during authentication
CREATE INDEX idx_users_email_password ON users(email, password_hash);

-- Add comment for documentation
COMMENT ON COLUMN users.password_hash IS 'BCrypt hashed password (60 characters)';
COMMENT ON COLUMN users.last_login_at IS 'Timestamp of the user''s last successful login';

