package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationMessageTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ApplicationMessage message = new ApplicationMessage();

        // Assert
        assertThat(message).isNotNull();
        assertThat(message.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ApplicationMessage message = new ApplicationMessage();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        message.setId(1L);
        message.setApplicationId(100L);
        message.setSenderType("user");
        message.setMessageType("inquiry");
        message.setMessageText("Hello, I have a question");
        message.setSentAt(now);
        message.setReadAt(now);

        // Assert
        assertThat(message.getId()).isEqualTo(1L);
        assertThat(message.getApplicationId()).isEqualTo(100L);
        assertThat(message.getSenderType()).isEqualTo("user");
        assertThat(message.getMessageType()).isEqualTo("inquiry");
        assertThat(message.getMessageText()).isEqualTo("Hello, I have a question");
        assertThat(message.getSentAt()).isEqualTo(now);
        assertThat(message.getReadAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        // Arrange
        ApplicationMessage message = new ApplicationMessage();

        // Act
        message.setId(1L);
        message.setApplicationId(100L);
        message.setSenderType("user");

        // Assert - nullable fields should be null
        assertThat(message.getMessageType()).isNull();
        assertThat(message.getReadAt()).isNull();
    }
}

