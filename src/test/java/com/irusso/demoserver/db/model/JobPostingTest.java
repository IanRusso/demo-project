package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for JobPosting model.
 */
class JobPostingTest {

    @Test
    void testDefaultConstructor() {
        JobPosting jobPosting = new JobPosting();
        assertThat(jobPosting).isNotNull();
    }

    @Test
    void testSettersAndGetters() {
        JobPosting jobPosting = new JobPosting();
        Timestamp now = Timestamp.from(Instant.now());

        jobPosting.setId(1L);
        jobPosting.setEmployerId(100L);
        jobPosting.setTitle("Software Engineer");
        jobPosting.setDescription("Exciting opportunity");
        jobPosting.setResponsibilities("Develop software");
        jobPosting.setLocation("San Francisco");
        jobPosting.setField("Technology");
        jobPosting.setExperienceLevel("Mid-Level");
        jobPosting.setSalaryMin(new BigDecimal("100000"));
        jobPosting.setSalaryMax(new BigDecimal("150000"));
        jobPosting.setStatus("Open");
        jobPosting.setPostedDate(now);
        jobPosting.setClosedDate(null);
        jobPosting.setCreatedAt(now);
        jobPosting.setUpdatedAt(now);

        assertThat(jobPosting.getId()).isEqualTo(1L);
        assertThat(jobPosting.getEmployerId()).isEqualTo(100L);
        assertThat(jobPosting.getTitle()).isEqualTo("Software Engineer");
        assertThat(jobPosting.getDescription()).isEqualTo("Exciting opportunity");
        assertThat(jobPosting.getResponsibilities()).isEqualTo("Develop software");
        assertThat(jobPosting.getLocation()).isEqualTo("San Francisco");
        assertThat(jobPosting.getField()).isEqualTo("Technology");
        assertThat(jobPosting.getExperienceLevel()).isEqualTo("Mid-Level");
        assertThat(jobPosting.getSalaryMin()).isEqualByComparingTo(new BigDecimal("100000"));
        assertThat(jobPosting.getSalaryMax()).isEqualByComparingTo(new BigDecimal("150000"));
        assertThat(jobPosting.getStatus()).isEqualTo("Open");
        assertThat(jobPosting.getPostedDate()).isEqualTo(now);
        assertThat(jobPosting.getClosedDate()).isNull();
        assertThat(jobPosting.getCreatedAt()).isEqualTo(now);
        assertThat(jobPosting.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    void testNullableFields() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setEmployerId(100L);
        jobPosting.setTitle("Software Engineer");

        assertThat(jobPosting.getDescription()).isNull();
        assertThat(jobPosting.getResponsibilities()).isNull();
        assertThat(jobPosting.getLocation()).isNull();
        assertThat(jobPosting.getField()).isNull();
        assertThat(jobPosting.getExperienceLevel()).isNull();
        assertThat(jobPosting.getSalaryMin()).isNull();
        assertThat(jobPosting.getSalaryMax()).isNull();
        assertThat(jobPosting.getStatus()).isNull();
        assertThat(jobPosting.getPostedDate()).isNull();
        assertThat(jobPosting.getClosedDate()).isNull();
        assertThat(jobPosting.getCreatedAt()).isNull();
        assertThat(jobPosting.getUpdatedAt()).isNull();
    }

    @Test
    void testSalaryRange() {
        JobPosting jobPosting = new JobPosting();
        jobPosting.setSalaryMin(new BigDecimal("80000.00"));
        jobPosting.setSalaryMax(new BigDecimal("120000.50"));

        assertThat(jobPosting.getSalaryMin()).isEqualByComparingTo(new BigDecimal("80000.00"));
        assertThat(jobPosting.getSalaryMax()).isEqualByComparingTo(new BigDecimal("120000.50"));
        assertThat(jobPosting.getSalaryMax()).isGreaterThan(jobPosting.getSalaryMin());
    }
}

