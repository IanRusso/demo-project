package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.EmployerHistoryDao;
import com.irusso.demoserver.db.model.EmployerHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerHistoryServiceTest {

    @Mock
    private EmployerHistoryDao employerHistoryDao;

    private EmployerHistoryService employerHistoryService;

    @BeforeEach
    void setUp() {
        employerHistoryService = new EmployerHistoryService(employerHistoryDao);
    }

    @Test
    void testGetAllEmployerHistory() {
        // Arrange
        EmployerHistory history1 = new EmployerHistory();
        history1.setId(1L);
        history1.setEmployerId(100L);

        EmployerHistory history2 = new EmployerHistory();
        history2.setId(2L);
        history2.setEmployerId(101L);

        List<EmployerHistory> expectedHistory = Arrays.asList(history1, history2);
        when(employerHistoryDao.findAll()).thenReturn(expectedHistory);

        // Act
        List<EmployerHistory> actualHistory = employerHistoryService.getAllEmployerHistory();

        // Assert
        assertThat(actualHistory).hasSize(2);
        verify(employerHistoryDao, times(1)).findAll();
    }

    @Test
    void testGetEmployerHistoryById_Found() {
        // Arrange
        EmployerHistory history = new EmployerHistory();
        history.setId(1L);
        history.setEmployerId(100L);

        when(employerHistoryDao.findById(1L)).thenReturn(Optional.of(history));

        // Act
        Optional<EmployerHistory> result = employerHistoryService.getEmployerHistoryById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(employerHistoryDao, times(1)).findById(1L);
    }

    @Test
    void testGetEmployerHistoryById_NotFound() {
        // Arrange
        when(employerHistoryDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<EmployerHistory> result = employerHistoryService.getEmployerHistoryById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(employerHistoryDao, times(1)).findById(999L);
    }

    @Test
    void testCreateEmployerHistory() {
        // Arrange
        EmployerHistory newHistory = new EmployerHistory();
        newHistory.setEmployerId(100L);

        when(employerHistoryDao.insert(newHistory)).thenReturn(1L);

        // Act
        Long historyId = employerHistoryService.createEmployerHistory(newHistory);

        // Assert
        assertThat(historyId).isEqualTo(1L);
        verify(employerHistoryDao, times(1)).insert(newHistory);
    }

    @Test
    void testUpdateEmployerHistory_Success() {
        // Arrange
        EmployerHistory history = new EmployerHistory();
        history.setId(1L);

        when(employerHistoryDao.update(1L, history)).thenReturn(true);

        // Act
        boolean result = employerHistoryService.updateEmployerHistory(history);

        // Assert
        assertThat(result).isTrue();
        verify(employerHistoryDao, times(1)).update(1L, history);
    }

    @Test
    void testUpdateEmployerHistory_Failure() {
        // Arrange
        EmployerHistory history = new EmployerHistory();
        history.setId(999L);

        when(employerHistoryDao.update(999L, history)).thenReturn(false);

        // Act
        boolean result = employerHistoryService.updateEmployerHistory(history);

        // Assert
        assertThat(result).isFalse();
        verify(employerHistoryDao, times(1)).update(999L, history);
    }

    @Test
    void testDeleteEmployerHistory_Success() {
        // Arrange
        when(employerHistoryDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = employerHistoryService.deleteEmployerHistory(1L);

        // Assert
        assertThat(result).isTrue();
        verify(employerHistoryDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteEmployerHistory_Failure() {
        // Arrange
        when(employerHistoryDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = employerHistoryService.deleteEmployerHistory(999L);

        // Assert
        assertThat(result).isFalse();
        verify(employerHistoryDao, times(1)).delete(999L);
    }
}

