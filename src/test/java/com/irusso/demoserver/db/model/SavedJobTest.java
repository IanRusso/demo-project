package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class SavedJobTest {

    @Test
    void testDefaultConstructor() {
        // Act
        SavedJob savedJob = new SavedJob();

        // Assert
        assertThat(savedJob).isNotNull();
        assertThat(savedJob.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        SavedJob savedJob = new SavedJob();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        savedJob.setId(1L);
        savedJob.setUserId(100L);
        savedJob.setJobPostingId(200L);
        savedJob.setSavedAt(now);

        // Assert
        assertThat(savedJob.getId()).isEqualTo(1L);
        assertThat(savedJob.getUserId()).isEqualTo(100L);
        assertThat(savedJob.getJobPostingId()).isEqualTo(200L);
        assertThat(savedJob.getSavedAt()).isEqualTo(now);
    }
}

