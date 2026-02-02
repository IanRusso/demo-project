package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.UserConnectionDao;
import com.irusso.demoserver.db.model.UserConnection;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for user connections.
 */
public class UserConnectionService {
    
    private final UserConnectionDao userConnectionDao;
    
    @Inject
    public UserConnectionService(UserConnectionDao userConnectionDao) {
        this.userConnectionDao = userConnectionDao;
    }
    
    /**
     * Get all user connections.
     */
    public List<UserConnection> getAllUserConnections() {
        return userConnectionDao.findAll();
    }
    
    /**
     * Get a user connection by ID.
     */
    public Optional<UserConnection> getUserConnectionById(Long id) {
        return userConnectionDao.findById(id);
    }
    
    /**
     * Create a new user connection.
     */
    public Long createUserConnection(UserConnection userConnection) {
        return userConnectionDao.insert(userConnection);
    }
    
    /**
     * Update an existing user connection.
     */
    public boolean updateUserConnection(Long id, UserConnection userConnection) {
        return userConnectionDao.update(id, userConnection);
    }
    
    /**
     * Delete a user connection.
     */
    public boolean deleteUserConnection(Long id) {
        return userConnectionDao.delete(id);
    }
    
    /**
     * Find all connections for a specific user.
     */
    public List<UserConnection> findByUserId(Long userId) {
        return userConnectionDao.findByUserId(userId);
    }
    
    /**
     * Find all accepted connections for a specific user.
     */
    public List<UserConnection> findAcceptedByUserId(Long userId) {
        return userConnectionDao.findAcceptedByUserId(userId);
    }
    
    /**
     * Find all pending connection requests for a specific user.
     */
    public List<UserConnection> findPendingByUserId(Long userId) {
        return userConnectionDao.findPendingByUserId(userId);
    }
    
    /**
     * Find pending connection requests received by a user.
     */
    public List<UserConnection> findPendingReceivedByUserId(Long userId) {
        return userConnectionDao.findPendingReceivedByUserId(userId);
    }
    
    /**
     * Find pending connection requests sent by a user.
     */
    public List<UserConnection> findPendingSentByUserId(Long userId) {
        return userConnectionDao.findPendingSentByUserId(userId);
    }
    
    /**
     * Find a specific connection between two users.
     */
    public List<UserConnection> findConnectionBetweenUsers(Long userId1, Long userId2) {
        return userConnectionDao.findConnectionBetweenUsers(userId1, userId2);
    }
    
    /**
     * Delete all connections for a specific user.
     */
    public int deleteByUserId(Long userId) {
        return userConnectionDao.deleteByUserId(userId);
    }
}

