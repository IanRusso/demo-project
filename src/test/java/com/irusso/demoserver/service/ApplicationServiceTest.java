package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.ApplicationDao;
import com.irusso.demoserver.db.model.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationDao applicationDao;

    private ApplicationService applicationService;

    @BeforeEach
    void setUp() {
        applicationService = new ApplicationService(applicationDao);
    }

    @Test
    void testGetAllApplications() {
        // Arrange
        Application app1 = new Application();
        app1.setId(1L);
        app1.setUserId(100L);
        app1.setJobPostingId(200L);

        Application app2 = new Application();
        app2.setId(2L);
        app2.setUserId(101L);
        app2.setJobPostingId(201L);

        List<Application> expectedApplications = Arrays.asList(app1, app2);
        when(applicationDao.findAll()).thenReturn(expectedApplications);

        // Act
        List<Application> actualApplications = applicationService.getAllApplications();

        // Assert
        assertThat(actualApplications).hasSize(2);
        assertThat(actualApplications).containsExactlyElementsOf(expectedApplications);
        verify(applicationDao, times(1)).findAll();
    }

    @Test
    void testGetApplicationById_Found() {
        // Arrange
        Application application = new Application();
        application.setId(1L);
        application.setUserId(100L);
        application.setStatus("pending");

        when(applicationDao.findById(1L)).thenReturn(Optional.of(application));

        // Act
        Optional<Application> result = applicationService.getApplicationById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getStatus()).isEqualTo("pending");
        verify(applicationDao, times(1)).findById(1L);
    }

    @Test
    void testGetApplicationById_NotFound() {
        // Arrange
        when(applicationDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Application> result = applicationService.getApplicationById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(applicationDao, times(1)).findById(999L);
    }

    @Test
    void testCreateApplication() {
        // Arrange
        Application newApplication = new Application();
        newApplication.setUserId(100L);
        newApplication.setJobPostingId(200L);
        newApplication.setCoverLetter("I am interested in this position");

        when(applicationDao.insert(newApplication)).thenReturn(1L);

        // Act
        Long applicationId = applicationService.createApplication(newApplication);

        // Assert
        assertThat(applicationId).isEqualTo(1L);
        verify(applicationDao, times(1)).insert(newApplication);
    }

    @Test
    void testUpdateApplication_Success() {
        // Arrange
        Application application = new Application();
        application.setId(1L);
        application.setStatus("reviewed");

        when(applicationDao.update(1L, application)).thenReturn(true);

        // Act
        boolean result = applicationService.updateApplication(application);

        // Assert
        assertThat(result).isTrue();
        verify(applicationDao, times(1)).update(1L, application);
    }

    @Test
    void testUpdateApplication_Failure() {
        // Arrange
        Application application = new Application();
        application.setId(999L);
        application.setStatus("rejected");

        when(applicationDao.update(999L, application)).thenReturn(false);

        // Act
        boolean result = applicationService.updateApplication(application);

        // Assert
        assertThat(result).isFalse();
        verify(applicationDao, times(1)).update(999L, application);
    }

    @Test
    void testDeleteApplication_Success() {
        // Arrange
        when(applicationDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = applicationService.deleteApplication(1L);

        // Assert
        assertThat(result).isTrue();
        verify(applicationDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteApplication_Failure() {
        // Arrange
        when(applicationDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = applicationService.deleteApplication(999L);

        // Assert
        assertThat(result).isFalse();
        verify(applicationDao, times(1)).delete(999L);
    }

    @Test
    void testFindByUserId() {
        // Arrange
        Application app1 = new Application();
        app1.setId(1L);
        app1.setUserId(100L);

        Application app2 = new Application();
        app2.setId(2L);
        app2.setUserId(100L);

        List<Application> expectedApplications = Arrays.asList(app1, app2);
        when(applicationDao.findByUserId(100L)).thenReturn(expectedApplications);

        // Act
        List<Application> actualApplications = applicationService.findByUserId(100L);

        // Assert
        assertThat(actualApplications).hasSize(2);
        assertThat(actualApplications).containsExactlyElementsOf(expectedApplications);
        verify(applicationDao, times(1)).findByUserId(100L);
    }

    @Test
    void testFindByJobPostingId() {
        // Arrange
        Application app1 = new Application();
        app1.setId(1L);
        app1.setJobPostingId(200L);

        Application app2 = new Application();
        app2.setId(2L);
        app2.setJobPostingId(200L);

        List<Application> expectedApplications = Arrays.asList(app1, app2);
        when(applicationDao.findByJobPostingId(200L)).thenReturn(expectedApplications);

        // Act
        List<Application> actualApplications = applicationService.findByJobPostingId(200L);

        // Assert
        assertThat(actualApplications).hasSize(2);
        assertThat(actualApplications).containsExactlyElementsOf(expectedApplications);
        verify(applicationDao, times(1)).findByJobPostingId(200L);
    }

    @Test
    void testFindByStatus() {
        // Arrange
        Application app1 = new Application();
        app1.setId(1L);
        app1.setStatus("pending");

        Application app2 = new Application();
        app2.setId(2L);
        app2.setStatus("pending");

        List<Application> expectedApplications = Arrays.asList(app1, app2);
        when(applicationDao.findByStatus("pending")).thenReturn(expectedApplications);

        // Act
        List<Application> actualApplications = applicationService.findByStatus("pending");

        // Assert
        assertThat(actualApplications).hasSize(2);
        assertThat(actualApplications).containsExactlyElementsOf(expectedApplications);
        verify(applicationDao, times(1)).findByStatus("pending");
    }
}

