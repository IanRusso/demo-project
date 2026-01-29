package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

class EmployerHistoryTest {

    @Test
    void testDefaultConstructor() {
        // Act
        EmployerHistory history = new EmployerHistory();

        // Assert
        assertThat(history).isNotNull();
        assertThat(history.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        EmployerHistory history = new EmployerHistory();
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis());
        Timestamp now = new Timestamp(System.currentTimeMillis());

        // Act
        history.setId(1L);
        history.setEmployerId(100L);
        history.setEventType("hiring");
        history.setEventDate(startDate);
        history.setNumberOfPositions(5);
        history.setSource("Company Website");
        history.setDescription("Hiring event");
        history.setCreatedAt(now);

        // Assert
        assertThat(history.getId()).isEqualTo(1L);
        assertThat(history.getEmployerId()).isEqualTo(100L);
        assertThat(history.getEventType()).isEqualTo("hiring");
        assertThat(history.getEventDate()).isEqualTo(startDate);
        assertThat(history.getNumberOfPositions()).isEqualTo(5);
        assertThat(history.getSource()).isEqualTo("Company Website");
        assertThat(history.getDescription()).isEqualTo("Hiring event");
        assertThat(history.getCreatedAt()).isEqualTo(now);
    }
}

