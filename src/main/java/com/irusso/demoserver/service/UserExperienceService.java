package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.irusso.demoserver.db.dao.UserExperienceDao;
import com.irusso.demoserver.db.model.UserExperience;

import java.util.List;
import java.util.Optional;

/**
 * Service class for UserExperience business logic.
 * Handles operations related to user experiences and achievements.
 */
@Singleton
public class UserExperienceService {

    private final UserExperienceDao userExperienceDao;

    @Inject
    public UserExperienceService(UserExperienceDao userExperienceDao) {
        this.userExperienceDao = userExperienceDao;
    }

    /**
     * Get all user experiences.
     *
     * @return list of all user experiences
     */
    public List<UserExperience> getAllUserExperiences() {
        return userExperienceDao.findAll();
    }

    /**
     * Get a user experience by ID.
     *
     * @param id the user experience ID
     * @return optional containing the user experience if found
     */
    public Optional<UserExperience> getUserExperienceById(Long id) {
        return userExperienceDao.findById(id);
    }

    /**
     * Get all experiences for a specific user.
     *
     * @param userId the user ID
     * @return list of user experiences
     */
    public List<UserExperience> getUserExperiencesByUserId(Long userId) {
        return userExperienceDao.findByUserId(userId);
    }

    /**
     * Get experiences by type for a specific user.
     *
     * @param userId the user ID
     * @param experienceType the experience type
     * @return list of user experiences
     */
    public List<UserExperience> getUserExperiencesByUserIdAndType(Long userId, String experienceType) {
        return userExperienceDao.findByUserIdAndType(userId, experienceType);
    }

    /**
     * Get current experiences for a specific user.
     *
     * @param userId the user ID
     * @return list of current user experiences
     */
    public List<UserExperience> getCurrentUserExperiencesByUserId(Long userId) {
        return userExperienceDao.findCurrentByUserId(userId);
    }

    /**
     * Create a new user experience.
     *
     * @param userExperience the user experience to create
     * @return the ID of the created user experience
     */
    public Long createUserExperience(UserExperience userExperience) {
        return userExperienceDao.insert(userExperience);
    }

    /**
     * Update an existing user experience.
     *
     * @param id the user experience ID
     * @param userExperience the updated user experience data
     * @return true if the update was successful
     */
    public boolean updateUserExperience(Long id, UserExperience userExperience) {
        return userExperienceDao.update(id, userExperience);
    }

    /**
     * Delete a user experience.
     *
     * @param id the user experience ID
     * @return true if the deletion was successful
     */
    public boolean deleteUserExperience(Long id) {
        return userExperienceDao.delete(id);
    }
}

