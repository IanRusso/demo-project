package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserExperienceDao;
import com.irusso.demoserver.db.model.UserExperience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserExperienceService.
 */
@ExtendWith(MockitoExtension.class)
class UserExperienceServiceTest {

    @Mock
    private UserExperienceDao userExperienceDao;

    private UserExperienceService userExperienceService;

    @BeforeEach
    void setUp() {
        userExperienceService = new UserExperienceService(userExperienceDao);
    }

    @Test
    void testGetAllUserExperiences() {
        // Arrange
        UserExperience exp1 = createTestExperience(1L, "Experience 1");
        UserExperience exp2 = createTestExperience(2L, "Experience 2");
        List<UserExperience> experiences = Arrays.asList(exp1, exp2);
        when(userExperienceDao.findAll()).thenReturn(experiences);

        // Act
        List<UserExperience> result = userExperienceService.getAllUserExperiences();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(exp1, exp2);
        verify(userExperienceDao).findAll();
    }

    @Test
    void testGetUserExperienceById() {
        // Arrange
        Long id = 1L;
        UserExperience experience = createTestExperience(id, "Test Experience");
        when(userExperienceDao.findById(id)).thenReturn(Optional.of(experience));

        // Act
        Optional<UserExperience> result = userExperienceService.getUserExperienceById(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getTitle()).isEqualTo("Test Experience");
        verify(userExperienceDao).findById(id);
    }

    @Test
    void testGetUserExperienceByIdNotFound() {
        // Arrange
        Long id = 999L;
        when(userExperienceDao.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<UserExperience> result = userExperienceService.getUserExperienceById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(userExperienceDao).findById(id);
    }

    @Test
    void testCreateUserExperience() {
        // Arrange
        UserExperience experience = createTestExperience(null, "New Experience");
        Long expectedId = 1L;
        when(userExperienceDao.insert(any(UserExperience.class))).thenReturn(expectedId);

        // Act
        Long result = userExperienceService.createUserExperience(experience);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(expectedId);
        verify(userExperienceDao).insert(experience);
    }

    @Test
    void testUpdateUserExperience() {
        // Arrange
        Long id = 1L;
        UserExperience experience = createTestExperience(id, "Updated Experience");
        when(userExperienceDao.update(eq(id), any(UserExperience.class))).thenReturn(true);

        // Act
        boolean result = userExperienceService.updateUserExperience(id, experience);

        // Assert
        assertThat(result).isTrue();
        verify(userExperienceDao).update(id, experience);
    }

    @Test
    void testUpdateUserExperienceNotFound() {
        // Arrange
        Long id = 999L;
        UserExperience experience = createTestExperience(id, "Updated Experience");
        when(userExperienceDao.update(eq(id), any(UserExperience.class))).thenReturn(false);

        // Act
        boolean result = userExperienceService.updateUserExperience(id, experience);

        // Assert
        assertThat(result).isFalse();
        verify(userExperienceDao).update(id, experience);
    }

    @Test
    void testDeleteUserExperience() {
        // Arrange
        Long id = 1L;
        when(userExperienceDao.delete(id)).thenReturn(true);

        // Act
        boolean result = userExperienceService.deleteUserExperience(id);

        // Assert
        assertThat(result).isTrue();
        verify(userExperienceDao).delete(id);
    }

    @Test
    void testDeleteUserExperienceNotFound() {
        // Arrange
        Long id = 999L;
        when(userExperienceDao.delete(id)).thenReturn(false);

        // Act
        boolean result = userExperienceService.deleteUserExperience(id);

        // Assert
        assertThat(result).isFalse();
        verify(userExperienceDao).delete(id);
    }

    @Test
    void testGetUserExperiencesByUserId() {
        // Arrange
        Long userId = 100L;
        UserExperience exp1 = createTestExperience(1L, "Experience 1");
        exp1.setUserId(userId);
        UserExperience exp2 = createTestExperience(2L, "Experience 2");
        exp2.setUserId(userId);
        List<UserExperience> experiences = Arrays.asList(exp1, exp2);
        when(userExperienceDao.findByUserId(userId)).thenReturn(experiences);

        // Act
        List<UserExperience> result = userExperienceService.getUserExperiencesByUserId(userId);

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        assertThat(result.get(1).getUserId()).isEqualTo(userId);
        verify(userExperienceDao).findByUserId(userId);
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
        when(userExperienceDao.findByUserIdAndType(userId, type)).thenReturn(experiences);

        // Act
        List<UserExperience> result = userExperienceService.getUserExperiencesByUserIdAndType(userId, type);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        assertThat(result.get(0).getExperienceType()).isEqualTo(type);
        verify(userExperienceDao).findByUserIdAndType(userId, type);
    }

    @Test
    void testGetCurrentUserExperiencesByUserId() {
        // Arrange
        Long userId = 100L;
        UserExperience exp1 = createTestExperience(1L, "Current Experience");
        exp1.setUserId(userId);
        exp1.setIsCurrent(true);
        List<UserExperience> experiences = Arrays.asList(exp1);
        when(userExperienceDao.findCurrentByUserId(userId)).thenReturn(experiences);

        // Act
        List<UserExperience> result = userExperienceService.getCurrentUserExperiencesByUserId(userId);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        assertThat(result.get(0).getIsCurrent()).isTrue();
        verify(userExperienceDao).findCurrentByUserId(userId);
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

