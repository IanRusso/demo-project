package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * SavedJob entity representing a row in the saved_jobs table.
 * Represents a job posting that a user has saved for later review.
 */
public class SavedJob {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("job_posting_id")
    private Long jobPostingId;
    
    @JsonProperty("saved_at")
    private Timestamp savedAt;
    
    private String notes;
    
    // Constructors
    public SavedJob() {}
    
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
    
    public Long getJobPostingId() {
        return jobPostingId;
    }
    
    public void setJobPostingId(Long jobPostingId) {
        this.jobPostingId = jobPostingId;
    }
    
    public Timestamp getSavedAt() {
        return savedAt;
    }
    
    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
}

