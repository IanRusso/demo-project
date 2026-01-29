package com.irusso.demoserver.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request object for changing a user's password.
 */
public class ChangePasswordRequest {

    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    private String newPassword;

    public ChangePasswordRequest() {
        // Default constructor for Jackson
    }

    public ChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    @JsonProperty
    public String getCurrentPassword() {
        return currentPassword;
    }

    @JsonProperty
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    @JsonProperty
    public String getNewPassword() {
        return newPassword;
    }

    @JsonProperty
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

