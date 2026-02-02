package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * UserProfession entity representing a row in the user_professions table.
 * Represents a profession/occupation that a user is interested in or has experience with.
 */
public class UserProfession {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("profession_id")
    private Long professionId;
    
    @JsonProperty("is_current")
    private Boolean isCurrent;
    
    @JsonProperty("years_of_experience")
    private Integer yearsOfExperience;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public UserProfession() {}
    
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
    
    public Long getProfessionId() {
        return professionId;
    }
    
    public void setProfessionId(Long professionId) {
        this.professionId = professionId;
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

