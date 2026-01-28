package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * EmployerHistory entity representing a row in the employer_history table.
 * Represents hiring or layoff events for an employer.
 */
public class EmployerHistory {
    
    private Long id;
    
    @JsonProperty("employer_id")
    private Long employerId;
    
    @JsonProperty("event_type")
    private String eventType;
    
    @JsonProperty("event_date")
    private Date eventDate;
    
    @JsonProperty("number_of_positions")
    private Integer numberOfPositions;
    
    private String source;
    private String description;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    // Constructors
    public EmployerHistory() {}
    
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
    
    public String getEventType() {
        return eventType;
    }
    
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
    
    public Date getEventDate() {
        return eventDate;
    }
    
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    
    public Integer getNumberOfPositions() {
        return numberOfPositions;
    }
    
    public void setNumberOfPositions(Integer numberOfPositions) {
        this.numberOfPositions = numberOfPositions;
    }
    
    public String getSource() {
        return source;
    }
    
    public void setSource(String source) {
        this.source = source;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

