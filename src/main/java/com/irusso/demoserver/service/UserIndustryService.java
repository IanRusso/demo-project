package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserIndustryDao;
import com.irusso.demoserver.db.model.UserIndustry;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserIndustry operations.
 * Handles business logic and delegates to UserIndustryDao for data access.
 */
public class UserIndustryService {

    private final UserIndustryDao userIndustryDao;

    @Inject
    public UserIndustryService(UserIndustryDao userIndustryDao) {
        this.userIndustryDao = userIndustryDao;
    }

    /**
     * Get all user industries.
     */
    public List<UserIndustry> getAllUserIndustries() {
        return userIndustryDao.findAll();
    }

    /**
     * Get a user industry by ID.
     */
    public Optional<UserIndustry> getUserIndustryById(Long id) {
        return userIndustryDao.findById(id);
    }

    /**
     * Create a new user industry.
     */
    public Long createUserIndustry(UserIndustry userIndustry) {
        return userIndustryDao.insert(userIndustry);
    }

    /**
     * Update an existing user industry.
     */
    public boolean updateUserIndustry(UserIndustry userIndustry) {
        return userIndustryDao.update(userIndustry.getId(), userIndustry);
    }

    /**
     * Delete a user industry by ID.
     */
    public boolean deleteUserIndustry(Long id) {
        return userIndustryDao.delete(id);
    }

    /**
     * Find industries by user ID.
     */
    public List<UserIndustry> findByUserId(Long userId) {
        return userIndustryDao.findByUserId(userId);
    }

    /**
     * Find current industries by user ID.
     */
    public List<UserIndustry> findCurrentByUserId(Long userId) {
        return userIndustryDao.findCurrentByUserId(userId);
    }

    /**
     * Find users by industry ID.
     */
    public List<UserIndustry> findByIndustryId(Long industryId) {
        return userIndustryDao.findByIndustryId(industryId);
    }
}

