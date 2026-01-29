package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.SavedJob;
import com.irusso.demoserver.service.SavedJobService;
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
class SavedJobResourceTest {

    @Mock
    private SavedJobService savedJobService;

    private SavedJobResource savedJobResource;

    @BeforeEach
    void setUp() {
        savedJobResource = new SavedJobResource(savedJobService);
    }

    @Test
    void testGetAllSavedJobs() {
        // Arrange
        SavedJob saved1 = new SavedJob();
        saved1.setId(1L);
        saved1.setUserId(100L);

        SavedJob saved2 = new SavedJob();
        saved2.setId(2L);
        saved2.setUserId(200L);

        List<SavedJob> savedJobs = Arrays.asList(saved1, saved2);
        when(savedJobService.getAllSavedJobs()).thenReturn(savedJobs);

        // Act
        Response response = savedJobResource.getAllSavedJobs();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<SavedJob>> apiResponse = (ApiResponse<List<SavedJob>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(savedJobService, times(1)).getAllSavedJobs();
    }

    @Test
    void testGetSavedJob_Found() {
        // Arrange
        Long savedId = 1L;
        SavedJob savedJob = new SavedJob();
        savedJob.setId(savedId);
        savedJob.setUserId(100L);

        when(savedJobService.getSavedJobById(savedId)).thenReturn(Optional.of(savedJob));

        // Act
        Response response = savedJobResource.getSavedJob(savedId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<SavedJob> apiResponse = (ApiResponse<SavedJob>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(savedId);
        verify(savedJobService, times(1)).getSavedJobById(savedId);
    }

    @Test
    void testGetSavedJob_NotFound() {
        // Arrange
        Long savedId = 999L;
        when(savedJobService.getSavedJobById(savedId)).thenReturn(Optional.empty());

        // Act
        Response response = savedJobResource.getSavedJob(savedId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<SavedJob> apiResponse = (ApiResponse<SavedJob>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job not found");
        verify(savedJobService, times(1)).getSavedJobById(savedId);
    }

    @Test
    void testCreateSavedJob() {
        // Arrange
        SavedJob newSavedJob = new SavedJob();
        newSavedJob.setUserId(100L);
        newSavedJob.setJobPostingId(200L);

        Long generatedId = 123L;
        when(savedJobService.createSavedJob(newSavedJob)).thenReturn(generatedId);

        // Act
        Response response = savedJobResource.createSavedJob(newSavedJob);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<SavedJob> apiResponse = (ApiResponse<SavedJob>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(savedJobService, times(1)).createSavedJob(newSavedJob);
    }

    @Test
    void testUpdateSavedJob_Success() {
        // Arrange
        Long savedId = 1L;
        SavedJob existingSavedJob = new SavedJob();
        existingSavedJob.setId(savedId);
        existingSavedJob.setUserId(100L);

        SavedJob updatedSavedJob = new SavedJob();
        updatedSavedJob.setUserId(200L);

        when(savedJobService.getSavedJobById(savedId)).thenReturn(Optional.of(existingSavedJob));
        when(savedJobService.updateSavedJob(any(SavedJob.class))).thenReturn(true);

        // Act
        Response response = savedJobResource.updateSavedJob(savedId, updatedSavedJob);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<SavedJob> apiResponse = (ApiResponse<SavedJob>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(savedId);
        verify(savedJobService, times(1)).getSavedJobById(savedId);
        verify(savedJobService, times(1)).updateSavedJob(any(SavedJob.class));
    }

    @Test
    void testUpdateSavedJob_NotFound() {
        // Arrange
        Long savedId = 999L;
        SavedJob updatedSavedJob = new SavedJob();
        updatedSavedJob.setUserId(200L);

        when(savedJobService.getSavedJobById(savedId)).thenReturn(Optional.empty());

        // Act
        Response response = savedJobResource.updateSavedJob(savedId, updatedSavedJob);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<SavedJob> apiResponse = (ApiResponse<SavedJob>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job not found");
        verify(savedJobService, times(1)).getSavedJobById(savedId);
        verify(savedJobService, never()).updateSavedJob(any(SavedJob.class));
    }

    @Test
    void testDeleteSavedJob_Success() {
        // Arrange
        Long savedId = 1L;
        when(savedJobService.deleteSavedJob(savedId)).thenReturn(true);

        // Act
        Response response = savedJobResource.deleteSavedJob(savedId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job deleted successfully");
        verify(savedJobService, times(1)).deleteSavedJob(savedId);
    }

    @Test
    void testDeleteSavedJob_NotFound() {
        // Arrange
        Long savedId = 999L;
        when(savedJobService.deleteSavedJob(savedId)).thenReturn(false);

        // Act
        Response response = savedJobResource.deleteSavedJob(savedId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Saved job not found");
        verify(savedJobService, times(1)).deleteSavedJob(savedId);
    }
}

