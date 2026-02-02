package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserIndustry entity representing a row in the user_industries table.
 * Represents an industry that a user is interested in or has experience with.
 */
public class UserIndustry {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("industry_id")
    private Long industryId;
    
    @JsonProperty("is_current")
    private Boolean isCurrent;
    
    @JsonProperty("years_of_experience")
    private Integer yearsOfExperience;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public UserIndustry() {}
    
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
    
    public Long getIndustryId() {
        return industryId;
    }
    
    public void setIndustryId(Long industryId) {
        this.industryId = industryId;
    }
    
    public Boolean getIsCurrent() {
        return isCurrent;
    }
    
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
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

