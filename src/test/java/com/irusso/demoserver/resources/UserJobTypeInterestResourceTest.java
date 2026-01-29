package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserJobTypeInterest;
import com.irusso.demoserver.service.UserJobTypeInterestService;
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
class UserJobTypeInterestResourceTest {

    @Mock
    private UserJobTypeInterestService userJobTypeInterestService;

    private UserJobTypeInterestResource userJobTypeInterestResource;

    @BeforeEach
    void setUp() {
        userJobTypeInterestResource = new UserJobTypeInterestResource(userJobTypeInterestService);
    }

    @Test
    void testGetAllUserJobTypeInterests() {
        // Arrange
        UserJobTypeInterest jobType1 = new UserJobTypeInterest();
        jobType1.setId(1L);
        jobType1.setUserId(100L);

        UserJobTypeInterest jobType2 = new UserJobTypeInterest();
        jobType2.setId(2L);
        jobType2.setUserId(200L);

        List<UserJobTypeInterest> jobTypes = Arrays.asList(jobType1, jobType2);
        when(userJobTypeInterestService.getAllUserJobTypeInterests()).thenReturn(jobTypes);

        // Act
        Response response = userJobTypeInterestResource.getAllUserJobTypeInterests();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<UserJobTypeInterest>> apiResponse = (ApiResponse<List<UserJobTypeInterest>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userJobTypeInterestService, times(1)).getAllUserJobTypeInterests();
    }

    @Test
    void testGetUserJobTypeInterest_Found() {
        // Arrange
        Long jobTypeId = 1L;
        UserJobTypeInterest jobType = new UserJobTypeInterest();
        jobType.setId(jobTypeId);
        jobType.setUserId(100L);

        when(userJobTypeInterestService.getUserJobTypeInterestById(jobTypeId)).thenReturn(Optional.of(jobType));

        // Act
        Response response = userJobTypeInterestResource.getUserJobTypeInterest(jobTypeId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserJobTypeInterest> apiResponse = (ApiResponse<UserJobTypeInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(jobTypeId);
        verify(userJobTypeInterestService, times(1)).getUserJobTypeInterestById(jobTypeId);
    }

    @Test
    void testGetUserJobTypeInterest_NotFound() {
        // Arrange
        Long jobTypeId = 999L;
        when(userJobTypeInterestService.getUserJobTypeInterestById(jobTypeId)).thenReturn(Optional.empty());

        // Act
        Response response = userJobTypeInterestResource.getUserJobTypeInterest(jobTypeId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserJobTypeInterest> apiResponse = (ApiResponse<UserJobTypeInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest not found");
        verify(userJobTypeInterestService, times(1)).getUserJobTypeInterestById(jobTypeId);
    }

    @Test
    void testCreateUserJobTypeInterest() {
        // Arrange
        UserJobTypeInterest newJobType = new UserJobTypeInterest();
        newJobType.setUserId(100L);
        newJobType.setJobType("Full-time");

        Long generatedId = 123L;
        when(userJobTypeInterestService.createUserJobTypeInterest(newJobType)).thenReturn(generatedId);

        // Act
        Response response = userJobTypeInterestResource.createUserJobTypeInterest(newJobType);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<UserJobTypeInterest> apiResponse = (ApiResponse<UserJobTypeInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(userJobTypeInterestService, times(1)).createUserJobTypeInterest(newJobType);
    }

    @Test
    void testUpdateUserJobTypeInterest_Success() {
        // Arrange
        Long jobTypeId = 1L;
        UserJobTypeInterest existingJobType = new UserJobTypeInterest();
        existingJobType.setId(jobTypeId);
        existingJobType.setUserId(100L);

        UserJobTypeInterest updatedJobType = new UserJobTypeInterest();
        updatedJobType.setJobType("Part-time");

        when(userJobTypeInterestService.getUserJobTypeInterestById(jobTypeId)).thenReturn(Optional.of(existingJobType));
        when(userJobTypeInterestService.updateUserJobTypeInterest(any(UserJobTypeInterest.class))).thenReturn(true);

        // Act
        Response response = userJobTypeInterestResource.updateUserJobTypeInterest(jobTypeId, updatedJobType);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserJobTypeInterest> apiResponse = (ApiResponse<UserJobTypeInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(jobTypeId);
        verify(userJobTypeInterestService, times(1)).getUserJobTypeInterestById(jobTypeId);
        verify(userJobTypeInterestService, times(1)).updateUserJobTypeInterest(any(UserJobTypeInterest.class));
    }

    @Test
    void testUpdateUserJobTypeInterest_NotFound() {
        // Arrange
        Long jobTypeId = 999L;
        UserJobTypeInterest updatedJobType = new UserJobTypeInterest();
        updatedJobType.setJobType("Part-time");

        when(userJobTypeInterestService.getUserJobTypeInterestById(jobTypeId)).thenReturn(Optional.empty());

        // Act
        Response response = userJobTypeInterestResource.updateUserJobTypeInterest(jobTypeId, updatedJobType);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserJobTypeInterest> apiResponse = (ApiResponse<UserJobTypeInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest not found");
        verify(userJobTypeInterestService, times(1)).getUserJobTypeInterestById(jobTypeId);
        verify(userJobTypeInterestService, never()).updateUserJobTypeInterest(any(UserJobTypeInterest.class));
    }

    @Test
    void testDeleteUserJobTypeInterest_Success() {
        // Arrange
        Long jobTypeId = 1L;
        when(userJobTypeInterestService.deleteUserJobTypeInterest(jobTypeId)).thenReturn(true);

        // Act
        Response response = userJobTypeInterestResource.deleteUserJobTypeInterest(jobTypeId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest deleted successfully");
        verify(userJobTypeInterestService, times(1)).deleteUserJobTypeInterest(jobTypeId);
    }

    @Test
    void testDeleteUserJobTypeInterest_NotFound() {
        // Arrange
        Long jobTypeId = 999L;
        when(userJobTypeInterestService.deleteUserJobTypeInterest(jobTypeId)).thenReturn(false);

        // Act
        Response response = userJobTypeInterestResource.deleteUserJobTypeInterest(jobTypeId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User job type interest not found");
        verify(userJobTypeInterestService, times(1)).deleteUserJobTypeInterest(jobTypeId);
    }
}

