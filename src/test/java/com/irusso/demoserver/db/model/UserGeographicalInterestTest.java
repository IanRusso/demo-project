package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserGeographicalInterestTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserGeographicalInterest geoInterest = new UserGeographicalInterest();

        // Assert
        assertThat(geoInterest).isNotNull();
        assertThat(geoInterest.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UserGeographicalInterest geoInterest = new UserGeographicalInterest();

        // Act
        geoInterest.setId(1L);
        geoInterest.setUserId(100L);
        geoInterest.setLocation("San Francisco, CA");

        // Assert
        assertThat(geoInterest.getId()).isEqualTo(1L);
        assertThat(geoInterest.getUserId()).isEqualTo(100L);
        assertThat(geoInterest.getLocation()).isEqualTo("San Francisco, CA");
    }
}

