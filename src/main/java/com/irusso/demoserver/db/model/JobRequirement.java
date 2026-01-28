package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * JobRequirement entity representing a row in the job_requirements table.
 * Represents a requirement for a job posting (hard, soft, or preference).
 */
public class JobRequirement {
    
    private Long id;
    
    @JsonProperty("job_posting_id")
    private Long jobPostingId;
    
    @JsonProperty("requirement_type")
    private String requirementType;
    
    @JsonProperty("requirement_category")
    private String requirementCategory;
    
    @JsonProperty("requirement_text")
    private String requirementText;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    // Constructors
    public JobRequirement() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getJobPostingId() {
        return jobPostingId;
    }
    
    public void setJobPostingId(Long jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
    
    public String getRequirementType() {
        return requirementType;
    }
    
    public void setRequirementType(String requirementType) {
        this.requirementType = requirementType;
    }
    
    public String getRequirementCategory() {
        return requirementCategory;
    }
    
    public void setRequirementCategory(String requirementCategory) {
        this.requirementCategory = requirementCategory;
    }
    
    public String getRequirementText() {
        return requirementText;
    }
    
    public void setRequirementText(String requirementText) {
        this.requirementText = requirementText;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

