package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.JobPostingDao;
import com.irusso.demoserver.db.model.JobPosting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobPostingServiceTest {

    @Mock
    private JobPostingDao jobPostingDao;

    private JobPostingService jobPostingService;

    @BeforeEach
    void setUp() {
        jobPostingService = new JobPostingService(jobPostingDao);
    }

    @Test
    void testGetAllJobPostings() {
        // Arrange
        JobPosting job1 = new JobPosting();
        job1.setId(1L);
        job1.setTitle("Software Engineer");
        job1.setEmployerId(100L);

        JobPosting job2 = new JobPosting();
        job2.setId(2L);
        job2.setTitle("Data Analyst");
        job2.setEmployerId(101L);

        List<JobPosting> expectedJobs = Arrays.asList(job1, job2);
        when(jobPostingDao.findAll()).thenReturn(expectedJobs);

        // Act
        List<JobPosting> actualJobs = jobPostingService.getAllJobPostings();

        // Assert
        assertThat(actualJobs).hasSize(2);
        assertThat(actualJobs).containsExactlyElementsOf(expectedJobs);
        verify(jobPostingDao, times(1)).findAll();
    }

    @Test
    void testGetJobPostingById_Found() {
        // Arrange
        JobPosting job = new JobPosting();
        job.setId(1L);
        job.setTitle("Software Engineer");
        job.setSalaryMin(new BigDecimal("80000"));

        when(jobPostingDao.findById(1L)).thenReturn(Optional.of(job));

        // Act
        Optional<JobPosting> result = jobPostingService.getJobPostingById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getTitle()).isEqualTo("Software Engineer");
        verify(jobPostingDao, times(1)).findById(1L);
    }

    @Test
    void testGetJobPostingById_NotFound() {
        // Arrange
        when(jobPostingDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<JobPosting> result = jobPostingService.getJobPostingById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(jobPostingDao, times(1)).findById(999L);
    }

    @Test
    void testCreateJobPosting() {
        // Arrange
        JobPosting newJob = new JobPosting();
        newJob.setTitle("New Position");
        newJob.setEmployerId(100L);

        when(jobPostingDao.insert(newJob)).thenReturn(1L);

        // Act
        Long jobId = jobPostingService.createJobPosting(newJob);

        // Assert
        assertThat(jobId).isEqualTo(1L);
        verify(jobPostingDao, times(1)).insert(newJob);
    }

    @Test
    void testUpdateJobPosting_Success() {
        // Arrange
        JobPosting job = new JobPosting();
        job.setId(1L);
        job.setTitle("Updated Position");

        when(jobPostingDao.update(1L, job)).thenReturn(true);

        // Act
        boolean result = jobPostingService.updateJobPosting(job);

        // Assert
        assertThat(result).isTrue();
        verify(jobPostingDao, times(1)).update(1L, job);
    }

    @Test
    void testUpdateJobPosting_Failure() {
        // Arrange
        JobPosting job = new JobPosting();
        job.setId(999L);
        job.setTitle("Non-existent Position");

        when(jobPostingDao.update(999L, job)).thenReturn(false);

        // Act
        boolean result = jobPostingService.updateJobPosting(job);

        // Assert
        assertThat(result).isFalse();
        verify(jobPostingDao, times(1)).update(999L, job);
    }

    @Test
    void testDeleteJobPosting_Success() {
        // Arrange
        when(jobPostingDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = jobPostingService.deleteJobPosting(1L);

        // Assert
        assertThat(result).isTrue();
        verify(jobPostingDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteJobPosting_Failure() {
        // Arrange
        when(jobPostingDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = jobPostingService.deleteJobPosting(999L);

        // Assert
        assertThat(result).isFalse();
        verify(jobPostingDao, times(1)).delete(999L);
    }
}

