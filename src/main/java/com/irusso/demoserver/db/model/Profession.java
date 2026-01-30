package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Profession entity representing a row in the professions table.
 */
public class Profession {

    private Long id;
    private String name;
    private PreparationLevel prep;
    private Instant createdAt;
    private Instant updatedAt;

    public Profession() {
        // Default constructor for JDBI
    }

    public Profession(String name, PreparationLevel prep) {
        this.name = name;
        this.prep = prep;
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
    public PreparationLevel getPrep() {
        return prep;
    }

    @JsonProperty
    public void setPrep(PreparationLevel prep) {
        this.prep = prep;
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

