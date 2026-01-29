package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.JobPostingDao;
import com.irusso.demoserver.db.model.JobPosting;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for JobPosting operations.
 * Handles business logic and delegates to JobPostingDao for data access.
 */
public class JobPostingService {

    private final JobPostingDao jobPostingDao;

    @Inject
    public JobPostingService(JobPostingDao jobPostingDao) {
        this.jobPostingDao = jobPostingDao;
    }

    /**
     * Get all job postings.
     */
    public List<JobPosting> getAllJobPostings() {
        return jobPostingDao.findAll();
    }

    /**
     * Get a job posting by ID.
     */
    public Optional<JobPosting> getJobPostingById(Long id) {
        return jobPostingDao.findById(id);
    }

    /**
     * Create a new job posting.
     */
    public Long createJobPosting(JobPosting jobPosting) {
        return jobPostingDao.insert(jobPosting);
    }

    /**
     * Update an existing job posting.
     */
    public boolean updateJobPosting(JobPosting jobPosting) {
        return jobPostingDao.update(jobPosting.getId(), jobPosting);
    }

    /**
     * Delete a job posting by ID.
     */
    public boolean deleteJobPosting(Long id) {
        return jobPostingDao.delete(id);
    }

    /**
     * Find job postings by employer ID.
     */
    public List<JobPosting> findByEmployerId(Long employerId) {
        return jobPostingDao.findByEmployerId(employerId);
    }

    /**
     * Find job postings by status.
     */
    public List<JobPosting> findByStatus(String status) {
        return jobPostingDao.findByStatus(status);
    }

    /**
     * Find job postings by field.
     */
    public List<JobPosting> findByField(String field) {
        return jobPostingDao.findByField(field);
    }

    /**
     * Find job postings by location.
     */
    public List<JobPosting> findByLocation(String location) {
        return jobPostingDao.findByLocation(location);
    }
}

