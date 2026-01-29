package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.UserGeographicalInterestDao;
import com.irusso.demoserver.db.model.UserGeographicalInterest;
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
class UserGeographicalInterestServiceTest {

    @Mock
    private UserGeographicalInterestDao userGeographicalInterestDao;

    private UserGeographicalInterestService userGeographicalInterestService;

    @BeforeEach
    void setUp() {
        userGeographicalInterestService = new UserGeographicalInterestService(userGeographicalInterestDao);
    }

    @Test
    void testGetAllUserGeographicalInterests() {
        // Arrange
        UserGeographicalInterest geo1 = new UserGeographicalInterest();
        geo1.setId(1L);
        geo1.setUserId(100L);

        UserGeographicalInterest geo2 = new UserGeographicalInterest();
        geo2.setId(2L);
        geo2.setUserId(101L);

        List<UserGeographicalInterest> expectedGeos = Arrays.asList(geo1, geo2);
        when(userGeographicalInterestDao.findAll()).thenReturn(expectedGeos);

        // Act
        List<UserGeographicalInterest> actualGeos = userGeographicalInterestService.getAllUserGeographicalInterests();

        // Assert
        assertThat(actualGeos).hasSize(2);
        verify(userGeographicalInterestDao, times(1)).findAll();
    }

    @Test
    void testGetUserGeographicalInterestById_Found() {
        // Arrange
        UserGeographicalInterest geo = new UserGeographicalInterest();
        geo.setId(1L);
        geo.setUserId(100L);

        when(userGeographicalInterestDao.findById(1L)).thenReturn(Optional.of(geo));

        // Act
        Optional<UserGeographicalInterest> result = userGeographicalInterestService.getUserGeographicalInterestById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(userGeographicalInterestDao, times(1)).findById(1L);
    }

    @Test
    void testGetUserGeographicalInterestById_NotFound() {
        // Arrange
        when(userGeographicalInterestDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<UserGeographicalInterest> result = userGeographicalInterestService.getUserGeographicalInterestById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(userGeographicalInterestDao, times(1)).findById(999L);
    }

    @Test
    void testCreateUserGeographicalInterest() {
        // Arrange
        UserGeographicalInterest newGeo = new UserGeographicalInterest();
        newGeo.setUserId(100L);

        when(userGeographicalInterestDao.insert(newGeo)).thenReturn(1L);

        // Act
        Long geoId = userGeographicalInterestService.createUserGeographicalInterest(newGeo);

        // Assert
        assertThat(geoId).isEqualTo(1L);
        verify(userGeographicalInterestDao, times(1)).insert(newGeo);
    }

    @Test
    void testUpdateUserGeographicalInterest_Success() {
        // Arrange
        UserGeographicalInterest geo = new UserGeographicalInterest();
        geo.setId(1L);

        when(userGeographicalInterestDao.update(1L, geo)).thenReturn(true);

        // Act
        boolean result = userGeographicalInterestService.updateUserGeographicalInterest(geo);

        // Assert
        assertThat(result).isTrue();
        verify(userGeographicalInterestDao, times(1)).update(1L, geo);
    }

    @Test
    void testUpdateUserGeographicalInterest_Failure() {
        // Arrange
        UserGeographicalInterest geo = new UserGeographicalInterest();
        geo.setId(999L);

        when(userGeographicalInterestDao.update(999L, geo)).thenReturn(false);

        // Act
        boolean result = userGeographicalInterestService.updateUserGeographicalInterest(geo);

        // Assert
        assertThat(result).isFalse();
        verify(userGeographicalInterestDao, times(1)).update(999L, geo);
    }

    @Test
    void testDeleteUserGeographicalInterest_Success() {
        // Arrange
        when(userGeographicalInterestDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = userGeographicalInterestService.deleteUserGeographicalInterest(1L);

        // Assert
        assertThat(result).isTrue();
        verify(userGeographicalInterestDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteUserGeographicalInterest_Failure() {
        // Arrange
        when(userGeographicalInterestDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = userGeographicalInterestService.deleteUserGeographicalInterest(999L);

        // Assert
        assertThat(result).isFalse();
        verify(userGeographicalInterestDao, times(1)).delete(999L);
    }
}

