package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * Application entity representing a row in the applications table.
 * Represents a user's application to a job posting.
 */
public class Application {
    
    private Long id;
    
    @JsonProperty("user_id")
    private Long userId;
    
    @JsonProperty("job_posting_id")
    private Long jobPostingId;
    
    @JsonProperty("cover_letter")
    private String coverLetter;
    
    private String status;
    
    @JsonProperty("applied_at")
    private Timestamp appliedAt;
    
    @JsonProperty("reviewed_at")
    private Timestamp reviewedAt;
    
    @JsonProperty("response_deadline")
    private Timestamp responseDeadline;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public Application() {}
    
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
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getAppliedAt() {
        return appliedAt;
    }
    
    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public Timestamp getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public Timestamp getResponseDeadline() {
        return responseDeadline;
    }
    
    public void setResponseDeadline(Timestamp responseDeadline) {
        this.responseDeadline = responseDeadline;
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

