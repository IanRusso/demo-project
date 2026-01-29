package com.irusso.demoserver.security;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 * BCrypt is a password hashing function designed to be slow and resistant to brute-force attacks.
 */
public class PasswordUtil {

    // BCrypt work factor (log2 rounds). 12 is a good balance between security and performance.
    // Each increment doubles the time required to hash a password.
    private static final int BCRYPT_ROUNDS = 12;

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private PasswordUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Hash a plain text password using BCrypt.
     * 
     * @param plainTextPassword the plain text password to hash
     * @return the BCrypt hashed password (60 characters)
     * @throws IllegalArgumentException if password is null or empty
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(BCRYPT_ROUNDS));
    }

    /**
     * Verify a plain text password against a BCrypt hash.
     * 
     * @param plainTextPassword the plain text password to verify
     * @param hashedPassword the BCrypt hashed password to check against
     * @return true if the password matches the hash, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }

    /**
     * Check if a password meets minimum security requirements.
     * 
     * @param password the password to validate
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        
        // Minimum 8 characters
        if (password.length() < 8) {
            return false;
        }
        
        // Maximum 72 characters (BCrypt limitation)
        if (password.length() > 72) {
            return false;
        }
        
        return true;
    }

    /**
     * Get a description of password requirements.
     * 
     * @return string describing password requirements
     */
    public static String getPasswordRequirements() {
        return "Password must be between 8 and 72 characters";
    }
}

