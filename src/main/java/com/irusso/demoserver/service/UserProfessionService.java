package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserProfessionDao;
import com.irusso.demoserver.db.model.UserProfession;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserProfession operations.
 * Handles business logic and delegates to UserProfessionDao for data access.
 */
public class UserProfessionService {

    private final UserProfessionDao userProfessionDao;

    @Inject
    public UserProfessionService(UserProfessionDao userProfessionDao) {
        this.userProfessionDao = userProfessionDao;
    }

    /**
     * Get all user professions.
     */
    public List<UserProfession> getAllUserProfessions() {
        return userProfessionDao.findAll();
    }

    /**
     * Get a user profession by ID.
     */
    public Optional<UserProfession> getUserProfessionById(Long id) {
        return userProfessionDao.findById(id);
    }

    /**
     * Create a new user profession.
     */
    public Long createUserProfession(UserProfession userProfession) {
        return userProfessionDao.insert(userProfession);
    }

    /**
     * Update an existing user profession.
     */
    public boolean updateUserProfession(UserProfession userProfession) {
        return userProfessionDao.update(userProfession.getId(), userProfession);
    }

    /**
     * Delete a user profession by ID.
     */
    public boolean deleteUserProfession(Long id) {
        return userProfessionDao.delete(id);
    }

    /**
     * Find professions by user ID.
     */
    public List<UserProfession> findByUserId(Long userId) {
        return userProfessionDao.findByUserId(userId);
    }

    /**
     * Find current professions by user ID.
     */
    public List<UserProfession> findCurrentByUserId(Long userId) {
        return userProfessionDao.findCurrentByUserId(userId);
    }

    /**
     * Find users by profession ID.
     */
    public List<UserProfession> findByProfessionId(Long professionId) {
        return userProfessionDao.findByProfessionId(professionId);
    }
}

