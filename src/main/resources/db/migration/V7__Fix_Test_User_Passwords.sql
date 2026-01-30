-- Fix password hashes for test users
-- The correct BCrypt hash for "password" with 12 rounds

UPDATE users
SET password_hash = '$2a$12$8EguFSP0Ln6Ng6IUFM5VMuPCNSYJdMPNe6f2rsaN/VuYBHeWYv/C2',
    updated_at = CURRENT_TIMESTAMP
WHERE email LIKE '%@example.com';

