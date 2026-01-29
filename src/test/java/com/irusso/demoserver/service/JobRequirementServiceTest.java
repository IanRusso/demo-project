package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.JobRequirementDao;
import com.irusso.demoserver.db.model.JobRequirement;
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
class JobRequirementServiceTest {

    @Mock
    private JobRequirementDao jobRequirementDao;

    private JobRequirementService jobRequirementService;

    @BeforeEach
    void setUp() {
        jobRequirementService = new JobRequirementService(jobRequirementDao);
    }

    @Test
    void testGetAllJobRequirements() {
        // Arrange
        JobRequirement req1 = new JobRequirement();
        req1.setId(1L);
        req1.setJobPostingId(100L);

        JobRequirement req2 = new JobRequirement();
        req2.setId(2L);
        req2.setJobPostingId(101L);

        List<JobRequirement> expectedRequirements = Arrays.asList(req1, req2);
        when(jobRequirementDao.findAll()).thenReturn(expectedRequirements);

        // Act
        List<JobRequirement> actualRequirements = jobRequirementService.getAllJobRequirements();

        // Assert
        assertThat(actualRequirements).hasSize(2);
        verify(jobRequirementDao, times(1)).findAll();
    }

    @Test
    void testGetJobRequirementById_Found() {
        // Arrange
        JobRequirement requirement = new JobRequirement();
        requirement.setId(1L);
        requirement.setJobPostingId(100L);

        when(jobRequirementDao.findById(1L)).thenReturn(Optional.of(requirement));

        // Act
        Optional<JobRequirement> result = jobRequirementService.getJobRequirementById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(jobRequirementDao, times(1)).findById(1L);
    }

    @Test
    void testGetJobRequirementById_NotFound() {
        // Arrange
        when(jobRequirementDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<JobRequirement> result = jobRequirementService.getJobRequirementById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(jobRequirementDao, times(1)).findById(999L);
    }

    @Test
    void testCreateJobRequirement() {
        // Arrange
        JobRequirement newRequirement = new JobRequirement();
        newRequirement.setJobPostingId(100L);

        when(jobRequirementDao.insert(newRequirement)).thenReturn(1L);

        // Act
        Long requirementId = jobRequirementService.createJobRequirement(newRequirement);

        // Assert
        assertThat(requirementId).isEqualTo(1L);
        verify(jobRequirementDao, times(1)).insert(newRequirement);
    }

    @Test
    void testUpdateJobRequirement_Success() {
        // Arrange
        JobRequirement requirement = new JobRequirement();
        requirement.setId(1L);

        when(jobRequirementDao.update(1L, requirement)).thenReturn(true);

        // Act
        boolean result = jobRequirementService.updateJobRequirement(requirement);

        // Assert
        assertThat(result).isTrue();
        verify(jobRequirementDao, times(1)).update(1L, requirement);
    }

    @Test
    void testUpdateJobRequirement_Failure() {
        // Arrange
        JobRequirement requirement = new JobRequirement();
        requirement.setId(999L);

        when(jobRequirementDao.update(999L, requirement)).thenReturn(false);

        // Act
        boolean result = jobRequirementService.updateJobRequirement(requirement);

        // Assert
        assertThat(result).isFalse();
        verify(jobRequirementDao, times(1)).update(999L, requirement);
    }

    @Test
    void testDeleteJobRequirement_Success() {
        // Arrange
        when(jobRequirementDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = jobRequirementService.deleteJobRequirement(1L);

        // Assert
        assertThat(result).isTrue();
        verify(jobRequirementDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteJobRequirement_Failure() {
        // Arrange
        when(jobRequirementDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = jobRequirementService.deleteJobRequirement(999L);

        // Assert
        assertThat(result).isFalse();
        verify(jobRequirementDao, times(1)).delete(999L);
    }
}

