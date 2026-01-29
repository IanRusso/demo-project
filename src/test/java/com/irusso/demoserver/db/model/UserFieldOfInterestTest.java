package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserFieldOfInterestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserFieldOfInterest fieldOfInterest = new UserFieldOfInterest();

        // Assert
        assertThat(fieldOfInterest).isNotNull();
        assertThat(fieldOfInterest.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UserFieldOfInterest fieldOfInterest = new UserFieldOfInterest();

        // Act
        fieldOfInterest.setId(1L);
        fieldOfInterest.setUserId(100L);
        fieldOfInterest.setFieldName("Technology");

        // Assert
        assertThat(fieldOfInterest.getId()).isEqualTo(1L);
        assertThat(fieldOfInterest.getUserId()).isEqualTo(100L);
        assertThat(fieldOfInterest.getFieldName()).isEqualTo("Technology");
    }
}

