package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.User;
import com.irusso.demoserver.service.UserService;
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

/**
 * Unit tests for UserResource.
 */
@ExtendWith(MockitoExtension.class)
class UserResourceTest {

    @Mock
    private UserService userService;

    private UserResource userResource;

    @BeforeEach
    void setUp() {
        userResource = new UserResource(userService);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Smith");

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(users);

        // Act
        Response response = userResource.getAllUsers();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<User>> apiResponse = (ApiResponse<List<User>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetUser_Found() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setName("John Doe");

        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // Act
        Response response = userResource.getUser(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<User> apiResponse = (ApiResponse<User>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(userId);
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testGetUser_NotFound() {
        // Arrange
        Long userId = 999L;
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act
        Response response = userResource.getUser(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<User> apiResponse = (ApiResponse<User>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User not found");
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");

        Long generatedId = 123L;
        when(userService.createUser(newUser)).thenReturn(generatedId);

        // Act
        Response response = userResource.createUser(newUser);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<User> apiResponse = (ApiResponse<User>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(userService, times(1)).createUser(newUser);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Existing User");

        User updatedUser = new User();
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated@example.com");

        when(userService.getUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userService.updateUser(any(User.class))).thenReturn(true);

        // Act
        Response response = userResource.updateUser(userId, updatedUser);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<User> apiResponse = (ApiResponse<User>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(userId);
        verify(userService, times(1)).getUserById(userId);
        verify(userService, times(1)).updateUser(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        // Arrange
        Long userId = 999L;
        User updatedUser = new User();
        updatedUser.setName("Updated User");

        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // Act
        Response response = userResource.updateUser(userId, updatedUser);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<User> apiResponse = (ApiResponse<User>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User not found");
        verify(userService, times(1)).getUserById(userId);
        verify(userService, never()).updateUser(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(true);

        // Act
        Response response = userResource.deleteUser(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User deleted successfully");
        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        // Arrange
        Long userId = 999L;
        when(userService.deleteUser(userId)).thenReturn(false);

        // Act
        Response response = userResource.deleteUser(userId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User not found");
        verify(userService, times(1)).deleteUser(userId);
    }
}

