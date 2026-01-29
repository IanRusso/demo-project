package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserDao;
import com.irusso.demoserver.db.model.User;
import com.irusso.demoserver.security.PasswordUtil;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for User operations.
 * Handles business logic and delegates to UserDao for data access.
 */
public class UserService {

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Get all users.
     */
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    /**
     * Get a user by ID.
     */
    public Optional<User> getUserById(Long id) {
        return userDao.findById(id);
    }

    /**
     * Get a user by email.
     */
    public Optional<User> getUserByEmail(String email) {
        List<User> users = userDao.findByEmail(email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    /**
     * Create a new user.
     */
    public Long createUser(User user) {
        return userDao.insert(user);
    }

    /**
     * Register a new user with a password.
     *
     * @param user the user to register
     * @param plainTextPassword the plain text password
     * @return the ID of the created user
     * @throws IllegalArgumentException if password is invalid or email already exists
     */
    public Long registerUser(User user, String plainTextPassword) {
        // Validate password
        if (!PasswordUtil.isPasswordValid(plainTextPassword)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        // Check if email already exists
        if (getUserByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Hash the password
        String passwordHash = PasswordUtil.hashPassword(plainTextPassword);
        user.setPasswordHash(passwordHash);

        // Create the user
        return userDao.insert(user);
    }

    /**
     * Authenticate a user with email and password.
     *
     * @param email the user's email
     * @param plainTextPassword the plain text password
     * @return Optional containing the user if authentication successful
     */
    public Optional<User> authenticateUser(String email, String plainTextPassword) {
        Optional<User> userOpt = getUserByEmail(email);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User user = userOpt.get();

        // Check if user has a password set
        if (user.getPasswordHash() == null) {
            return Optional.empty();
        }

        // Verify password
        if (!PasswordUtil.verifyPassword(plainTextPassword, user.getPasswordHash())) {
            return Optional.empty();
        }

        // Update last login timestamp
        userDao.updateLastLogin(user.getId());

        return Optional.of(user);
    }

    /**
     * Change a user's password.
     *
     * @param userId the user ID
     * @param currentPassword the current password
     * @param newPassword the new password
     * @return true if password was changed successfully
     * @throws IllegalArgumentException if new password is invalid
     */
    public boolean changePassword(Long userId, String currentPassword, String newPassword) {
        // Validate new password
        if (!PasswordUtil.isPasswordValid(newPassword)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        // Get user
        Optional<User> userOpt = getUserById(userId);
        if (userOpt.isEmpty()) {
            return false;
        }

        User user = userOpt.get();

        // Verify current password
        if (user.getPasswordHash() == null ||
            !PasswordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            return false;
        }

        // Hash and update new password
        String newPasswordHash = PasswordUtil.hashPassword(newPassword);
        return userDao.updatePasswordHash(userId, newPasswordHash);
    }

    /**
     * Set or reset a user's password (admin function).
     *
     * @param userId the user ID
     * @param newPassword the new password
     * @return true if password was set successfully
     * @throws IllegalArgumentException if new password is invalid
     */
    public boolean setPassword(Long userId, String newPassword) {
        // Validate new password
        if (!PasswordUtil.isPasswordValid(newPassword)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        // Hash and update password
        String newPasswordHash = PasswordUtil.hashPassword(newPassword);
        return userDao.updatePasswordHash(userId, newPasswordHash);
    }

    /**
     * Update an existing user.
     */
    public boolean updateUser(User user) {
        return userDao.update(user.getId(), user);
    }

    /**
     * Delete a user by ID.
     */
    public boolean deleteUser(Long id) {
        return userDao.delete(id);
    }
}

