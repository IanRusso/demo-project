package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Employee entity representing a row in the employees table.
 */
public class Employee {

    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String location;
    private String educationLevel;
    private String summary;
    private String profilePictureUrl;
    private String employmentStatus;
    private String backgroundCheckStatus;
    private BigDecimal employeeRating;
    private BigDecimal communicationRating;
    private BigDecimal salaryExpectationsMin;
    private BigDecimal salaryExpectationsMax;
    private Boolean activelySeeking;
    private Instant createdAt;
    private Instant updatedAt;

    public Employee() {
        // Default constructor for JDBI
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonProperty
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @JsonProperty
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @JsonProperty
    public String getLocation() {
        return location;
    }

    @JsonProperty
    public void setLocation(String location) {
        this.location = location;
    }

    @JsonProperty
    public String getEducationLevel() {
        return educationLevel;
    }

    @JsonProperty
    public void setEducationLevel(String educationLevel) {
        this.educationLevel = educationLevel;
    }

    @JsonProperty
    public String getSummary() {
        return summary;
    }

    @JsonProperty
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty
    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    @JsonProperty
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    @JsonProperty
    public String getEmploymentStatus() {
        return employmentStatus;
    }

    @JsonProperty
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    @JsonProperty
    public String getBackgroundCheckStatus() {
        return backgroundCheckStatus;
    }

    @JsonProperty
    public void setBackgroundCheckStatus(String backgroundCheckStatus) {
        this.backgroundCheckStatus = backgroundCheckStatus;
    }

    @JsonProperty
    public BigDecimal getEmployeeRating() {
        return employeeRating;
    }

    @JsonProperty
    public void setEmployeeRating(BigDecimal employeeRating) {
        this.employeeRating = employeeRating;
    }

    @JsonProperty
    public BigDecimal getCommunicationRating() {
        return communicationRating;
    }

    @JsonProperty
    public void setCommunicationRating(BigDecimal communicationRating) {
        this.communicationRating = communicationRating;
    }

    @JsonProperty
    public BigDecimal getSalaryExpectationsMin() {
        return salaryExpectationsMin;
    }

    @JsonProperty
    public void setSalaryExpectationsMin(BigDecimal salaryExpectationsMin) {
        this.salaryExpectationsMin = salaryExpectationsMin;
    }

    @JsonProperty
    public BigDecimal getSalaryExpectationsMax() {
        return salaryExpectationsMax;
    }

    @JsonProperty
    public void setSalaryExpectationsMax(BigDecimal salaryExpectationsMax) {
        this.salaryExpectationsMax = salaryExpectationsMax;
    }

    @JsonProperty
    public Boolean getActivelySeeking() {
        return activelySeeking;
    }

    @JsonProperty
    public void setActivelySeeking(Boolean activelySeeking) {
        this.activelySeeking = activelySeeking;
    }

    @JsonProperty
    public Instant getCreatedAt() {
        return createdAt;
    }

    @JsonProperty
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @JsonProperty
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @JsonProperty
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}

