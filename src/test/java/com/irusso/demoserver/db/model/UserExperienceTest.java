package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for UserExperience model.
 */
class UserExperienceTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        UserExperience experience = new UserExperience();
        Long id = 1L;
        Long userId = 100L;
        String title = "Led development team";
        String description = "Led a team of 5 developers";
        String experienceType = "LEADERSHIP";
        Date startDate = Date.valueOf("2023-01-01");
        Date endDate = Date.valueOf("2023-06-30");
        Boolean isCurrent = false;
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        Timestamp updatedAt = new Timestamp(System.currentTimeMillis());

        // Act
        experience.setId(id);
        experience.setUserId(userId);
        experience.setTitle(title);
        experience.setDescription(description);
        experience.setExperienceType(experienceType);
        experience.setStartDate(startDate);
        experience.setEndDate(endDate);
        experience.setIsCurrent(isCurrent);
        experience.setCreatedAt(createdAt);
        experience.setUpdatedAt(updatedAt);

        // Assert
        assertThat(experience.getId()).isEqualTo(id);
        assertThat(experience.getUserId()).isEqualTo(userId);
        assertThat(experience.getTitle()).isEqualTo(title);
        assertThat(experience.getDescription()).isEqualTo(description);
        assertThat(experience.getExperienceType()).isEqualTo(experienceType);
        assertThat(experience.getStartDate()).isEqualTo(startDate);
        assertThat(experience.getEndDate()).isEqualTo(endDate);
        assertThat(experience.getIsCurrent()).isEqualTo(isCurrent);
        assertThat(experience.getCreatedAt()).isEqualTo(createdAt);
        assertThat(experience.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void testNoArgsConstructor() {
        // Act
        UserExperience experience = new UserExperience();

        // Assert
        assertThat(experience).isNotNull();
        assertThat(experience.getId()).isNull();
        assertThat(experience.getUserId()).isNull();
        assertThat(experience.getTitle()).isNull();
        assertThat(experience.getDescription()).isNull();
        assertThat(experience.getExperienceType()).isNull();
        assertThat(experience.getStartDate()).isNull();
        assertThat(experience.getEndDate()).isNull();
        assertThat(experience.getIsCurrent()).isNull();
        assertThat(experience.getCreatedAt()).isNull();
        assertThat(experience.getUpdatedAt()).isNull();
    }

    @Test
    void testCurrentExperience() {
        // Arrange
        UserExperience experience = new UserExperience();
        experience.setTitle("Current project");
        experience.setIsCurrent(true);
        experience.setStartDate(Date.valueOf("2024-01-01"));

        // Assert
        assertThat(experience.getIsCurrent()).isTrue();
        assertThat(experience.getEndDate()).isNull();
    }

    @Test
    void testPastExperience() {
        // Arrange
        UserExperience experience = new UserExperience();
        experience.setTitle("Past project");
        experience.setIsCurrent(false);
        experience.setStartDate(Date.valueOf("2023-01-01"));
        experience.setEndDate(Date.valueOf("2023-12-31"));

        // Assert
        assertThat(experience.getIsCurrent()).isFalse();
        assertThat(experience.getEndDate()).isNotNull();
    }
}

