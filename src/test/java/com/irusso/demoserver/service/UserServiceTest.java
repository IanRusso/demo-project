package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserDao;
import com.irusso.demoserver.db.model.User;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserDao userDao;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userDao);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Smith");
        user2.setEmail("jane@example.com");

        List<User> expectedUsers = Arrays.asList(user1, user2);
        when(userDao.findAll()).thenReturn(expectedUsers);

        // Act
        List<User> actualUsers = userService.getAllUsers();

        // Assert
        assertThat(actualUsers).hasSize(2);
        assertThat(actualUsers).containsExactlyElementsOf(expectedUsers);
        verify(userDao, times(1)).findAll();
    }

    @Test
    void testGetUserById_Found() {
        // Arrange
        Long userId = 1L;
        User expectedUser = new User();
        expectedUser.setId(userId);
        expectedUser.setName("John Doe");
        expectedUser.setEmail("john@example.com");

        when(userDao.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Act
        Optional<User> actualUser = userService.getUserById(userId);

        // Assert
        assertThat(actualUser).isPresent();
        assertThat(actualUser.get().getId()).isEqualTo(userId);
        assertThat(actualUser.get().getName()).isEqualTo("John Doe");
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long userId = 999L;
        when(userDao.findById(userId)).thenReturn(Optional.empty());

        // Act
        Optional<User> actualUser = userService.getUserById(userId);

        // Assert
        assertThat(actualUser).isEmpty();
        verify(userDao, times(1)).findById(userId);
    }

    @Test
    void testCreateUser() {
        // Arrange
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");

        Long expectedId = 123L;
        when(userDao.insert(newUser)).thenReturn(expectedId);

        // Act
        Long actualId = userService.createUser(newUser);

        // Assert
        assertThat(actualId).isEqualTo(expectedId);
        verify(userDao, times(1)).insert(newUser);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("Updated Name");
        user.setEmail("updated@example.com");

        when(userDao.update(eq(1L), eq(user))).thenReturn(true);

        // Act
        boolean result = userService.updateUser(user);

        // Assert
        assertThat(result).isTrue();
        verify(userDao, times(1)).update(1L, user);
    }

    @Test
    void testUpdateUser_Failure() {
        // Arrange
        User user = new User();
        user.setId(999L);
        user.setName("Non-existent User");

        when(userDao.update(eq(999L), eq(user))).thenReturn(false);

        // Act
        boolean result = userService.updateUser(user);

        // Assert
        assertThat(result).isFalse();
        verify(userDao, times(1)).update(999L, user);
    }

    @Test
    void testDeleteUser_Success() {
        // Arrange
        Long userId = 1L;
        when(userDao.delete(userId)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertThat(result).isTrue();
        verify(userDao, times(1)).delete(userId);
    }

    @Test
    void testDeleteUser_Failure() {
        // Arrange
        Long userId = 999L;
        when(userDao.delete(userId)).thenReturn(false);

        // Act
        boolean result = userService.deleteUser(userId);

        // Assert
        assertThat(result).isFalse();
        verify(userDao, times(1)).delete(userId);
    }
}

