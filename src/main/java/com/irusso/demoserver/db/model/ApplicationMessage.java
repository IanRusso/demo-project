package com.irusso.demoserver.db.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;

/**
 * ApplicationMessage entity representing a row in the application_messages table.
 * Represents a message exchanged between user and employer regarding an application.
 */
public class ApplicationMessage {
    
    private Long id;
    
    @JsonProperty("application_id")
    private Long applicationId;
    
    @JsonProperty("sender_type")
    private String senderType;
    
    @JsonProperty("message_type")
    private String messageType;
    
    @JsonProperty("message_text")
    private String messageText;
    
    @JsonProperty("sent_at")
    private Timestamp sentAt;
    
    @JsonProperty("read_at")
    private Timestamp readAt;
    
    // Constructors
    public ApplicationMessage() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    
    public String getSenderType() {
        return senderType;
    }
    
    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }
    
    public String getMessageType() {
        return messageType;
    }
    
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    
    public String getMessageText() {
        return messageText;
    }
    
    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    public Timestamp getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
    
    public Timestamp getReadAt() {
        return readAt;
    }
    
    public void setReadAt(Timestamp readAt) {
        this.readAt = readAt;
    }
}

