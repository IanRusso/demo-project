package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.EmployerDao;
import com.irusso.demoserver.db.model.Employer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for EmployerService.
 */
@ExtendWith(MockitoExtension.class)
class EmployerServiceTest {

    @Mock
    private EmployerDao employerDao;

    private EmployerService employerService;

    @BeforeEach
    void setUp() {
        employerService = new EmployerService(employerDao);
    }

    @Test
    void testGetAllEmployers() {
        // Arrange
        Employer employer1 = new Employer();
        employer1.setId(1L);
        employer1.setName("Company A");

        Employer employer2 = new Employer();
        employer2.setId(2L);
        employer2.setName("Company B");

        List<Employer> expectedEmployers = Arrays.asList(employer1, employer2);
        when(employerDao.findAll()).thenReturn(expectedEmployers);

        // Act
        List<Employer> actualEmployers = employerService.getAllEmployers();

        // Assert
        assertThat(actualEmployers).hasSize(2);
        assertThat(actualEmployers).containsExactlyElementsOf(expectedEmployers);
        verify(employerDao, times(1)).findAll();
    }

    @Test
    void testGetEmployerById_Found() {
        // Arrange
        Long employerId = 1L;
        Employer expectedEmployer = new Employer();
        expectedEmployer.setId(employerId);
        expectedEmployer.setName("Test Company");

        when(employerDao.findById(employerId)).thenReturn(Optional.of(expectedEmployer));

        // Act
        Optional<Employer> actualEmployer = employerService.getEmployerById(employerId);

        // Assert
        assertThat(actualEmployer).isPresent();
        assertThat(actualEmployer.get().getId()).isEqualTo(employerId);
        assertThat(actualEmployer.get().getName()).isEqualTo("Test Company");
        verify(employerDao, times(1)).findById(employerId);
    }

    @Test
    void testGetEmployerById_NotFound() {
        // Arrange
        Long employerId = 999L;
        when(employerDao.findById(employerId)).thenReturn(Optional.empty());

        // Act
        Optional<Employer> actualEmployer = employerService.getEmployerById(employerId);

        // Assert
        assertThat(actualEmployer).isEmpty();
        verify(employerDao, times(1)).findById(employerId);
    }

    @Test
    void testCreateEmployer() {
        // Arrange
        Employer newEmployer = new Employer();
        newEmployer.setName("New Company");

        Long expectedId = 123L;
        when(employerDao.insert(newEmployer)).thenReturn(expectedId);

        // Act
        Long actualId = employerService.createEmployer(newEmployer);

        // Assert
        assertThat(actualId).isEqualTo(expectedId);
        verify(employerDao, times(1)).insert(newEmployer);
    }

    @Test
    void testUpdateEmployer_Success() {
        // Arrange
        Employer employer = new Employer();
        employer.setId(1L);
        employer.setName("Updated Company");

        when(employerDao.update(eq(1L), eq(employer))).thenReturn(true);

        // Act
        boolean result = employerService.updateEmployer(employer);

        // Assert
        assertThat(result).isTrue();
        verify(employerDao, times(1)).update(1L, employer);
    }

    @Test
    void testUpdateEmployer_Failure() {
        // Arrange
        Employer employer = new Employer();
        employer.setId(999L);
        employer.setName("Non-existent Company");

        when(employerDao.update(eq(999L), eq(employer))).thenReturn(false);

        // Act
        boolean result = employerService.updateEmployer(employer);

        // Assert
        assertThat(result).isFalse();
        verify(employerDao, times(1)).update(999L, employer);
    }

    @Test
    void testDeleteEmployer_Success() {
        // Arrange
        Long employerId = 1L;
        when(employerDao.delete(employerId)).thenReturn(true);

        // Act
        boolean result = employerService.deleteEmployer(employerId);

        // Assert
        assertThat(result).isTrue();
        verify(employerDao, times(1)).delete(employerId);
    }

    @Test
    void testDeleteEmployer_Failure() {
        // Arrange
        Long employerId = 999L;
        when(employerDao.delete(employerId)).thenReturn(false);

        // Act
        boolean result = employerService.deleteEmployer(employerId);

        // Assert
        assertThat(result).isFalse();
        verify(employerDao, times(1)).delete(employerId);
    }
}

