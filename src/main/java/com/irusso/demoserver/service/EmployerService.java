package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.EmployerDao;
import com.irusso.demoserver.db.model.Employer;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Employer operations.
 * Handles business logic and delegates to EmployerDao for data access.
 */
public class EmployerService {

    private final EmployerDao employerDao;

    @Inject
    public EmployerService(EmployerDao employerDao) {
        this.employerDao = employerDao;
    }

    /**
     * Get all employers.
     */
    public List<Employer> getAllEmployers() {
        return employerDao.findAll();
    }

    /**
     * Get an employer by ID.
     */
    public Optional<Employer> getEmployerById(Long id) {
        return employerDao.findById(id);
    }

    /**
     * Create a new employer.
     */
    public Long createEmployer(Employer employer) {
        return employerDao.insert(employer);
    }

    /**
     * Update an existing employer.
     */
    public boolean updateEmployer(Employer employer) {
        return employerDao.update(employer.getId(), employer);
    }

    /**
     * Delete an employer by ID.
     */
    public boolean deleteEmployer(Long id) {
        return employerDao.delete(id);
    }
}

