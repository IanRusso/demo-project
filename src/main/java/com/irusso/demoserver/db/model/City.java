package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * City entity representing a row in the cities table.
 * Contains geographic and demographic information about cities worldwide.
 */
public class City {
    
    private Long id;
    private Long geonameId;
    private String name;
    private String asciiName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String featureClass;
    private String featureCode;
    private String countryCode;
    private String cc2;
    private String admin1Code;
    private String admin2Code;
    private String admin3Code;
    private String admin4Code;
    private Long population;
    private Integer elevation;
    private Integer dem;
    private String timezone;
    private Date modificationDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    @JsonProperty
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @JsonProperty
    public Long getGeonameId() {
        return geonameId;
    }
    
    public void setGeonameId(Long geonameId) {
        this.geonameId = geonameId;
    }
    
    @JsonProperty
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @JsonProperty
    public String getAsciiName() {
        return asciiName;
    }
    
    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    @JsonProperty
    public BigDecimal getLatitude() {
        return latitude;
    }
    
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
    
    @JsonProperty
    public BigDecimal getLongitude() {
        return longitude;
    }
    
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
    
    @JsonProperty
    public String getFeatureClass() {
        return featureClass;
    }
    
    public void setFeatureClass(String featureClass) {
        this.featureClass = featureClass;
    }
    
    @JsonProperty
    public String getFeatureCode() {
        return featureCode;
    }
    
    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }
    
    @JsonProperty
    public String getCountryCode() {
        return countryCode;
    }
    
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    
    @JsonProperty
    public String getCc2() {
        return cc2;
    }
    
    public void setCc2(String cc2) {
        this.cc2 = cc2;
    }
    
    @JsonProperty
    public String getAdmin1Code() {
        return admin1Code;
    }
    
    public void setAdmin1Code(String admin1Code) {
        this.admin1Code = admin1Code;
    }
    
    @JsonProperty
    public String getAdmin2Code() {
        return admin2Code;
    }
    
    public void setAdmin2Code(String admin2Code) {
        this.admin2Code = admin2Code;
    }
    
    @JsonProperty
    public String getAdmin3Code() {
        return admin3Code;
    }
    
    public void setAdmin3Code(String admin3Code) {
        this.admin3Code = admin3Code;
    }
    
    @JsonProperty
    public String getAdmin4Code() {
        return admin4Code;
    }
    
    public void setAdmin4Code(String admin4Code) {
        this.admin4Code = admin4Code;
    }
    
    @JsonProperty
    public Long getPopulation() {
        return population;
    }
    
    public void setPopulation(Long population) {
        this.population = population;
    }
    
    @JsonProperty
    public Integer getElevation() {
        return elevation;
    }
    
    public void setElevation(Integer elevation) {
        this.elevation = elevation;
    }
    
    @JsonProperty
    public Integer getDem() {
        return dem;
    }
    
    public void setDem(Integer dem) {
        this.dem = dem;
    }
    
    @JsonProperty
    public String getTimezone() {
        return timezone;
    }
    
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    
    @JsonProperty
    public Date getModificationDate() {
        return modificationDate;
    }
    
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
    
    @JsonProperty
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @JsonProperty
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

