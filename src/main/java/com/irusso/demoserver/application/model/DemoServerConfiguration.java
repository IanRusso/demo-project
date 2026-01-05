package com.irusso.demoserver.application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotEmpty;

/**
 * Configuration class for the Demo Server application.
 * This class holds all configuration properties loaded from the YAML file.
 */
public class DemoServerConfiguration extends Configuration {

    @NotEmpty
    private String applicationName = "Demo Server";

    @JsonProperty
    public String getApplicationName() {
        return applicationName;
    }

    @JsonProperty
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}

