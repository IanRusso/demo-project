package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.EmploymentHistoryDao;
import com.irusso.demoserver.db.model.EmploymentHistory;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for EmploymentHistory operations.
 * Handles business logic and delegates to EmploymentHistoryDao for data access.
 */
public class EmploymentHistoryService {

    private final EmploymentHistoryDao employmentHistoryDao;

    @Inject
    public EmploymentHistoryService(EmploymentHistoryDao employmentHistoryDao) {
        this.employmentHistoryDao = employmentHistoryDao;
    }

    /**
     * Get all employment history records.
     */
    public List<EmploymentHistory> getAllEmploymentHistory() {
        return employmentHistoryDao.findAll();
    }

    /**
     * Get an employment history record by ID.
     */
    public Optional<EmploymentHistory> getEmploymentHistoryById(Long id) {
        return employmentHistoryDao.findById(id);
    }

    /**
     * Create a new employment history record.
     */
    public Long createEmploymentHistory(EmploymentHistory employmentHistory) {
        return employmentHistoryDao.insert(employmentHistory);
    }

    /**
     * Update an existing employment history record.
     */
    public boolean updateEmploymentHistory(EmploymentHistory employmentHistory) {
        return employmentHistoryDao.update(employmentHistory.getId(), employmentHistory);
    }

    /**
     * Delete an employment history record by ID.
     */
    public boolean deleteEmploymentHistory(Long id) {
        return employmentHistoryDao.delete(id);
    }

    /**
     * Find employment history by user ID.
     */
    public List<EmploymentHistory> findByUserId(Long userId) {
        return employmentHistoryDao.findByUserId(userId);
    }
}

