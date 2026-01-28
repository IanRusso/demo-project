package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserFieldOfInterest entity representing a row in the user_fields_of_interest table.
 * Represents a field/industry that a user is interested in working in.
 */
public class UserFieldOfInterest {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("field_name")
    private String fieldName;
    
    @JsonProperty("is_hard_requirement")
    private Boolean isHardRequirement;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    // Constructors
    public UserFieldOfInterest() {}
    
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
    
    public String getFieldName() {
        return fieldName;
    }
    
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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

