package com.irusso.demoserver.db.dao;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for ColumnDefinition.
 */
class ColumnDefinitionTest {

    static class TestEntity {
        private Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void testBuilder_MinimalConfiguration() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .build();

        assertThat(column.getColumnName()).isEqualTo("id");
        assertThat(column.getJavaPropertyName()).isEqualTo("id");
        assertThat(column.getJavaType()).isEqualTo(Long.class);
        assertThat(column.isInsertable()).isTrue();
        assertThat(column.isUpdatable()).isTrue();
    }

    @Test
    void testBuilder_FullConfiguration() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("user_name")
                .javaPropertyName("name")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .getter(TestEntity::getName)
                .build();

        assertThat(column.getColumnName()).isEqualTo("user_name");
        assertThat(column.getJavaPropertyName()).isEqualTo("name");
        assertThat(column.getJavaType()).isEqualTo(String.class);
        assertThat(column.isInsertable()).isTrue();
        assertThat(column.isUpdatable()).isTrue();
    }

    @Test
    void testBuilder_AutoGenerateJavaPropertyName() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("user_name")
                .javaType(String.class)
                .build();

        assertThat(column.getJavaPropertyName()).isEqualTo("userName");
    }

    @Test
    void testBuilder_MissingColumnName() {
        assertThatThrownBy(() ->
                ColumnDefinition.<TestEntity>builder()
                        .javaType(String.class)
                        .build()
        ).isInstanceOf(IllegalStateException.class)
                .hasMessage("columnName is required");
    }

    @Test
    void testBuilder_MissingJavaType() {
        assertThatThrownBy(() ->
                ColumnDefinition.<TestEntity>builder()
                        .columnName("name")
                        .build()
        ).isInstanceOf(IllegalStateException.class)
                .hasMessage("javaType is required");
    }

    @Test
    void testToCamelCase_SimpleCase() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("user_name")
                .javaType(String.class)
                .build();

        assertThat(column.getJavaPropertyName()).isEqualTo("userName");
    }

    @Test
    void testToCamelCase_MultipleUnderscores() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("user_first_name")
                .javaType(String.class)
                .build();

        assertThat(column.getJavaPropertyName()).isEqualTo("userFirstName");
    }

    @Test
    void testToCamelCase_NoUnderscores() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .build();

        assertThat(column.getJavaPropertyName()).isEqualTo("name");
    }

    @Test
    void testGetter_FunctionalInterface() {
        TestEntity entity = new TestEntity();
        entity.setName("Test Name");

        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .getter(TestEntity::getName)
                .build();

        Object value = column.getValue(entity);
        assertThat(value).isEqualTo("Test Name");
    }

    @Test
    void testInsertableAndUpdatable_DefaultValues() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .build();

        assertThat(column.isInsertable()).isTrue();
        assertThat(column.isUpdatable()).isTrue();
    }

    @Test
    void testInsertableAndUpdatable_CustomValues() {
        ColumnDefinition<TestEntity> column = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .build();

        assertThat(column.isInsertable()).isTrue();
        assertThat(column.isUpdatable()).isTrue();
    }
}

