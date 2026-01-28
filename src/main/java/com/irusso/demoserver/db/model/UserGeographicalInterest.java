package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserGeographicalInterest entity representing a row in the user_geographical_interests table.
 * Represents a geographical location where a user is interested in working.
 */
public class UserGeographicalInterest {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    private String location;
    
    @JsonProperty("is_hard_requirement")
    private Boolean isHardRequirement;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    // Constructors
    public UserGeographicalInterest() {}
    
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Boolean getIsHardRequirement() {
        return isHardRequirement;
    }
    
    public void setIsHardRequirement(Boolean isHardRequirement) {
        this.isHardRequirement = isHardRequirement;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

