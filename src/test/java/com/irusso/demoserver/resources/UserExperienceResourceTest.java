package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserExperience;
import com.irusso.demoserver.service.UserExperienceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserExperienceResource.
 */
@ExtendWith(MockitoExtension.class)
class UserExperienceResourceTest {

    @Mock
    private UserExperienceService userExperienceService;

    private UserExperienceResource userExperienceResource;

    @BeforeEach
    void setUp() {
        userExperienceResource = new UserExperienceResource(userExperienceService);
    }

    @Test
    void testGetAllUserExperiences() {
        // Arrange
        UserExperience exp1 = createTestExperience(1L, "Experience 1");
        UserExperience exp2 = createTestExperience(2L, "Experience 2");
        List<UserExperience> experiences = Arrays.asList(exp1, exp2);
        when(userExperienceService.getAllUserExperiences()).thenReturn(experiences);

        // Act
        Response response = userExperienceResource.getAllUserExperiences();

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<List<UserExperience>> apiResponse = (ApiResponse<List<UserExperience>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userExperienceService).getAllUserExperiences();
    }

    @Test
    void testGetAllUserExperiencesEmpty() {
        // Arrange
        when(userExperienceService.getAllUserExperiences()).thenReturn(Collections.emptyList());

        // Act
        Response response = userExperienceResource.getAllUserExperiences();

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<List<UserExperience>> apiResponse = (ApiResponse<List<UserExperience>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).isEmpty();
        verify(userExperienceService).getAllUserExperiences();
    }

    @Test
    void testGetUserExperienceById() {
        // Arrange
        Long id = 1L;
        UserExperience experience = createTestExperience(id, "Test Experience");
        when(userExperienceService.getUserExperienceById(id)).thenReturn(Optional.of(experience));

        // Act
        Response response = userExperienceResource.getUserExperience(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<UserExperience> apiResponse = (ApiResponse<UserExperience>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(id);
        verify(userExperienceService).getUserExperienceById(id);
    }

    @Test
    void testGetUserExperienceByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(userExperienceService.getUserExperienceById(id)).thenReturn(Optional.empty());

        // Act
        Response response = userExperienceResource.getUserExperience(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(404);
        ApiResponse<UserExperience> apiResponse = (ApiResponse<UserExperience>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).contains("not found");
        verify(userExperienceService).getUserExperienceById(id);
    }

    @Test
    void testCreateUserExperience() {
        // Arrange
        UserExperience experience = createTestExperience(null, "New Experience");
        Long expectedId = 1L;
        when(userExperienceService.createUserExperience(any(UserExperience.class))).thenReturn(expectedId);

        // Act
        Response response = userExperienceResource.createUserExperience(experience);

        // Assert
        assertThat(response.getStatus()).isEqualTo(201);
        ApiResponse<UserExperience> apiResponse = (ApiResponse<UserExperience>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(expectedId);
        assertThat(apiResponse.getMessage()).contains("created successfully");
        verify(userExperienceService).createUserExperience(experience);
    }

    @Test
    void testUpdateUserExperience() {
        // Arrange
        Long id = 1L;
        UserExperience experience = createTestExperience(id, "Updated Experience");
        when(userExperienceService.updateUserExperience(eq(id), any(UserExperience.class))).thenReturn(true);

        // Act
        Response response = userExperienceResource.updateUserExperience(id, experience);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).contains("updated successfully");
        verify(userExperienceService).updateUserExperience(id, experience);
    }

    @Test
    void testUpdateUserExperienceNotFound() {
        // Arrange
        Long id = 999L;
        UserExperience experience = createTestExperience(id, "Updated Experience");
        when(userExperienceService.updateUserExperience(eq(id), any(UserExperience.class))).thenReturn(false);

        // Act
        Response response = userExperienceResource.updateUserExperience(id, experience);

        // Assert
        assertThat(response.getStatus()).isEqualTo(404);
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).contains("not found");
        verify(userExperienceService).updateUserExperience(id, experience);
    }

    @Test
    void testDeleteUserExperience() {
        // Arrange
        Long id = 1L;
        when(userExperienceService.deleteUserExperience(id)).thenReturn(true);

        // Act
        Response response = userExperienceResource.deleteUserExperience(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).contains("deleted successfully");
        verify(userExperienceService).deleteUserExperience(id);
    }

    @Test
    void testDeleteUserExperienceNotFound() {
        // Arrange
        Long id = 999L;
        when(userExperienceService.deleteUserExperience(id)).thenReturn(false);

        // Act
        Response response = userExperienceResource.deleteUserExperience(id);

        // Assert
        assertThat(response.getStatus()).isEqualTo(404);
        ApiResponse<Void> apiResponse = (ApiResponse<Void>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).contains("not found");
        verify(userExperienceService).deleteUserExperience(id);
    }

    @Test
    void testGetUserExperiencesByUserId() {
        // Arrange
        Long userId = 100L;
        UserExperience exp1 = createTestExperience(1L, "Experience 1");
        exp1.setUserId(userId);
        List<UserExperience> experiences = Arrays.asList(exp1);
        when(userExperienceService.getUserExperiencesByUserId(userId)).thenReturn(experiences);

        // Act
        Response response = userExperienceResource.getUserExperiencesByUserId(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<List<UserExperience>> apiResponse = (ApiResponse<List<UserExperience>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(1);
        verify(userExperienceService).getUserExperiencesByUserId(userId);
    }

    @Test
    void testGetUserExperiencesByUserIdAndType() {
        // Arrange
        Long userId = 100L;
        String type = "LEADERSHIP";
        UserExperience exp1 = createTestExperience(1L, "Leadership Experience");
        exp1.setUserId(userId);
        exp1.setExperienceType(type);
        List<UserExperience> experiences = Arrays.asList(exp1);
        when(userExperienceService.getUserExperiencesByUserIdAndType(userId, type)).thenReturn(experiences);

        // Act
        Response response = userExperienceResource.getUserExperiencesByUserIdAndType(userId, type);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<List<UserExperience>> apiResponse = (ApiResponse<List<UserExperience>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(1);
        assertThat(apiResponse.getData().get(0).getExperienceType()).isEqualTo(type);
        verify(userExperienceService).getUserExperiencesByUserIdAndType(userId, type);
    }

    @Test
    void testGetCurrentUserExperiencesByUserId() {
        // Arrange
        Long userId = 100L;
        UserExperience exp1 = createTestExperience(1L, "Current Experience");
        exp1.setUserId(userId);
        exp1.setIsCurrent(true);
        List<UserExperience> experiences = Arrays.asList(exp1);
        when(userExperienceService.getCurrentUserExperiencesByUserId(userId)).thenReturn(experiences);

        // Act
        Response response = userExperienceResource.getCurrentUserExperiencesByUserId(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(200);
        ApiResponse<List<UserExperience>> apiResponse = (ApiResponse<List<UserExperience>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(1);
        assertThat(apiResponse.getData().get(0).getIsCurrent()).isTrue();
        verify(userExperienceService).getCurrentUserExperiencesByUserId(userId);
    }

    private UserExperience createTestExperience(Long id, String title) {
        UserExperience experience = new UserExperience();
        experience.setId(id);
        experience.setUserId(100L);
        experience.setTitle(title);
        experience.setDescription("Test description");
        experience.setExperienceType("TECHNICAL");
        experience.setStartDate(Date.valueOf("2023-01-01"));
        experience.setEndDate(Date.valueOf("2023-12-31"));
        experience.setIsCurrent(false);
        return experience;
    }
}

