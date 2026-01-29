package com.irusso.demoserver.db.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserSkillTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserSkill userSkill = new UserSkill();

        // Assert
        assertThat(userSkill).isNotNull();
        assertThat(userSkill.getId()).isNull();
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UserSkill userSkill = new UserSkill();

        // Act
        userSkill.setId(1L);
        userSkill.setUserId(100L);
        userSkill.setSkillName("Java");
        userSkill.setProficiencyLevel("Expert");

        // Assert
        assertThat(userSkill.getId()).isEqualTo(1L);
        assertThat(userSkill.getUserId()).isEqualTo(100L);
        assertThat(userSkill.getSkillName()).isEqualTo("Java");
        assertThat(userSkill.getProficiencyLevel()).isEqualTo("Expert");
    }

    @Test
    void testNullableFields() {
        // Arrange
        UserSkill userSkill = new UserSkill();

        // Act
        userSkill.setId(1L);
        userSkill.setUserId(100L);
        userSkill.setSkillName("Java");

        // Assert - nullable fields should be null
        assertThat(userSkill.getProficiencyLevel()).isNull();
    }
}

