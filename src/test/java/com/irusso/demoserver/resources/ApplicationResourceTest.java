package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.Application;
import com.irusso.demoserver.service.ApplicationService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationResourceTest {

    @Mock
    private ApplicationService applicationService;

    private ApplicationResource applicationResource;

    @BeforeEach
    void setUp() {
        applicationResource = new ApplicationResource(applicationService);
    }

    @Test
    void testGetAllApplications() {
        // Arrange
        Application app1 = new Application();
        app1.setId(1L);
        app1.setUserId(100L);

        Application app2 = new Application();
        app2.setId(2L);
        app2.setUserId(200L);

        List<Application> applications = Arrays.asList(app1, app2);
        when(applicationService.getAllApplications()).thenReturn(applications);

        // Act
        Response response = applicationResource.getAllApplications();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<Application>> apiResponse = (ApiResponse<List<Application>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(applicationService, times(1)).getAllApplications();
    }

    @Test
    void testGetApplication_Found() {
        // Arrange
        Long appId = 1L;
        Application application = new Application();
        application.setId(appId);
        application.setUserId(100L);

        when(applicationService.getApplicationById(appId)).thenReturn(Optional.of(application));

        // Act
        Response response = applicationResource.getApplication(appId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Application> apiResponse = (ApiResponse<Application>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(appId);
        verify(applicationService, times(1)).getApplicationById(appId);
    }

    @Test
    void testGetApplication_NotFound() {
        // Arrange
        Long appId = 999L;
        when(applicationService.getApplicationById(appId)).thenReturn(Optional.empty());

        // Act
        Response response = applicationResource.getApplication(appId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Application> apiResponse = (ApiResponse<Application>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application not found");
        verify(applicationService, times(1)).getApplicationById(appId);
    }

    @Test
    void testCreateApplication() {
        // Arrange
        Application newApplication = new Application();
        newApplication.setUserId(100L);
        newApplication.setJobPostingId(200L);

        Long generatedId = 123L;
        when(applicationService.createApplication(newApplication)).thenReturn(generatedId);

        // Act
        Response response = applicationResource.createApplication(newApplication);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<Application> apiResponse = (ApiResponse<Application>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(applicationService, times(1)).createApplication(newApplication);
    }

    @Test
    void testUpdateApplication_Success() {
        // Arrange
        Long appId = 1L;
        Application existingApplication = new Application();
        existingApplication.setId(appId);
        existingApplication.setUserId(100L);

        Application updatedApplication = new Application();
        updatedApplication.setStatus("accepted");

        when(applicationService.getApplicationById(appId)).thenReturn(Optional.of(existingApplication));
        when(applicationService.updateApplication(any(Application.class))).thenReturn(true);

        // Act
        Response response = applicationResource.updateApplication(appId, updatedApplication);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Application> apiResponse = (ApiResponse<Application>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(appId);
        verify(applicationService, times(1)).getApplicationById(appId);
        verify(applicationService, times(1)).updateApplication(any(Application.class));
    }

    @Test
    void testUpdateApplication_NotFound() {
        // Arrange
        Long appId = 999L;
        Application updatedApplication = new Application();
        updatedApplication.setStatus("accepted");

        when(applicationService.getApplicationById(appId)).thenReturn(Optional.empty());

        // Act
        Response response = applicationResource.updateApplication(appId, updatedApplication);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Application> apiResponse = (ApiResponse<Application>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application not found");
        verify(applicationService, times(1)).getApplicationById(appId);
        verify(applicationService, never()).updateApplication(any(Application.class));
    }

    @Test
    void testDeleteApplication_Success() {
        // Arrange
        Long appId = 1L;
        when(applicationService.deleteApplication(appId)).thenReturn(true);

        // Act
        Response response = applicationResource.deleteApplication(appId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application deleted successfully");
        verify(applicationService, times(1)).deleteApplication(appId);
    }

    @Test
    void testDeleteApplication_NotFound() {
        // Arrange
        Long appId = 999L;
        when(applicationService.deleteApplication(appId)).thenReturn(false);

        // Act
        Response response = applicationResource.deleteApplication(appId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application not found");
        verify(applicationService, times(1)).deleteApplication(appId);
    }
}

