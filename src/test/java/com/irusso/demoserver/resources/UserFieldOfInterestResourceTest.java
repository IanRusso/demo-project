package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserFieldOfInterest;
import com.irusso.demoserver.service.UserFieldOfInterestService;
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
class UserFieldOfInterestResourceTest {

    @Mock
    private UserFieldOfInterestService userFieldOfInterestService;

    private UserFieldOfInterestResource userFieldOfInterestResource;

    @BeforeEach
    void setUp() {
        userFieldOfInterestResource = new UserFieldOfInterestResource(userFieldOfInterestService);
    }

    @Test
    void testGetAllUserFieldsOfInterest() {
        // Arrange
        UserFieldOfInterest field1 = new UserFieldOfInterest();
        field1.setId(1L);
        field1.setUserId(100L);

        UserFieldOfInterest field2 = new UserFieldOfInterest();
        field2.setId(2L);
        field2.setUserId(200L);

        List<UserFieldOfInterest> fields = Arrays.asList(field1, field2);
        when(userFieldOfInterestService.getAllUserFieldsOfInterest()).thenReturn(fields);

        // Act
        Response response = userFieldOfInterestResource.getAllUserFieldsOfInterest();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<UserFieldOfInterest>> apiResponse = (ApiResponse<List<UserFieldOfInterest>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userFieldOfInterestService, times(1)).getAllUserFieldsOfInterest();
    }

    @Test
    void testGetUserFieldOfInterest_Found() {
        // Arrange
        Long fieldId = 1L;
        UserFieldOfInterest field = new UserFieldOfInterest();
        field.setId(fieldId);
        field.setUserId(100L);

        when(userFieldOfInterestService.getUserFieldOfInterestById(fieldId)).thenReturn(Optional.of(field));

        // Act
        Response response = userFieldOfInterestResource.getUserFieldOfInterest(fieldId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserFieldOfInterest> apiResponse = (ApiResponse<UserFieldOfInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(fieldId);
        verify(userFieldOfInterestService, times(1)).getUserFieldOfInterestById(fieldId);
    }

    @Test
    void testGetUserFieldOfInterest_NotFound() {
        // Arrange
        Long fieldId = 999L;
        when(userFieldOfInterestService.getUserFieldOfInterestById(fieldId)).thenReturn(Optional.empty());

        // Act
        Response response = userFieldOfInterestResource.getUserFieldOfInterest(fieldId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserFieldOfInterest> apiResponse = (ApiResponse<UserFieldOfInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest not found");
        verify(userFieldOfInterestService, times(1)).getUserFieldOfInterestById(fieldId);
    }

    @Test
    void testCreateUserFieldOfInterest() {
        // Arrange
        UserFieldOfInterest newField = new UserFieldOfInterest();
        newField.setUserId(100L);
        newField.setFieldName("Technology");

        Long generatedId = 123L;
        when(userFieldOfInterestService.createUserFieldOfInterest(newField)).thenReturn(generatedId);

        // Act
        Response response = userFieldOfInterestResource.createUserFieldOfInterest(newField);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<UserFieldOfInterest> apiResponse = (ApiResponse<UserFieldOfInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(userFieldOfInterestService, times(1)).createUserFieldOfInterest(newField);
    }

    @Test
    void testUpdateUserFieldOfInterest_Success() {
        // Arrange
        Long fieldId = 1L;
        UserFieldOfInterest existingField = new UserFieldOfInterest();
        existingField.setId(fieldId);
        existingField.setUserId(100L);

        UserFieldOfInterest updatedField = new UserFieldOfInterest();
        updatedField.setFieldName("Healthcare");

        when(userFieldOfInterestService.getUserFieldOfInterestById(fieldId)).thenReturn(Optional.of(existingField));
        when(userFieldOfInterestService.updateUserFieldOfInterest(any(UserFieldOfInterest.class))).thenReturn(true);

        // Act
        Response response = userFieldOfInterestResource.updateUserFieldOfInterest(fieldId, updatedField);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserFieldOfInterest> apiResponse = (ApiResponse<UserFieldOfInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(fieldId);
        verify(userFieldOfInterestService, times(1)).getUserFieldOfInterestById(fieldId);
        verify(userFieldOfInterestService, times(1)).updateUserFieldOfInterest(any(UserFieldOfInterest.class));
    }

    @Test
    void testUpdateUserFieldOfInterest_NotFound() {
        // Arrange
        Long fieldId = 999L;
        UserFieldOfInterest updatedField = new UserFieldOfInterest();
        updatedField.setFieldName("Healthcare");

        when(userFieldOfInterestService.getUserFieldOfInterestById(fieldId)).thenReturn(Optional.empty());

        // Act
        Response response = userFieldOfInterestResource.updateUserFieldOfInterest(fieldId, updatedField);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserFieldOfInterest> apiResponse = (ApiResponse<UserFieldOfInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest not found");
        verify(userFieldOfInterestService, times(1)).getUserFieldOfInterestById(fieldId);
        verify(userFieldOfInterestService, never()).updateUserFieldOfInterest(any(UserFieldOfInterest.class));
    }

    @Test
    void testDeleteUserFieldOfInterest_Success() {
        // Arrange
        Long fieldId = 1L;
        when(userFieldOfInterestService.deleteUserFieldOfInterest(fieldId)).thenReturn(true);

        // Act
        Response response = userFieldOfInterestResource.deleteUserFieldOfInterest(fieldId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest deleted successfully");
        verify(userFieldOfInterestService, times(1)).deleteUserFieldOfInterest(fieldId);
    }

    @Test
    void testDeleteUserFieldOfInterest_NotFound() {
        // Arrange
        Long fieldId = 999L;
        when(userFieldOfInterestService.deleteUserFieldOfInterest(fieldId)).thenReturn(false);

        // Act
        Response response = userFieldOfInterestResource.deleteUserFieldOfInterest(fieldId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User field of interest not found");
        verify(userFieldOfInterestService, times(1)).deleteUserFieldOfInterest(fieldId);
    }
}

