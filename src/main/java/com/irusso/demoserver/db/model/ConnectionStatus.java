package com.irusso.demoserver.db.model;

/**
 * Enum representing the status of a user connection.
 */
public enum ConnectionStatus {
    /**
     * Connection request has been sent but not yet accepted.
     */
    PENDING("Pending"),
    
    /**
     * Connection request has been accepted and the connection is active.
     */
    ACCEPTED("Accepted"),
    
    /**
     * Connection has been blocked by one of the parties.
     */
    BLOCKED("Blocked");
    
    private final String displayName;
    
    ConnectionStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Get ConnectionStatus from string value.
     */
    public static ConnectionStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        for (ConnectionStatus status : ConnectionStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown connection status: " + value);
    }
}

