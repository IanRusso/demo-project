package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserJobTypeInterestDao;
import com.irusso.demoserver.db.model.UserJobTypeInterest;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserJobTypeInterest operations.
 * Handles business logic and delegates to UserJobTypeInterestDao for data access.
 */
public class UserJobTypeInterestService {

    private final UserJobTypeInterestDao userJobTypeInterestDao;

    @Inject
    public UserJobTypeInterestService(UserJobTypeInterestDao userJobTypeInterestDao) {
        this.userJobTypeInterestDao = userJobTypeInterestDao;
    }

    /**
     * Get all user job type interests.
     */
    public List<UserJobTypeInterest> getAllUserJobTypeInterests() {
        return userJobTypeInterestDao.findAll();
    }

    /**
     * Get a user job type interest by ID.
     */
    public Optional<UserJobTypeInterest> getUserJobTypeInterestById(Long id) {
        return userJobTypeInterestDao.findById(id);
    }

    /**
     * Create a new user job type interest.
     */
    public Long createUserJobTypeInterest(UserJobTypeInterest userJobTypeInterest) {
        return userJobTypeInterestDao.insert(userJobTypeInterest);
    }

    /**
     * Update an existing user job type interest.
     */
    public boolean updateUserJobTypeInterest(UserJobTypeInterest userJobTypeInterest) {
        return userJobTypeInterestDao.update(userJobTypeInterest.getId(), userJobTypeInterest);
    }

    /**
     * Delete a user job type interest by ID.
     */
    public boolean deleteUserJobTypeInterest(Long id) {
        return userJobTypeInterestDao.delete(id);
    }

    /**
     * Find job type interests by user ID.
     */
    public List<UserJobTypeInterest> findByUserId(Long userId) {
        return userJobTypeInterestDao.findByUserId(userId);
    }

    /**
     * Find hard requirements by user ID.
     */
    public List<UserJobTypeInterest> findHardRequirementsByUserId(Long userId) {
        return userJobTypeInterestDao.findHardRequirementsByUserId(userId);
    }
}

