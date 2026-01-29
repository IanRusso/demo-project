package com.irusso.demoserver.db.dao;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for TableDefinition.
 */
class TableDefinitionTest {

    static class TestEntity {
        private Long id;
        private String name;
        private String email;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }
    }

    @Test
    void testBuilder_MinimalConfiguration() {
        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .build();

        assertThat(table.getTableName()).isEqualTo("test_table");
        assertThat(table.getIdColumn()).isEqualTo("id");
        assertThat(table.getColumnDefinitions()).isEmpty();
    }

    @Test
    void testBuilder_WithCustomIdColumn() {
        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .idColumn("custom_id")
                .build();

        assertThat(table.getTableName()).isEqualTo("test_table");
        assertThat(table.getIdColumn()).isEqualTo("custom_id");
    }

    @Test
    void testBuilder_WithColumns() {
        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .getter(TestEntity::getName)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .getter(TestEntity::getEmail)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        assertThat(table.getColumnDefinitions()).hasSize(2);
        assertThat(table.getColumnDefinitions().get(0).getColumnName()).isEqualTo("name");
        assertThat(table.getColumnDefinitions().get(1).getColumnName()).isEqualTo("email");
    }

    @Test
    void testBuilder_MissingTableName() {
        assertThatThrownBy(() ->
                TableDefinition.<TestEntity>builder()
                        .build()
        ).isInstanceOf(IllegalStateException.class)
                .hasMessage("tableName is required");
    }

    @Test
    void testGetInsertableColumns() {
        ColumnDefinition<TestEntity> idColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .insertable(false)
                .updatable(false)
                .build();

        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(idColumn)
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        List<ColumnDefinition<TestEntity>> insertableColumns = table.getInsertableColumns();
        assertThat(insertableColumns).hasSize(2);
        assertThat(insertableColumns.get(0).getColumnName()).isEqualTo("name");
        assertThat(insertableColumns.get(1).getColumnName()).isEqualTo("email");
    }

    @Test
    void testGetUpdatableColumns() {
        ColumnDefinition<TestEntity> idColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .insertable(false)
                .updatable(false)
                .build();

        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .insertable(true)
                .updatable(true)
                .build();

        ColumnDefinition<TestEntity> createdAtColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("created_at")
                .javaType(String.class)
                .insertable(true)
                .updatable(false)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(idColumn)
                .addColumn(nameColumn)
                .addColumn(createdAtColumn)
                .build();

        List<ColumnDefinition<TestEntity>> updatableColumns = table.getUpdatableColumns();
        assertThat(updatableColumns).hasSize(1);
        assertThat(updatableColumns.get(0).getColumnName()).isEqualTo("name");
    }

    @Test
    void testGetColumn() {
        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(nameColumn)
                .build();

        ColumnDefinition<TestEntity> foundColumn = table.getColumn("name");
        assertThat(foundColumn).isNotNull();
        assertThat(foundColumn.getColumnName()).isEqualTo("name");

        ColumnDefinition<TestEntity> notFoundColumn = table.getColumn("nonexistent");
        assertThat(notFoundColumn).isNull();
    }

    @Test
    void testGetColumnNamesList() {
        ColumnDefinition<TestEntity> idColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .build();

        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaType(String.class)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(idColumn)
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        String columnNamesList = table.getColumnNamesList();
        assertThat(columnNamesList).isEqualTo("id, name, email");
    }

    @Test
    void testGetInsertableColumnNamesList() {
        ColumnDefinition<TestEntity> idColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("id")
                .javaType(Long.class)
                .insertable(false)
                .build();

        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaType(String.class)
                .insertable(true)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaType(String.class)
                .insertable(true)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(idColumn)
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        String insertableColumnNamesList = table.getInsertableColumnNamesList();
        assertThat(insertableColumnNamesList).isEqualTo("name, email");
    }

    @Test
    void testGetInsertableParametersList() {
        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaPropertyName("name")
                .javaType(String.class)
                .insertable(true)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaPropertyName("email")
                .javaType(String.class)
                .insertable(true)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        String parametersList = table.getInsertableParametersList();
        assertThat(parametersList).isEqualTo(":name, :email");
    }

    @Test
    void testGetUpdateSetClause() {
        ColumnDefinition<TestEntity> nameColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("name")
                .javaPropertyName("name")
                .javaType(String.class)
                .updatable(true)
                .build();

        ColumnDefinition<TestEntity> emailColumn = ColumnDefinition.<TestEntity>builder()
                .columnName("email")
                .javaPropertyName("email")
                .javaType(String.class)
                .updatable(true)
                .build();

        TableDefinition<TestEntity> table = TableDefinition.<TestEntity>builder()
                .tableName("test_table")
                .addColumn(nameColumn)
                .addColumn(emailColumn)
                .build();

        String updateSetClause = table.getUpdateSetClause();
        assertThat(updateSetClause).isEqualTo("name = :name, email = :email");
    }
}

