package com.irusso.demoserver.db.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Defines the structure of a database table for generic DAO operations.
 * 
 * @param <T> The entity type
 */
public class TableDefinition<T> {

    private final String tableName;
    private final String idColumn;
    private final List<ColumnDefinition<T>> columnDefinitions;

    private TableDefinition(Builder<T> builder) {
        this.tableName = builder.tableName;
        this.idColumn = builder.idColumn;
        this.columnDefinitions = Collections.unmodifiableList(builder.columnDefinitions);
    }

    /**
     * Get the table name.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Get the ID column name.
     */
    public String getIdColumn() {
        return idColumn;
    }

    /**
     * Get all column definitions.
     */
    public List<ColumnDefinition<T>> getColumnDefinitions() {
        return columnDefinitions;
    }

    /**
     * Get column definitions that should be included in INSERT statements.
     */
    public List<ColumnDefinition<T>> getInsertableColumns() {
        return columnDefinitions.stream()
            .filter(ColumnDefinition::isInsertable)
            .collect(Collectors.toList());
    }

    /**
     * Get column definitions that should be included in UPDATE statements.
     */
    public List<ColumnDefinition<T>> getUpdatableColumns() {
        return columnDefinitions.stream()
            .filter(ColumnDefinition::isUpdatable)
            .collect(Collectors.toList());
    }

    /**
     * Get a column definition by column name.
     */
    public ColumnDefinition<T> getColumn(String columnName) {
        return columnDefinitions.stream()
            .filter(col -> col.getColumnName().equals(columnName))
            .findFirst()
            .orElse(null);
    }

    /**
     * Generate a comma-separated list of column names for SELECT/INSERT.
     */
    public String getColumnNamesList() {
        return columnDefinitions.stream()
            .map(ColumnDefinition::getColumnName)
            .collect(Collectors.joining(", "));
    }

    /**
     * Generate a comma-separated list of insertable column names.
     */
    public String getInsertableColumnNamesList() {
        return getInsertableColumns().stream()
            .map(ColumnDefinition::getColumnName)
            .collect(Collectors.joining(", "));
    }

    /**
     * Generate a comma-separated list of parameter placeholders for INSERT.
     * Example: ":name, :email, :phoneNumber"
     */
    public String getInsertableParametersList() {
        return getInsertableColumns().stream()
            .map(col -> ":" + col.getJavaPropertyName())
            .collect(Collectors.joining(", "));
    }

    /**
     * Generate SET clause for UPDATE statements.
     * Example: "name = :name, email = :email, phone_number = :phoneNumber"
     */
    public String getUpdateSetClause() {
        return getUpdatableColumns().stream()
            .map(col -> col.getColumnName() + " = :" + col.getJavaPropertyName())
            .collect(Collectors.joining(", "));
    }

    /**
     * Create a new builder for TableDefinition.
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for TableDefinition.
     */
    public static class Builder<T> {
        private String tableName;
        private String idColumn = "id";
        private List<ColumnDefinition<T>> columnDefinitions = new ArrayList<>();

        /**
         * Set the table name.
         */
        public Builder<T> tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        /**
         * Set the ID column name (defaults to "id").
         */
        public Builder<T> idColumn(String idColumn) {
            this.idColumn = idColumn;
            return this;
        }

        /**
         * Add a column definition.
         */
        public Builder<T> addColumn(ColumnDefinition<T> columnDefinition) {
            this.columnDefinitions.add(columnDefinition);
            return this;
        }

        /**
         * Add multiple column definitions.
         */
        public Builder<T> addColumns(List<ColumnDefinition<T>> columnDefinitions) {
            this.columnDefinitions.addAll(columnDefinitions);
            return this;
        }

        /**
         * Build the TableDefinition.
         */
        public TableDefinition<T> build() {
            if (tableName == null) {
                throw new IllegalStateException("tableName is required");
            }
            if (idColumn == null) {
                throw new IllegalStateException("idColumn is required");
            }
            return new TableDefinition<>(this);
        }
    }
}

