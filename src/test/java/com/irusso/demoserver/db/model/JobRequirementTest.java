package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JobRequirementTest {

    @Test
    void testDefaultConstructor() {
        // Act
        JobRequirement requirement = new JobRequirement();

        // Assert
        assertThat(requirement).isNotNull();
        assertThat(requirement.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        JobRequirement requirement = new JobRequirement();

        // Act
        requirement.setId(1L);
        requirement.setJobPostingId(100L);
        requirement.setRequirementType("hard");
        requirement.setRequirementCategory("skill");
        requirement.setRequirementText("Java programming");

        // Assert
        assertThat(requirement.getId()).isEqualTo(1L);
        assertThat(requirement.getJobPostingId()).isEqualTo(100L);
        assertThat(requirement.getRequirementType()).isEqualTo("hard");
        assertThat(requirement.getRequirementCategory()).isEqualTo("skill");
        assertThat(requirement.getRequirementText()).isEqualTo("Java programming");
    }

    @Test
    void testNullableFields() {
        // Arrange
        JobRequirement requirement = new JobRequirement();

        // Act
        requirement.setId(1L);
        requirement.setJobPostingId(100L);

        // Assert - nullable fields should be null
        assertThat(requirement.getRequirementType()).isNull();
        assertThat(requirement.getRequirementCategory()).isNull();
        assertThat(requirement.getRequirementText()).isNull();
    }
}

