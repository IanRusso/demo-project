package com.irusso.demoserver.db.dao;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Defines a column in a database table with metadata for generic operations.
 * 
 * @param <T> The entity type
 */
public class ColumnDefinition<T> {

    private final String columnName;
    private final String javaPropertyName;
    private final Class<?> javaType;
    private final boolean nullable;
    private final boolean insertable;
    private final boolean updatable;
    private final Function<T, Object> getter;
    private final BiConsumer<T, Object> setter;

    private ColumnDefinition(Builder<T> builder) {
        this.columnName = builder.columnName;
        this.javaPropertyName = builder.javaPropertyName;
        this.javaType = builder.javaType;
        this.nullable = builder.nullable;
        this.insertable = builder.insertable;
        this.updatable = builder.updatable;
        this.getter = builder.getter;
        this.setter = builder.setter;
    }

    /**
     * Get the database column name.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Get the Java property name (for binding parameters).
     */
    public String getJavaPropertyName() {
        return javaPropertyName;
    }

    /**
     * Get the Java type of this column.
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * Check if this column can be null.
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Check if this column should be included in INSERT statements.
     */
    public boolean isInsertable() {
        return insertable;
    }

    /**
     * Check if this column should be included in UPDATE statements.
     */
    public boolean isUpdatable() {
        return updatable;
    }

    /**
     * Get the value of this column from an entity.
     */
    public Object getValue(T entity) {
        return getter != null ? getter.apply(entity) : null;
    }

    /**
     * Set the value of this column on an entity.
     */
    public void setValue(T entity, Object value) {
        if (setter != null) {
            setter.accept(entity, value);
        }
    }

    /**
     * Create a new builder for ColumnDefinition.
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    /**
     * Builder for ColumnDefinition.
     */
    public static class Builder<T> {
        private String columnName;
        private String javaPropertyName;
        private Class<?> javaType;
        private boolean nullable = true;
        private boolean insertable = true;
        private boolean updatable = true;
        private Function<T, Object> getter;
        private BiConsumer<T, Object> setter;

        /**
         * Set the database column name.
         */
        public Builder<T> columnName(String columnName) {
            this.columnName = columnName;
            return this;
        }

        /**
         * Set the Java property name (defaults to camelCase of column name).
         */
        public Builder<T> javaPropertyName(String javaPropertyName) {
            this.javaPropertyName = javaPropertyName;
            return this;
        }

        /**
         * Set the Java type.
         */
        public Builder<T> javaType(Class<?> javaType) {
            this.javaType = javaType;
            return this;
        }

        /**
         * Set whether this column can be null.
         */
        public Builder<T> nullable(boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        /**
         * Set whether this column should be included in INSERT statements.
         */
        public Builder<T> insertable(boolean insertable) {
            this.insertable = insertable;
            return this;
        }

        /**
         * Set whether this column should be included in UPDATE statements.
         */
        public Builder<T> updatable(boolean updatable) {
            this.updatable = updatable;
            return this;
        }

        /**
         * Set the getter function to extract value from entity.
         */
        public Builder<T> getter(Function<T, Object> getter) {
            this.getter = getter;
            return this;
        }

        /**
         * Set the setter function to set value on entity.
         */
        public Builder<T> setter(BiConsumer<T, Object> setter) {
            this.setter = setter;
            return this;
        }

        /**
         * Build the ColumnDefinition.
         */
        public ColumnDefinition<T> build() {
            if (columnName == null) {
                throw new IllegalStateException("columnName is required");
            }
            if (javaPropertyName == null) {
                // Default to camelCase conversion of column name
                javaPropertyName = toCamelCase(columnName);
            }
            if (javaType == null) {
                throw new IllegalStateException("javaType is required");
            }
            return new ColumnDefinition<>(this);
        }

        /**
         * Convert snake_case to camelCase.
         */
        private String toCamelCase(String snakeCase) {
            StringBuilder result = new StringBuilder();
            boolean capitalizeNext = false;
            
            for (char c : snakeCase.toCharArray()) {
                if (c == '_') {
                    capitalizeNext = true;
                } else {
                    if (capitalizeNext) {
                        result.append(Character.toUpperCase(c));
                        capitalizeNext = false;
                    } else {
                        result.append(c);
                    }
                }
            }
            
            return result.toString();
        }
    }
}

