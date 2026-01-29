package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.ApplicationDao;
import com.irusso.demoserver.db.model.Application;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Application operations.
 * Handles business logic and delegates to ApplicationDao for data access.
 */
public class ApplicationService {

    private final ApplicationDao applicationDao;

    @Inject
    public ApplicationService(ApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    /**
     * Get all applications.
     */
    public List<Application> getAllApplications() {
        return applicationDao.findAll();
    }

    /**
     * Get an application by ID.
     */
    public Optional<Application> getApplicationById(Long id) {
        return applicationDao.findById(id);
    }

    /**
     * Create a new application.
     */
    public Long createApplication(Application application) {
        return applicationDao.insert(application);
    }

    /**
     * Update an existing application.
     */
    public boolean updateApplication(Application application) {
        return applicationDao.update(application.getId(), application);
    }

    /**
     * Delete an application by ID.
     */
    public boolean deleteApplication(Long id) {
        return applicationDao.delete(id);
    }

    /**
     * Find applications by user ID.
     */
    public List<Application> findByUserId(Long userId) {
        return applicationDao.findByUserId(userId);
    }

    /**
     * Find applications by job posting ID.
     */
    public List<Application> findByJobPostingId(Long jobPostingId) {
        return applicationDao.findByJobPostingId(jobPostingId);
    }

    /**
     * Find applications by status.
     */
    public List<Application> findByStatus(String status) {
        return applicationDao.findByStatus(status);
    }
}

