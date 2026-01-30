package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

/**
 * Industry entity representing a row in the industries table.
 */
public class Industry {

    private Long id;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

    public Industry() {
        // Default constructor for JDBI
    }

    public Industry(String name) {
        this.name = name;
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

