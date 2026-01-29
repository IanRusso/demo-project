package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.SavedJobDao;
import com.irusso.demoserver.db.model.SavedJob;
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
class SavedJobServiceTest {

    @Mock
    private SavedJobDao savedJobDao;

    private SavedJobService savedJobService;

    @BeforeEach
    void setUp() {
        savedJobService = new SavedJobService(savedJobDao);
    }

    @Test
    void testGetAllSavedJobs() {
        // Arrange
        SavedJob saved1 = new SavedJob();
        saved1.setId(1L);
        saved1.setUserId(100L);

        SavedJob saved2 = new SavedJob();
        saved2.setId(2L);
        saved2.setUserId(101L);

        List<SavedJob> expectedSavedJobs = Arrays.asList(saved1, saved2);
        when(savedJobDao.findAll()).thenReturn(expectedSavedJobs);

        // Act
        List<SavedJob> actualSavedJobs = savedJobService.getAllSavedJobs();

        // Assert
        assertThat(actualSavedJobs).hasSize(2);
        verify(savedJobDao, times(1)).findAll();
    }

    @Test
    void testGetSavedJobById_Found() {
        // Arrange
        SavedJob savedJob = new SavedJob();
        savedJob.setId(1L);
        savedJob.setUserId(100L);

        when(savedJobDao.findById(1L)).thenReturn(Optional.of(savedJob));

        // Act
        Optional<SavedJob> result = savedJobService.getSavedJobById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(savedJobDao, times(1)).findById(1L);
    }

    @Test
    void testGetSavedJobById_NotFound() {
        // Arrange
        when(savedJobDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<SavedJob> result = savedJobService.getSavedJobById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(savedJobDao, times(1)).findById(999L);
    }

    @Test
    void testCreateSavedJob() {
        // Arrange
        SavedJob newSavedJob = new SavedJob();
        newSavedJob.setUserId(100L);

        when(savedJobDao.insert(newSavedJob)).thenReturn(1L);

        // Act
        Long savedJobId = savedJobService.createSavedJob(newSavedJob);

        // Assert
        assertThat(savedJobId).isEqualTo(1L);
        verify(savedJobDao, times(1)).insert(newSavedJob);
    }

    @Test
    void testUpdateSavedJob_Success() {
        // Arrange
        SavedJob savedJob = new SavedJob();
        savedJob.setId(1L);

        when(savedJobDao.update(1L, savedJob)).thenReturn(true);

        // Act
        boolean result = savedJobService.updateSavedJob(savedJob);

        // Assert
        assertThat(result).isTrue();
        verify(savedJobDao, times(1)).update(1L, savedJob);
    }

    @Test
    void testUpdateSavedJob_Failure() {
        // Arrange
        SavedJob savedJob = new SavedJob();
        savedJob.setId(999L);

        when(savedJobDao.update(999L, savedJob)).thenReturn(false);

        // Act
        boolean result = savedJobService.updateSavedJob(savedJob);

        // Assert
        assertThat(result).isFalse();
        verify(savedJobDao, times(1)).update(999L, savedJob);
    }

    @Test
    void testDeleteSavedJob_Success() {
        // Arrange
        when(savedJobDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = savedJobService.deleteSavedJob(1L);

        // Assert
        assertThat(result).isTrue();
        verify(savedJobDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteSavedJob_Failure() {
        // Arrange
        when(savedJobDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = savedJobService.deleteSavedJob(999L);

        // Assert
        assertThat(result).isFalse();
        verify(savedJobDao, times(1)).delete(999L);
    }
}

