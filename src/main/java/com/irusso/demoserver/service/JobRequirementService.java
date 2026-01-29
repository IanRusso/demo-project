package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.JobRequirementDao;
import com.irusso.demoserver.db.model.JobRequirement;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for JobRequirement operations.
 * Handles business logic and delegates to JobRequirementDao for data access.
 */
public class JobRequirementService {

    private final JobRequirementDao jobRequirementDao;

    @Inject
    public JobRequirementService(JobRequirementDao jobRequirementDao) {
        this.jobRequirementDao = jobRequirementDao;
    }

    /**
     * Get all job requirements.
     */
    public List<JobRequirement> getAllJobRequirements() {
        return jobRequirementDao.findAll();
    }

    /**
     * Get a job requirement by ID.
     */
    public Optional<JobRequirement> getJobRequirementById(Long id) {
        return jobRequirementDao.findById(id);
    }

    /**
     * Create a new job requirement.
     */
    public Long createJobRequirement(JobRequirement jobRequirement) {
        return jobRequirementDao.insert(jobRequirement);
    }

    /**
     * Update an existing job requirement.
     */
    public boolean updateJobRequirement(JobRequirement jobRequirement) {
        return jobRequirementDao.update(jobRequirement.getId(), jobRequirement);
    }

    /**
     * Delete a job requirement by ID.
     */
    public boolean deleteJobRequirement(Long id) {
        return jobRequirementDao.delete(id);
    }

    /**
     * Find job requirements by job posting ID.
     */
    public List<JobRequirement> findByJobPostingId(Long jobPostingId) {
        return jobRequirementDao.findByJobPostingId(jobPostingId);
    }
}

