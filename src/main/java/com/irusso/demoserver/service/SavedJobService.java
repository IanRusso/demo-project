package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.SavedJobDao;
import com.irusso.demoserver.db.model.SavedJob;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for SavedJob operations.
 * Handles business logic and delegates to SavedJobDao for data access.
 */
public class SavedJobService {

    private final SavedJobDao savedJobDao;

    @Inject
    public SavedJobService(SavedJobDao savedJobDao) {
        this.savedJobDao = savedJobDao;
    }

    /**
     * Get all saved jobs.
     */
    public List<SavedJob> getAllSavedJobs() {
        return savedJobDao.findAll();
    }

    /**
     * Get a saved job by ID.
     */
    public Optional<SavedJob> getSavedJobById(Long id) {
        return savedJobDao.findById(id);
    }

    /**
     * Create a new saved job.
     */
    public Long createSavedJob(SavedJob savedJob) {
        return savedJobDao.insert(savedJob);
    }

    /**
     * Update an existing saved job.
     */
    public boolean updateSavedJob(SavedJob savedJob) {
        return savedJobDao.update(savedJob.getId(), savedJob);
    }

    /**
     * Delete a saved job by ID.
     */
    public boolean deleteSavedJob(Long id) {
        return savedJobDao.delete(id);
    }

    /**
     * Find saved jobs by user ID.
     */
    public List<SavedJob> findByUserId(Long userId) {
        return savedJobDao.findByUserId(userId);
    }
}

