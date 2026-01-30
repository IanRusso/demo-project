package com.irusso.demoserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

/**
 * Request object for importing professions from a CSV file URL.
 */
public class ProfessionImportRequest {
    
    @NotEmpty
    private String url;
    
    @JsonProperty
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
}

