package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    @Test
    void testDefaultConstructor() {
        // Act
        Application application = new Application();

        // Assert
        assertThat(application).isNotNull();
        assertThat(application.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Application application = new Application();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        application.setId(1L);
        application.setUserId(100L);
        application.setJobPostingId(200L);
        application.setCoverLetter("I am very interested");
        application.setStatus("pending");
        application.setAppliedAt(now);
        application.setReviewedAt(now);
        application.setResponseDeadline(now);
        application.setCreatedAt(now);
        application.setUpdatedAt(now);

        // Assert
        assertThat(application.getId()).isEqualTo(1L);
        assertThat(application.getUserId()).isEqualTo(100L);
        assertThat(application.getJobPostingId()).isEqualTo(200L);
        assertThat(application.getCoverLetter()).isEqualTo("I am very interested");
        assertThat(application.getStatus()).isEqualTo("pending");
        assertThat(application.getAppliedAt()).isEqualTo(now);
        assertThat(application.getReviewedAt()).isEqualTo(now);
        assertThat(application.getResponseDeadline()).isEqualTo(now);
        assertThat(application.getCreatedAt()).isEqualTo(now);
        assertThat(application.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        // Arrange
        Application application = new Application();

        // Act
        application.setId(1L);
        application.setUserId(100L);
        application.setJobPostingId(200L);

        // Assert - nullable fields should be null
        assertThat(application.getCoverLetter()).isNull();
        assertThat(application.getReviewedAt()).isNull();
        assertThat(application.getResponseDeadline()).isNull();
    }
}

