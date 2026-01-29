package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.JobRequirement;
import com.irusso.demoserver.service.JobRequirementService;
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
class JobRequirementResourceTest {

    @Mock
    private JobRequirementService jobRequirementService;

    private JobRequirementResource jobRequirementResource;

    @BeforeEach
    void setUp() {
        jobRequirementResource = new JobRequirementResource(jobRequirementService);
    }

    @Test
    void testGetAllJobRequirements() {
        // Arrange
        JobRequirement req1 = new JobRequirement();
        req1.setId(1L);
        req1.setRequirementType("hard");

        JobRequirement req2 = new JobRequirement();
        req2.setId(2L);
        req2.setRequirementType("soft");

        List<JobRequirement> requirements = Arrays.asList(req1, req2);
        when(jobRequirementService.getAllJobRequirements()).thenReturn(requirements);

        // Act
        Response response = jobRequirementResource.getAllJobRequirements();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<JobRequirement>> apiResponse = (ApiResponse<List<JobRequirement>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(jobRequirementService, times(1)).getAllJobRequirements();
    }

    @Test
    void testGetJobRequirement_Found() {
        // Arrange
        Long reqId = 1L;
        JobRequirement requirement = new JobRequirement();
        requirement.setId(reqId);
        requirement.setRequirementType("hard");

        when(jobRequirementService.getJobRequirementById(reqId)).thenReturn(Optional.of(requirement));

        // Act
        Response response = jobRequirementResource.getJobRequirement(reqId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<JobRequirement> apiResponse = (ApiResponse<JobRequirement>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(reqId);
        verify(jobRequirementService, times(1)).getJobRequirementById(reqId);
    }

    @Test
    void testGetJobRequirement_NotFound() {
        // Arrange
        Long reqId = 999L;
        when(jobRequirementService.getJobRequirementById(reqId)).thenReturn(Optional.empty());

        // Act
        Response response = jobRequirementResource.getJobRequirement(reqId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<JobRequirement> apiResponse = (ApiResponse<JobRequirement>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement not found");
        verify(jobRequirementService, times(1)).getJobRequirementById(reqId);
    }

    @Test
    void testCreateJobRequirement() {
        // Arrange
        JobRequirement newRequirement = new JobRequirement();
        newRequirement.setRequirementType("hard");

        Long generatedId = 123L;
        when(jobRequirementService.createJobRequirement(newRequirement)).thenReturn(generatedId);

        // Act
        Response response = jobRequirementResource.createJobRequirement(newRequirement);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<JobRequirement> apiResponse = (ApiResponse<JobRequirement>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(jobRequirementService, times(1)).createJobRequirement(newRequirement);
    }

    @Test
    void testUpdateJobRequirement_Success() {
        // Arrange
        Long reqId = 1L;
        JobRequirement existingRequirement = new JobRequirement();
        existingRequirement.setId(reqId);
        existingRequirement.setRequirementType("hard");

        JobRequirement updatedRequirement = new JobRequirement();
        updatedRequirement.setRequirementType("soft");

        when(jobRequirementService.getJobRequirementById(reqId)).thenReturn(Optional.of(existingRequirement));
        when(jobRequirementService.updateJobRequirement(any(JobRequirement.class))).thenReturn(true);

        // Act
        Response response = jobRequirementResource.updateJobRequirement(reqId, updatedRequirement);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<JobRequirement> apiResponse = (ApiResponse<JobRequirement>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(reqId);
        verify(jobRequirementService, times(1)).getJobRequirementById(reqId);
        verify(jobRequirementService, times(1)).updateJobRequirement(any(JobRequirement.class));
    }

    @Test
    void testUpdateJobRequirement_NotFound() {
        // Arrange
        Long reqId = 999L;
        JobRequirement updatedRequirement = new JobRequirement();
        updatedRequirement.setRequirementType("soft");

        when(jobRequirementService.getJobRequirementById(reqId)).thenReturn(Optional.empty());

        // Act
        Response response = jobRequirementResource.updateJobRequirement(reqId, updatedRequirement);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<JobRequirement> apiResponse = (ApiResponse<JobRequirement>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement not found");
        verify(jobRequirementService, times(1)).getJobRequirementById(reqId);
        verify(jobRequirementService, never()).updateJobRequirement(any(JobRequirement.class));
    }

    @Test
    void testDeleteJobRequirement_Success() {
        // Arrange
        Long reqId = 1L;
        when(jobRequirementService.deleteJobRequirement(reqId)).thenReturn(true);

        // Act
        Response response = jobRequirementResource.deleteJobRequirement(reqId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement deleted successfully");
        verify(jobRequirementService, times(1)).deleteJobRequirement(reqId);
    }

    @Test
    void testDeleteJobRequirement_NotFound() {
        // Arrange
        Long reqId = 999L;
        when(jobRequirementService.deleteJobRequirement(reqId)).thenReturn(false);

        // Act
        Response response = jobRequirementResource.deleteJobRequirement(reqId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job requirement not found");
        verify(jobRequirementService, times(1)).deleteJobRequirement(reqId);
    }
}

