package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserJobTypeInterestDao;
import com.irusso.demoserver.db.model.UserJobTypeInterest;
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
class UserJobTypeInterestServiceTest {

    @Mock
    private UserJobTypeInterestDao userJobTypeInterestDao;

    private UserJobTypeInterestService userJobTypeInterestService;

    @BeforeEach
    void setUp() {
        userJobTypeInterestService = new UserJobTypeInterestService(userJobTypeInterestDao);
    }

    @Test
    void testGetAllUserJobTypeInterests() {
        // Arrange
        UserJobTypeInterest jobType1 = new UserJobTypeInterest();
        jobType1.setId(1L);
        jobType1.setUserId(100L);

        UserJobTypeInterest jobType2 = new UserJobTypeInterest();
        jobType2.setId(2L);
        jobType2.setUserId(101L);

        List<UserJobTypeInterest> expectedJobTypes = Arrays.asList(jobType1, jobType2);
        when(userJobTypeInterestDao.findAll()).thenReturn(expectedJobTypes);

        // Act
        List<UserJobTypeInterest> actualJobTypes = userJobTypeInterestService.getAllUserJobTypeInterests();

        // Assert
        assertThat(actualJobTypes).hasSize(2);
        verify(userJobTypeInterestDao, times(1)).findAll();
    }

    @Test
    void testGetUserJobTypeInterestById_Found() {
        // Arrange
        UserJobTypeInterest jobType = new UserJobTypeInterest();
        jobType.setId(1L);
        jobType.setUserId(100L);

        when(userJobTypeInterestDao.findById(1L)).thenReturn(Optional.of(jobType));

        // Act
        Optional<UserJobTypeInterest> result = userJobTypeInterestService.getUserJobTypeInterestById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(userJobTypeInterestDao, times(1)).findById(1L);
    }

    @Test
    void testGetUserJobTypeInterestById_NotFound() {
        // Arrange
        when(userJobTypeInterestDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<UserJobTypeInterest> result = userJobTypeInterestService.getUserJobTypeInterestById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userJobTypeInterestDao, times(1)).findById(999L);
    }

    @Test
    void testCreateUserJobTypeInterest() {
        // Arrange
        UserJobTypeInterest newJobType = new UserJobTypeInterest();
        newJobType.setUserId(100L);

        when(userJobTypeInterestDao.insert(newJobType)).thenReturn(1L);

        // Act
        Long jobTypeId = userJobTypeInterestService.createUserJobTypeInterest(newJobType);

        // Assert
        assertThat(jobTypeId).isEqualTo(1L);
        verify(userJobTypeInterestDao, times(1)).insert(newJobType);
    }

    @Test
    void testUpdateUserJobTypeInterest_Success() {
        // Arrange
        UserJobTypeInterest jobType = new UserJobTypeInterest();
        jobType.setId(1L);

        when(userJobTypeInterestDao.update(1L, jobType)).thenReturn(true);

        // Act
        boolean result = userJobTypeInterestService.updateUserJobTypeInterest(jobType);

        // Assert
        assertThat(result).isTrue();
        verify(userJobTypeInterestDao, times(1)).update(1L, jobType);
    }

    @Test
    void testUpdateUserJobTypeInterest_Failure() {
        // Arrange
        UserJobTypeInterest jobType = new UserJobTypeInterest();
        jobType.setId(999L);

        when(userJobTypeInterestDao.update(999L, jobType)).thenReturn(false);

        // Act
        boolean result = userJobTypeInterestService.updateUserJobTypeInterest(jobType);

        // Assert
        assertThat(result).isFalse();
        verify(userJobTypeInterestDao, times(1)).update(999L, jobType);
    }

    @Test
    void testDeleteUserJobTypeInterest_Success() {
        // Arrange
        when(userJobTypeInterestDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = userJobTypeInterestService.deleteUserJobTypeInterest(1L);

        // Assert
        assertThat(result).isTrue();
        verify(userJobTypeInterestDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserJobTypeInterest_Failure() {
        // Arrange
        when(userJobTypeInterestDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = userJobTypeInterestService.deleteUserJobTypeInterest(999L);

        // Assert
        assertThat(result).isFalse();
        verify(userJobTypeInterestDao, times(1)).delete(999L);
    }
}

