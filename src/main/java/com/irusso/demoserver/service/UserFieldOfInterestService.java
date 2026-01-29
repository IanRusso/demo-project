package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserFieldOfInterestDao;
import com.irusso.demoserver.db.model.UserFieldOfInterest;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserFieldOfInterest operations.
 * Handles business logic and delegates to UserFieldOfInterestDao for data access.
 */
public class UserFieldOfInterestService {

    private final UserFieldOfInterestDao userFieldOfInterestDao;

    @Inject
    public UserFieldOfInterestService(UserFieldOfInterestDao userFieldOfInterestDao) {
        this.userFieldOfInterestDao = userFieldOfInterestDao;
    }

    /**
     * Get all user fields of interest.
     */
    public List<UserFieldOfInterest> getAllUserFieldsOfInterest() {
        return userFieldOfInterestDao.findAll();
    }

    /**
     * Get a user field of interest by ID.
     */
    public Optional<UserFieldOfInterest> getUserFieldOfInterestById(Long id) {
        return userFieldOfInterestDao.findById(id);
    }

    /**
     * Create a new user field of interest.
     */
    public Long createUserFieldOfInterest(UserFieldOfInterest userFieldOfInterest) {
        return userFieldOfInterestDao.insert(userFieldOfInterest);
    }

    /**
     * Update an existing user field of interest.
     */
    public boolean updateUserFieldOfInterest(UserFieldOfInterest userFieldOfInterest) {
        return userFieldOfInterestDao.update(userFieldOfInterest.getId(), userFieldOfInterest);
    }

    /**
     * Delete a user field of interest by ID.
     */
    public boolean deleteUserFieldOfInterest(Long id) {
        return userFieldOfInterestDao.delete(id);
    }

    /**
     * Find fields of interest by user ID.
     */
    public List<UserFieldOfInterest> findByUserId(Long userId) {
        return userFieldOfInterestDao.findByUserId(userId);
    }

    /**
     * Find hard requirements by user ID.
     */
    public List<UserFieldOfInterest> findHardRequirementsByUserId(Long userId) {
        return userFieldOfInterestDao.findHardRequirementsByUserId(userId);
    }
}

