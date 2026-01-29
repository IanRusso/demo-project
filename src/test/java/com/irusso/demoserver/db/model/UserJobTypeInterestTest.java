package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserJobTypeInterestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserJobTypeInterest jobTypeInterest = new UserJobTypeInterest();

        // Assert
        assertThat(jobTypeInterest).isNotNull();
        assertThat(jobTypeInterest.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UserJobTypeInterest jobTypeInterest = new UserJobTypeInterest();

        // Act
        jobTypeInterest.setId(1L);
        jobTypeInterest.setUserId(100L);
        jobTypeInterest.setJobType("Full-time");

        // Assert
        assertThat(jobTypeInterest.getId()).isEqualTo(1L);
        assertThat(jobTypeInterest.getUserId()).isEqualTo(100L);
        assertThat(jobTypeInterest.getJobType()).isEqualTo("Full-time");
    }
}

