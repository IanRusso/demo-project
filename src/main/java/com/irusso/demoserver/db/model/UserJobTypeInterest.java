package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserJobTypeInterest entity representing a row in the user_job_type_interests table.
 * Represents a type of job (e.g., full-time, part-time, contract) that a user is interested in.
 */
public class UserJobTypeInterest {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("job_type")
    private String jobType;
    
    @JsonProperty("is_hard_requirement")
    private Boolean isHardRequirement;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    // Constructors
    public UserJobTypeInterest() {}
    
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
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
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

