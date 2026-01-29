package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for User model.
 */
class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertThat(user).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        Instant now = Instant.now();

        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPhoneNumber("555-1234");
        user.setLocation("New York");
        user.setEducationLevel("Bachelor's");
        user.setSummary("Experienced developer");
        user.setProfilePictureUrl("http://example.com/pic.jpg");
        user.setEmploymentStatus("Employed");
        user.setBackgroundCheckStatus("Passed");
        user.setUserRating(new BigDecimal("4.5"));
        user.setCommunicationRating(new BigDecimal("4.8"));
        user.setSalaryExpectationsMin(new BigDecimal("80000"));
        user.setSalaryExpectationsMax(new BigDecimal("120000"));
        user.setActivelySeeking(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getPhoneNumber()).isEqualTo("555-1234");
        assertThat(user.getLocation()).isEqualTo("New York");
        assertThat(user.getEducationLevel()).isEqualTo("Bachelor's");
        assertThat(user.getSummary()).isEqualTo("Experienced developer");
        assertThat(user.getProfilePictureUrl()).isEqualTo("http://example.com/pic.jpg");
        assertThat(user.getEmploymentStatus()).isEqualTo("Employed");
        assertThat(user.getBackgroundCheckStatus()).isEqualTo("Passed");
        assertThat(user.getUserRating()).isEqualByComparingTo(new BigDecimal("4.5"));
        assertThat(user.getCommunicationRating()).isEqualByComparingTo(new BigDecimal("4.8"));
        assertThat(user.getSalaryExpectationsMin()).isEqualByComparingTo(new BigDecimal("80000"));
        assertThat(user.getSalaryExpectationsMax()).isEqualByComparingTo(new BigDecimal("120000"));
        assertThat(user.getActivelySeeking()).isTrue();
        assertThat(user.getCreatedAt()).isEqualTo(now);
        assertThat(user.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        User user = new User();
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@example.com");

        assertThat(user.getPhoneNumber()).isNull();
        assertThat(user.getLocation()).isNull();
        assertThat(user.getEducationLevel()).isNull();
        assertThat(user.getSummary()).isNull();
        assertThat(user.getProfilePictureUrl()).isNull();
        assertThat(user.getEmploymentStatus()).isNull();
        assertThat(user.getBackgroundCheckStatus()).isNull();
        assertThat(user.getUserRating()).isNull();
        assertThat(user.getCommunicationRating()).isNull();
        assertThat(user.getSalaryExpectationsMin()).isNull();
        assertThat(user.getSalaryExpectationsMax()).isNull();
        assertThat(user.getActivelySeeking()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }
}

