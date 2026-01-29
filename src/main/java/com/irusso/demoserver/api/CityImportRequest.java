package com.irusso.demoserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

/**
 * Request object for importing cities from a tar.gz file URL.
 */
public class CityImportRequest {
    
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

