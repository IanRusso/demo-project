package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class EmploymentHistoryTest {

    @Test
    void testDefaultConstructor() {
        // Act
        EmploymentHistory history = new EmploymentHistory();

        // Assert
        assertThat(history).isNotNull();
        assertThat(history.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        EmploymentHistory history = new EmploymentHistory();
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis());
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        history.setId(1L);
        history.setUserId(100L);
        history.setJobTitle("Software Engineer");
        history.setEmployerName("Tech Corp");
        history.setStartDate(startDate);
        history.setEndDate(endDate);
        history.setDescription("Developed software");
        history.setIsCurrent(true);
        history.setCreatedAt(now);
        history.setUpdatedAt(now);

        // Assert
        assertThat(history.getId()).isEqualTo(1L);
        assertThat(history.getUserId()).isEqualTo(100L);
        assertThat(history.getJobTitle()).isEqualTo("Software Engineer");
        assertThat(history.getEmployerName()).isEqualTo("Tech Corp");
        assertThat(history.getStartDate()).isEqualTo(startDate);
        assertThat(history.getEndDate()).isEqualTo(endDate);
        assertThat(history.getDescription()).isEqualTo("Developed software");
        assertThat(history.getIsCurrent()).isTrue();
        assertThat(history.getCreatedAt()).isEqualTo(now);
        assertThat(history.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        // Arrange
        EmploymentHistory history = new EmploymentHistory();

        // Act
        history.setId(1L);
        history.setUserId(100L);

        // Assert - nullable fields should be null
        assertThat(history.getEndDate()).isNull();
        assertThat(history.getDescription()).isNull();
        assertThat(history.getIsCurrent()).isNull();
    }
}

