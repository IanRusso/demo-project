package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.EmploymentHistoryDao;
import com.irusso.demoserver.db.model.EmploymentHistory;
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
class EmploymentHistoryServiceTest {

    @Mock
    private EmploymentHistoryDao employmentHistoryDao;

    private EmploymentHistoryService employmentHistoryService;

    @BeforeEach
    void setUp() {
        employmentHistoryService = new EmploymentHistoryService(employmentHistoryDao);
    }

    @Test
    void testGetAllEmploymentHistory() {
        // Arrange
        EmploymentHistory history1 = new EmploymentHistory();
        history1.setId(1L);
        history1.setUserId(100L);

        EmploymentHistory history2 = new EmploymentHistory();
        history2.setId(2L);
        history2.setUserId(101L);

        List<EmploymentHistory> expectedHistory = Arrays.asList(history1, history2);
        when(employmentHistoryDao.findAll()).thenReturn(expectedHistory);

        // Act
        List<EmploymentHistory> actualHistory = employmentHistoryService.getAllEmploymentHistory();

        // Assert
        assertThat(actualHistory).hasSize(2);
        verify(employmentHistoryDao, times(1)).findAll();
    }

    @Test
    void testGetEmploymentHistoryById_Found() {
        // Arrange
        EmploymentHistory history = new EmploymentHistory();
        history.setId(1L);
        history.setUserId(100L);

        when(employmentHistoryDao.findById(1L)).thenReturn(Optional.of(history));

        // Act
        Optional<EmploymentHistory> result = employmentHistoryService.getEmploymentHistoryById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(employmentHistoryDao, times(1)).findById(1L);
    }

    @Test
    void testGetEmploymentHistoryById_NotFound() {
        // Arrange
        when(employmentHistoryDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<EmploymentHistory> result = employmentHistoryService.getEmploymentHistoryById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(employmentHistoryDao, times(1)).findById(999L);
    }

    @Test
    void testCreateEmploymentHistory() {
        // Arrange
        EmploymentHistory newHistory = new EmploymentHistory();
        newHistory.setUserId(100L);

        when(employmentHistoryDao.insert(newHistory)).thenReturn(1L);

        // Act
        Long historyId = employmentHistoryService.createEmploymentHistory(newHistory);

        // Assert
        assertThat(historyId).isEqualTo(1L);
        verify(employmentHistoryDao, times(1)).insert(newHistory);
    }

    @Test
    void testUpdateEmploymentHistory_Success() {
        // Arrange
        EmploymentHistory history = new EmploymentHistory();
        history.setId(1L);

        when(employmentHistoryDao.update(1L, history)).thenReturn(true);

        // Act
        boolean result = employmentHistoryService.updateEmploymentHistory(history);

        // Assert
        assertThat(result).isTrue();
        verify(employmentHistoryDao, times(1)).update(1L, history);
    }

    @Test
    void testUpdateEmploymentHistory_Failure() {
        // Arrange
        EmploymentHistory history = new EmploymentHistory();
        history.setId(999L);

        when(employmentHistoryDao.update(999L, history)).thenReturn(false);

        // Act
        boolean result = employmentHistoryService.updateEmploymentHistory(history);

        // Assert
        assertThat(result).isFalse();
        verify(employmentHistoryDao, times(1)).update(999L, history);
    }

    @Test
    void testDeleteEmploymentHistory_Success() {
        // Arrange
        when(employmentHistoryDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = employmentHistoryService.deleteEmploymentHistory(1L);

        // Assert
        assertThat(result).isTrue();
        verify(employmentHistoryDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteEmploymentHistory_Failure() {
        // Arrange
        when(employmentHistoryDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = employmentHistoryService.deleteEmploymentHistory(999L);

        // Assert
        assertThat(result).isFalse();
        verify(employmentHistoryDao, times(1)).delete(999L);
    }
}

