package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * JobPosting entity representing a row in the job_postings table.
 * Represents a job posting created by an employer.
 */
public class JobPosting {
    
    private Long id;
    
    @JsonProperty("employer_id")
    private Long employerId;
    
    private String title;
    private String description;
    private String responsibilities;
    private String location;
    private String field;
    
    @JsonProperty("experience_level")
    private String experienceLevel;
    
    @JsonProperty("salary_min")
    private BigDecimal salaryMin;
    
    @JsonProperty("salary_max")
    private BigDecimal salaryMax;
    
    private String status;
    
    @JsonProperty("posted_date")
    private Timestamp postedDate;
    
    @JsonProperty("closed_date")
    private Timestamp closedDate;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public JobPosting() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEmployerId() {
        return employerId;
    }
    
    public void setEmployerId(Long employerId) {
        this.employerId = employerId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getResponsibilities() {
        return responsibilities;
    }
    
    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getField() {
        return field;
    }
    
    public void setField(String field) {
        this.field = field;
    }
    
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }
    
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getPostedDate() {
        return postedDate;
    }
    
    public void setPostedDate(Timestamp postedDate) {
        this.postedDate = postedDate;
    }
    
    public Timestamp getClosedDate() {
        return closedDate;
    }
    
    public void setClosedDate(Timestamp closedDate) {
        this.closedDate = closedDate;
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

