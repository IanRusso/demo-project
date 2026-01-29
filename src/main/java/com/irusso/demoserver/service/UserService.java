package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserDao;
import com.irusso.demoserver.db.model.User;

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
     * Create a new user.
     */
    public Long createUser(User user) {
        return userDao.insert(user);
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

