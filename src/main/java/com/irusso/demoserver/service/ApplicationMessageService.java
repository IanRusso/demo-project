package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.ApplicationMessageDao;
import com.irusso.demoserver.db.model.ApplicationMessage;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for ApplicationMessage operations.
 * Handles business logic and delegates to ApplicationMessageDao for data access.
 */
public class ApplicationMessageService {

    private final ApplicationMessageDao applicationMessageDao;

    @Inject
    public ApplicationMessageService(ApplicationMessageDao applicationMessageDao) {
        this.applicationMessageDao = applicationMessageDao;
    }

    /**
     * Get all application messages.
     */
    public List<ApplicationMessage> getAllApplicationMessages() {
        return applicationMessageDao.findAll();
    }

    /**
     * Get an application message by ID.
     */
    public Optional<ApplicationMessage> getApplicationMessageById(Long id) {
        return applicationMessageDao.findById(id);
    }

    /**
     * Create a new application message.
     */
    public Long createApplicationMessage(ApplicationMessage applicationMessage) {
        return applicationMessageDao.insert(applicationMessage);
    }

    /**
     * Update an existing application message.
     */
    public boolean updateApplicationMessage(ApplicationMessage applicationMessage) {
        return applicationMessageDao.update(applicationMessage.getId(), applicationMessage);
    }

    /**
     * Delete an application message by ID.
     */
    public boolean deleteApplicationMessage(Long id) {
        return applicationMessageDao.delete(id);
    }

    /**
     * Find messages by application ID.
     */
    public List<ApplicationMessage> findByApplicationId(Long applicationId) {
        return applicationMessageDao.findByApplicationId(applicationId);
    }
}

