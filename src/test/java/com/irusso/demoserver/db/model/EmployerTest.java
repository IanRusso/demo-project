package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class EmployerTest {

    @Test
    void testDefaultConstructor() {
        // Act
        Employer employer = new Employer();

        // Assert
        assertThat(employer).isNotNull();
        assertThat(employer.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        Employer employer = new Employer();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        employer.setId(1L);
        employer.setName("Tech Corp");
        employer.setEmail("contact@techcorp.com");
        employer.setPhoneNumber("555-1234");
        employer.setLocation("San Francisco, CA");
        employer.setCompanySize("100-500");
        employer.setCompanyType("Technology");
        employer.setDescription("A leading tech company");
        employer.setCompanyPictureUrl("https://example.com/logo.png");
        employer.setEmployerRating(new BigDecimal("4.5"));
        employer.setCommunicationRating(new BigDecimal("4.8"));
        employer.setCreatedAt(now);
        employer.setUpdatedAt(now);

        // Assert
        assertThat(employer.getId()).isEqualTo(1L);
        assertThat(employer.getName()).isEqualTo("Tech Corp");
        assertThat(employer.getEmail()).isEqualTo("contact@techcorp.com");
        assertThat(employer.getPhoneNumber()).isEqualTo("555-1234");
        assertThat(employer.getLocation()).isEqualTo("San Francisco, CA");
        assertThat(employer.getCompanySize()).isEqualTo("100-500");
        assertThat(employer.getCompanyType()).isEqualTo("Technology");
        assertThat(employer.getDescription()).isEqualTo("A leading tech company");
        assertThat(employer.getCompanyPictureUrl()).isEqualTo("https://example.com/logo.png");
        assertThat(employer.getEmployerRating()).isEqualByComparingTo(new BigDecimal("4.5"));
        assertThat(employer.getCommunicationRating()).isEqualByComparingTo(new BigDecimal("4.8"));
        assertThat(employer.getCreatedAt()).isEqualTo(now);
        assertThat(employer.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        // Arrange
        Employer employer = new Employer();

        // Act
        employer.setId(1L);
        employer.setName("Tech Corp");

        // Assert - nullable fields should be null
        assertThat(employer.getEmail()).isNull();
        assertThat(employer.getPhoneNumber()).isNull();
        assertThat(employer.getDescription()).isNull();
        assertThat(employer.getCompanyPictureUrl()).isNull();
        assertThat(employer.getEmployerRating()).isNull();
        assertThat(employer.getCommunicationRating()).isNull();
    }
}

