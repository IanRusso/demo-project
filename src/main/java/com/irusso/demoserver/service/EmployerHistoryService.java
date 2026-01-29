package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.EmployerHistoryDao;
import com.irusso.demoserver.db.model.EmployerHistory;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for EmployerHistory operations.
 * Handles business logic and delegates to EmployerHistoryDao for data access.
 */
public class EmployerHistoryService {

    private final EmployerHistoryDao employerHistoryDao;

    @Inject
    public EmployerHistoryService(EmployerHistoryDao employerHistoryDao) {
        this.employerHistoryDao = employerHistoryDao;
    }

    /**
     * Get all employer history records.
     */
    public List<EmployerHistory> getAllEmployerHistory() {
        return employerHistoryDao.findAll();
    }

    /**
     * Get an employer history record by ID.
     */
    public Optional<EmployerHistory> getEmployerHistoryById(Long id) {
        return employerHistoryDao.findById(id);
    }

    /**
     * Create a new employer history record.
     */
    public Long createEmployerHistory(EmployerHistory employerHistory) {
        return employerHistoryDao.insert(employerHistory);
    }

    /**
     * Update an existing employer history record.
     */
    public boolean updateEmployerHistory(EmployerHistory employerHistory) {
        return employerHistoryDao.update(employerHistory.getId(), employerHistory);
    }

    /**
     * Delete an employer history record by ID.
     */
    public boolean deleteEmployerHistory(Long id) {
        return employerHistoryDao.delete(id);
    }

    /**
     * Find employer history by employer ID.
     */
    public List<EmployerHistory> findByEmployerId(Long employerId) {
        return employerHistoryDao.findByEmployerId(employerId);
    }

    /**
     * Find employer history by event type.
     */
    public List<EmployerHistory> findByEventType(Long employerId, String eventType) {
        return employerHistoryDao.findByEventType(employerId, eventType);
    }
}

