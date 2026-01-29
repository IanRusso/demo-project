package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserSkillDao;
import com.irusso.demoserver.db.model.UserSkill;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for UserSkill operations.
 * Handles business logic and delegates to UserSkillDao for data access.
 */
public class UserSkillService {

    private final UserSkillDao userSkillDao;

    @Inject
    public UserSkillService(UserSkillDao userSkillDao) {
        this.userSkillDao = userSkillDao;
    }

    /**
     * Get all user skills.
     */
    public List<UserSkill> getAllUserSkills() {
        return userSkillDao.findAll();
    }

    /**
     * Get a user skill by ID.
     */
    public Optional<UserSkill> getUserSkillById(Long id) {
        return userSkillDao.findById(id);
    }

    /**
     * Create a new user skill.
     */
    public Long createUserSkill(UserSkill userSkill) {
        return userSkillDao.insert(userSkill);
    }

    /**
     * Update an existing user skill.
     */
    public boolean updateUserSkill(UserSkill userSkill) {
        return userSkillDao.update(userSkill.getId(), userSkill);
    }

    /**
     * Delete a user skill by ID.
     */
    public boolean deleteUserSkill(Long id) {
        return userSkillDao.delete(id);
    }

    /**
     * Find skills by user ID.
     */
    public List<UserSkill> findByUserId(Long userId) {
        return userSkillDao.findByUserId(userId);
    }

    /**
     * Find skills by skill name.
     */
    public List<UserSkill> findBySkillName(String skillName) {
        return userSkillDao.findBySkillName(skillName);
    }
}

