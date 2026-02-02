package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserConnection entity representing a row in the user_connections table.
 * Represents a professional connection between two users.
 */
public class UserConnection {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("connected_user_id")
    private Long connectedUserId;
    
    @JsonProperty("connection_status")
    private String connectionStatus;
    
    @JsonProperty("connection_type")
    private String connectionType;
    
    @JsonProperty("initiated_by_user_id")
    private Long initiatedByUserId;
    
    @JsonProperty("connected_at")
    private Timestamp connectedAt;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public UserConnection() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getConnectedUserId() {
        return connectedUserId;
    }
    
    public void setConnectedUserId(Long connectedUserId) {
        this.connectedUserId = connectedUserId;
    }
    
    public String getConnectionStatus() {
        return connectionStatus;
    }
    
    public void setConnectionStatus(String connectionStatus) {
        this.connectionStatus = connectionStatus;
    }
    
    public String getConnectionType() {
        return connectionType;
    }
    
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
    
    public Long getInitiatedByUserId() {
        return initiatedByUserId;
    }
    
    public void setInitiatedByUserId(Long initiatedByUserId) {
        this.initiatedByUserId = initiatedByUserId;
    }
    
    public Timestamp getConnectedAt() {
        return connectedAt;
    }
    
    public void setConnectedAt(Timestamp connectedAt) {
        this.connectedAt = connectedAt;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

