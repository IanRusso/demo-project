package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserGeographicalInterestDao;
import com.irusso.demoserver.db.model.UserGeographicalInterest;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserGeographicalInterest operations.
 * Handles business logic and delegates to UserGeographicalInterestDao for data access.
 */
public class UserGeographicalInterestService {

    private final UserGeographicalInterestDao userGeographicalInterestDao;

    @Inject
    public UserGeographicalInterestService(UserGeographicalInterestDao userGeographicalInterestDao) {
        this.userGeographicalInterestDao = userGeographicalInterestDao;
    }

    /**
     * Get all user geographical interests.
     */
    public List<UserGeographicalInterest> getAllUserGeographicalInterests() {
        return userGeographicalInterestDao.findAll();
    }

    /**
     * Get a user geographical interest by ID.
     */
    public Optional<UserGeographicalInterest> getUserGeographicalInterestById(Long id) {
        return userGeographicalInterestDao.findById(id);
    }

    /**
     * Create a new user geographical interest.
     */
    public Long createUserGeographicalInterest(UserGeographicalInterest userGeographicalInterest) {
        return userGeographicalInterestDao.insert(userGeographicalInterest);
    }

    /**
     * Update an existing user geographical interest.
     */
    public boolean updateUserGeographicalInterest(UserGeographicalInterest userGeographicalInterest) {
        return userGeographicalInterestDao.update(userGeographicalInterest.getId(), userGeographicalInterest);
    }

    /**
     * Delete a user geographical interest by ID.
     */
    public boolean deleteUserGeographicalInterest(Long id) {
        return userGeographicalInterestDao.delete(id);
    }

    /**
     * Find geographical interests by user ID.
     */
    public List<UserGeographicalInterest> findByUserId(Long userId) {
        return userGeographicalInterestDao.findByUserId(userId);
    }

    /**
     * Find hard requirements by user ID.
     */
    public List<UserGeographicalInterest> findHardRequirementsByUserId(Long userId) {
        return userGeographicalInterestDao.findHardRequirementsByUserId(userId);
    }
}

