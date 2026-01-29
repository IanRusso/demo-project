package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.JobPosting;
import com.irusso.demoserver.service.JobPostingService;
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
class JobPostingResourceTest {

    @Mock
    private JobPostingService jobPostingService;

    private JobPostingResource jobPostingResource;

    @BeforeEach
    void setUp() {
        jobPostingResource = new JobPostingResource(jobPostingService);
    }

    @Test
    void testGetAllJobPostings() {
        // Arrange
        JobPosting job1 = new JobPosting();
        job1.setId(1L);
        job1.setTitle("Software Engineer");

        JobPosting job2 = new JobPosting();
        job2.setId(2L);
        job2.setTitle("Data Scientist");

        List<JobPosting> jobPostings = Arrays.asList(job1, job2);
        when(jobPostingService.getAllJobPostings()).thenReturn(jobPostings);

        // Act
        Response response = jobPostingResource.getAllJobPostings();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<JobPosting>> apiResponse = (ApiResponse<List<JobPosting>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(jobPostingService, times(1)).getAllJobPostings();
    }

    @Test
    void testGetJobPosting_Found() {
        // Arrange
        Long jobId = 1L;
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(jobId);
        jobPosting.setTitle("Software Engineer");

        when(jobPostingService.getJobPostingById(jobId)).thenReturn(Optional.of(jobPosting));

        // Act
        Response response = jobPostingResource.getJobPosting(jobId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<JobPosting> apiResponse = (ApiResponse<JobPosting>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(jobId);
        verify(jobPostingService, times(1)).getJobPostingById(jobId);
    }

    @Test
    void testGetJobPosting_NotFound() {
        // Arrange
        Long jobId = 999L;
        when(jobPostingService.getJobPostingById(jobId)).thenReturn(Optional.empty());

        // Act
        Response response = jobPostingResource.getJobPosting(jobId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<JobPosting> apiResponse = (ApiResponse<JobPosting>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting not found");
        verify(jobPostingService, times(1)).getJobPostingById(jobId);
    }

    @Test
    void testCreateJobPosting() {
        // Arrange
        JobPosting newJobPosting = new JobPosting();
        newJobPosting.setTitle("New Job");

        Long generatedId = 123L;
        when(jobPostingService.createJobPosting(newJobPosting)).thenReturn(generatedId);

        // Act
        Response response = jobPostingResource.createJobPosting(newJobPosting);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<JobPosting> apiResponse = (ApiResponse<JobPosting>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(jobPostingService, times(1)).createJobPosting(newJobPosting);
    }

    @Test
    void testUpdateJobPosting_Success() {
        // Arrange
        Long jobId = 1L;
        JobPosting existingJobPosting = new JobPosting();
        existingJobPosting.setId(jobId);
        existingJobPosting.setTitle("Old Job");

        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setTitle("Updated Job");

        when(jobPostingService.getJobPostingById(jobId)).thenReturn(Optional.of(existingJobPosting));
        when(jobPostingService.updateJobPosting(any(JobPosting.class))).thenReturn(true);

        // Act
        Response response = jobPostingResource.updateJobPosting(jobId, updatedJobPosting);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<JobPosting> apiResponse = (ApiResponse<JobPosting>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(jobId);
        verify(jobPostingService, times(1)).getJobPostingById(jobId);
        verify(jobPostingService, times(1)).updateJobPosting(any(JobPosting.class));
    }

    @Test
    void testUpdateJobPosting_NotFound() {
        // Arrange
        Long jobId = 999L;
        JobPosting updatedJobPosting = new JobPosting();
        updatedJobPosting.setTitle("Updated Job");

        when(jobPostingService.getJobPostingById(jobId)).thenReturn(Optional.empty());

        // Act
        Response response = jobPostingResource.updateJobPosting(jobId, updatedJobPosting);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<JobPosting> apiResponse = (ApiResponse<JobPosting>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting not found");
        verify(jobPostingService, times(1)).getJobPostingById(jobId);
        verify(jobPostingService, never()).updateJobPosting(any(JobPosting.class));
    }

    @Test
    void testDeleteJobPosting_Success() {
        // Arrange
        Long jobId = 1L;
        when(jobPostingService.deleteJobPosting(jobId)).thenReturn(true);

        // Act
        Response response = jobPostingResource.deleteJobPosting(jobId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting deleted successfully");
        verify(jobPostingService, times(1)).deleteJobPosting(jobId);
    }

    @Test
    void testDeleteJobPosting_NotFound() {
        // Arrange
        Long jobId = 999L;
        when(jobPostingService.deleteJobPosting(jobId)).thenReturn(false);

        // Act
        Response response = jobPostingResource.deleteJobPosting(jobId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Job posting not found");
        verify(jobPostingService, times(1)).deleteJobPosting(jobId);
    }
}

