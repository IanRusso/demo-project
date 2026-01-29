package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserFieldOfInterestDao;
import com.irusso.demoserver.db.model.UserFieldOfInterest;
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
class UserFieldOfInterestServiceTest {

    @Mock
    private UserFieldOfInterestDao userFieldOfInterestDao;

    private UserFieldOfInterestService userFieldOfInterestService;

    @BeforeEach
    void setUp() {
        userFieldOfInterestService = new UserFieldOfInterestService(userFieldOfInterestDao);
    }

    @Test
    void testGetAllUserFieldsOfInterest() {
        // Arrange
        UserFieldOfInterest field1 = new UserFieldOfInterest();
        field1.setId(1L);
        field1.setUserId(100L);

        UserFieldOfInterest field2 = new UserFieldOfInterest();
        field2.setId(2L);
        field2.setUserId(101L);

        List<UserFieldOfInterest> expectedFields = Arrays.asList(field1, field2);
        when(userFieldOfInterestDao.findAll()).thenReturn(expectedFields);

        // Act
        List<UserFieldOfInterest> actualFields = userFieldOfInterestService.getAllUserFieldsOfInterest();

        // Assert
        assertThat(actualFields).hasSize(2);
        verify(userFieldOfInterestDao, times(1)).findAll();
    }

    @Test
    void testGetUserFieldOfInterestById_Found() {
        // Arrange
        UserFieldOfInterest field = new UserFieldOfInterest();
        field.setId(1L);
        field.setUserId(100L);

        when(userFieldOfInterestDao.findById(1L)).thenReturn(Optional.of(field));

        // Act
        Optional<UserFieldOfInterest> result = userFieldOfInterestService.getUserFieldOfInterestById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(userFieldOfInterestDao, times(1)).findById(1L);
    }

    @Test
    void testGetUserFieldOfInterestById_NotFound() {
        // Arrange
        when(userFieldOfInterestDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<UserFieldOfInterest> result = userFieldOfInterestService.getUserFieldOfInterestById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userFieldOfInterestDao, times(1)).findById(999L);
    }

    @Test
    void testCreateUserFieldOfInterest() {
        // Arrange
        UserFieldOfInterest newField = new UserFieldOfInterest();
        newField.setUserId(100L);

        when(userFieldOfInterestDao.insert(newField)).thenReturn(1L);

        // Act
        Long fieldId = userFieldOfInterestService.createUserFieldOfInterest(newField);

        // Assert
        assertThat(fieldId).isEqualTo(1L);
        verify(userFieldOfInterestDao, times(1)).insert(newField);
    }

    @Test
    void testUpdateUserFieldOfInterest_Success() {
        // Arrange
        UserFieldOfInterest field = new UserFieldOfInterest();
        field.setId(1L);

        when(userFieldOfInterestDao.update(1L, field)).thenReturn(true);

        // Act
        boolean result = userFieldOfInterestService.updateUserFieldOfInterest(field);

        // Assert
        assertThat(result).isTrue();
        verify(userFieldOfInterestDao, times(1)).update(1L, field);
    }

    @Test
    void testUpdateUserFieldOfInterest_Failure() {
        // Arrange
        UserFieldOfInterest field = new UserFieldOfInterest();
        field.setId(999L);

        when(userFieldOfInterestDao.update(999L, field)).thenReturn(false);

        // Act
        boolean result = userFieldOfInterestService.updateUserFieldOfInterest(field);

        // Assert
        assertThat(result).isFalse();
        verify(userFieldOfInterestDao, times(1)).update(999L, field);
    }

    @Test
    void testDeleteUserFieldOfInterest_Success() {
        // Arrange
        when(userFieldOfInterestDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = userFieldOfInterestService.deleteUserFieldOfInterest(1L);

        // Assert
        assertThat(result).isTrue();
        verify(userFieldOfInterestDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserFieldOfInterest_Failure() {
        // Arrange
        when(userFieldOfInterestDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = userFieldOfInterestService.deleteUserFieldOfInterest(999L);

        // Assert
        assertThat(result).isFalse();
        verify(userFieldOfInterestDao, times(1)).delete(999L);
    }
}

