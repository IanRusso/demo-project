package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserSkillDao;
import com.irusso.demoserver.db.model.UserSkill;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserSkillServiceTest {

    @Mock
    private UserSkillDao userSkillDao;

    private UserSkillService userSkillService;

    @BeforeEach
    void setUp() {
        userSkillService = new UserSkillService(userSkillDao);
    }

    @Test
    void testGetAllUserSkills() {
        // Arrange
        UserSkill skill1 = new UserSkill();
        skill1.setId(1L);
        skill1.setUserId(100L);

        UserSkill skill2 = new UserSkill();
        skill2.setId(2L);
        skill2.setUserId(101L);

        List<UserSkill> expectedSkills = Arrays.asList(skill1, skill2);
        when(userSkillDao.findAll()).thenReturn(expectedSkills);

        // Act
        List<UserSkill> actualSkills = userSkillService.getAllUserSkills();

        // Assert
        assertThat(actualSkills).hasSize(2);
        verify(userSkillDao, times(1)).findAll();
    }

    @Test
    void testGetUserSkillById_Found() {
        // Arrange
        UserSkill skill = new UserSkill();
        skill.setId(1L);
        skill.setUserId(100L);

        when(userSkillDao.findById(1L)).thenReturn(Optional.of(skill));

        // Act
        Optional<UserSkill> result = userSkillService.getUserSkillById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(userSkillDao, times(1)).findById(1L);
    }

    @Test
    void testGetUserSkillById_NotFound() {
        // Arrange
        when(userSkillDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<UserSkill> result = userSkillService.getUserSkillById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userSkillDao, times(1)).findById(999L);
    }

    @Test
    void testCreateUserSkill() {
        // Arrange
        UserSkill newSkill = new UserSkill();
        newSkill.setUserId(100L);

        when(userSkillDao.insert(newSkill)).thenReturn(1L);

        // Act
        Long skillId = userSkillService.createUserSkill(newSkill);

        // Assert
        assertThat(skillId).isEqualTo(1L);
        verify(userSkillDao, times(1)).insert(newSkill);
    }

    @Test
    void testUpdateUserSkill_Success() {
        // Arrange
        UserSkill skill = new UserSkill();
        skill.setId(1L);

        when(userSkillDao.update(1L, skill)).thenReturn(true);

        // Act
        boolean result = userSkillService.updateUserSkill(skill);

        // Assert
        assertThat(result).isTrue();
        verify(userSkillDao, times(1)).update(1L, skill);
    }

    @Test
    void testUpdateUserSkill_Failure() {
        // Arrange
        UserSkill skill = new UserSkill();
        skill.setId(999L);

        when(userSkillDao.update(999L, skill)).thenReturn(false);

        // Act
        boolean result = userSkillService.updateUserSkill(skill);

        // Assert
        assertThat(result).isFalse();
        verify(userSkillDao, times(1)).update(999L, skill);
    }

    @Test
    void testDeleteUserSkill_Success() {
        // Arrange
        when(userSkillDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = userSkillService.deleteUserSkill(1L);

        // Assert
        assertThat(result).isTrue();
        verify(userSkillDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserSkill_Failure() {
        // Arrange
        when(userSkillDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = userSkillService.deleteUserSkill(999L);

        // Assert
        assertThat(result).isFalse();
        verify(userSkillDao, times(1)).delete(999L);
    }
}

