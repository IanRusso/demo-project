package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Employer entity representing a row in the employers table.
 * Represents a company/employer profile in the job marketplace.
 */
public class Employer {
    
    private Long id;
    private String name;
    private String email;
    
    @JsonProperty("phone_number")
    private String phoneNumber;
    
    private String location;
    
    @JsonProperty("company_size")
    private String companySize;
    
    @JsonProperty("company_type")
    private String companyType;
    
    private String description;
    
    @JsonProperty("company_picture_url")
    private String companyPictureUrl;
    
    @JsonProperty("employer_rating")
    private BigDecimal employerRating;
    
    @JsonProperty("communication_rating")
    private BigDecimal communicationRating;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
    
    @JsonProperty("updated_at")
    private Timestamp updatedAt;
    
    // Constructors
    public Employer() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getCompanySize() {
        return companySize;
    }
    
    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }
    
    public String getCompanyType() {
        return companyType;
    }
    
    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCompanyPictureUrl() {
        return companyPictureUrl;
    }
    
    public void setCompanyPictureUrl(String companyPictureUrl) {
        this.companyPictureUrl = companyPictureUrl;
    }
    
    public BigDecimal getEmployerRating() {
        return employerRating;
    }
    
    public void setEmployerRating(BigDecimal employerRating) {
        this.employerRating = employerRating;
    }
    
    public BigDecimal getCommunicationRating() {
        return communicationRating;
    }
    
    public void setCommunicationRating(BigDecimal communicationRating) {
        this.communicationRating = communicationRating;
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

