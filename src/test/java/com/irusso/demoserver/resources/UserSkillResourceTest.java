package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserSkill;
import com.irusso.demoserver.service.UserSkillService;
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
class UserSkillResourceTest {

    @Mock
    private UserSkillService userSkillService;

    private UserSkillResource userSkillResource;

    @BeforeEach
    void setUp() {
        userSkillResource = new UserSkillResource(userSkillService);
    }

    @Test
    void testGetAllUserSkills() {
        // Arrange
        UserSkill skill1 = new UserSkill();
        skill1.setId(1L);
        skill1.setUserId(100L);

        UserSkill skill2 = new UserSkill();
        skill2.setId(2L);
        skill2.setUserId(200L);

        List<UserSkill> skills = Arrays.asList(skill1, skill2);
        when(userSkillService.getAllUserSkills()).thenReturn(skills);

        // Act
        Response response = userSkillResource.getAllUserSkills();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<UserSkill>> apiResponse = (ApiResponse<List<UserSkill>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userSkillService, times(1)).getAllUserSkills();
    }

    @Test
    void testGetUserSkill_Found() {
        // Arrange
        Long skillId = 1L;
        UserSkill skill = new UserSkill();
        skill.setId(skillId);
        skill.setUserId(100L);

        when(userSkillService.getUserSkillById(skillId)).thenReturn(Optional.of(skill));

        // Act
        Response response = userSkillResource.getUserSkill(skillId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserSkill> apiResponse = (ApiResponse<UserSkill>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(skillId);
        verify(userSkillService, times(1)).getUserSkillById(skillId);
    }

    @Test
    void testGetUserSkill_NotFound() {
        // Arrange
        Long skillId = 999L;
        when(userSkillService.getUserSkillById(skillId)).thenReturn(Optional.empty());

        // Act
        Response response = userSkillResource.getUserSkill(skillId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserSkill> apiResponse = (ApiResponse<UserSkill>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill not found");
        verify(userSkillService, times(1)).getUserSkillById(skillId);
    }

    @Test
    void testCreateUserSkill() {
        // Arrange
        UserSkill newSkill = new UserSkill();
        newSkill.setUserId(100L);
        newSkill.setSkillName("Java");

        Long generatedId = 123L;
        when(userSkillService.createUserSkill(newSkill)).thenReturn(generatedId);

        // Act
        Response response = userSkillResource.createUserSkill(newSkill);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<UserSkill> apiResponse = (ApiResponse<UserSkill>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(userSkillService, times(1)).createUserSkill(newSkill);
    }

    @Test
    void testUpdateUserSkill_Success() {
        // Arrange
        Long skillId = 1L;
        UserSkill existingSkill = new UserSkill();
        existingSkill.setId(skillId);
        existingSkill.setUserId(100L);

        UserSkill updatedSkill = new UserSkill();
        updatedSkill.setSkillName("Python");

        when(userSkillService.getUserSkillById(skillId)).thenReturn(Optional.of(existingSkill));
        when(userSkillService.updateUserSkill(any(UserSkill.class))).thenReturn(true);

        // Act
        Response response = userSkillResource.updateUserSkill(skillId, updatedSkill);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserSkill> apiResponse = (ApiResponse<UserSkill>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(skillId);
        verify(userSkillService, times(1)).getUserSkillById(skillId);
        verify(userSkillService, times(1)).updateUserSkill(any(UserSkill.class));
    }

    @Test
    void testUpdateUserSkill_NotFound() {
        // Arrange
        Long skillId = 999L;
        UserSkill updatedSkill = new UserSkill();
        updatedSkill.setSkillName("Python");

        when(userSkillService.getUserSkillById(skillId)).thenReturn(Optional.empty());

        // Act
        Response response = userSkillResource.updateUserSkill(skillId, updatedSkill);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserSkill> apiResponse = (ApiResponse<UserSkill>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill not found");
        verify(userSkillService, times(1)).getUserSkillById(skillId);
        verify(userSkillService, never()).updateUserSkill(any(UserSkill.class));
    }

    @Test
    void testDeleteUserSkill_Success() {
        // Arrange
        Long skillId = 1L;
        when(userSkillService.deleteUserSkill(skillId)).thenReturn(true);

        // Act
        Response response = userSkillResource.deleteUserSkill(skillId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill deleted successfully");
        verify(userSkillService, times(1)).deleteUserSkill(skillId);
    }

    @Test
    void testDeleteUserSkill_NotFound() {
        // Arrange
        Long skillId = 999L;
        when(userSkillService.deleteUserSkill(skillId)).thenReturn(false);

        // Act
        Response response = userSkillResource.deleteUserSkill(skillId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User skill not found");
        verify(userSkillService, times(1)).deleteUserSkill(skillId);
    }
}

