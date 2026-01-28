package com.irusso.demoserver.db.dao;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.Query;
import org.jdbi.v3.core.statement.Update;

import java.util.List;
import java.util.Optional;

/**
 * Abstract base class for Data Access Objects providing standard CRUD operations.
 *
 * This class provides generic database operations for any table using JDBI3.
 * Subclasses can use TableDefinition for automatic INSERT/UPDATE generation.
 *
 * @param <T> The entity type this DAO manages
 * @param <ID> The type of the entity's primary key
 */
public abstract class StandardDao<T, ID> {

    protected final Jdbi jdbi;
    private final TableDefinition<T> tableDefinition;
    private final RowMapper<T> rowMapper;

    /**
     * Constructor for StandardDao with TableDefinition.
     *
     * @param jdbi The JDBI instance for database access
     * @param tableDefinition The table definition with column metadata
     * @param rowMapper The RowMapper to convert ResultSet rows to entity objects
     */
    protected StandardDao(Jdbi jdbi, TableDefinition<T> tableDefinition, RowMapper<T> rowMapper) {
        this.jdbi = jdbi;
        this.tableDefinition = tableDefinition;
        this.rowMapper = rowMapper;
    }

    /**
     * Find an entity by its ID.
     *
     * @param id The primary key value
     * @return Optional containing the entity if found, empty otherwise
     */
    public Optional<T> findById(ID id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = :id",
            tableDefinition.getTableName(), tableDefinition.getIdColumn());

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind("id", id)
                .map(rowMapper)
                .findFirst()
        );
    }

    /**
     * Find all entities in the table.
     *
     * @return List of all entities
     */
    public List<T> findAll() {
        String sql = String.format("SELECT * FROM %s ORDER BY %s",
            tableDefinition.getTableName(), tableDefinition.getIdColumn());

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .map(rowMapper)
                .list()
        );
    }

    /**
     * Find entities with pagination.
     *
     * @param limit Maximum number of results to return
     * @param offset Number of results to skip
     * @return List of entities for the requested page
     */
    public List<T> findAll(int limit, int offset) {
        String sql = String.format(
            "SELECT * FROM %s ORDER BY %s LIMIT :limit OFFSET :offset",
            tableDefinition.getTableName(), tableDefinition.getIdColumn()
        );

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .bind("limit", limit)
                .bind("offset", offset)
                .map(rowMapper)
                .list()
        );
    }

    /**
     * Count total number of entities in the table.
     *
     * @return Total count of entities
     */
    public long count() {
        String sql = String.format("SELECT COUNT(*) FROM %s", tableDefinition.getTableName());

        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .mapTo(Long.class)
                .one()
        );
    }

    /**
     * Insert a new entity into the database using the table definition.
     * This method automatically generates the INSERT statement based on column definitions.
     *
     * @param entity The entity to insert
     * @return The ID of the newly inserted entity
     */
    @SuppressWarnings("unchecked")
    public ID insert(T entity) {
        String sql = String.format(
            "INSERT INTO %s (%s) VALUES (%s) RETURNING %s",
            tableDefinition.getTableName(),
            tableDefinition.getInsertableColumnNamesList(),
            tableDefinition.getInsertableParametersList(),
            tableDefinition.getIdColumn()
        );

        return jdbi.withHandle(handle -> {
            Query query = handle.createQuery(sql);

            // Bind all insertable column values
            for (ColumnDefinition<T> column : tableDefinition.getInsertableColumns()) {
                Object value = column.getValue(entity);
                query.bind(column.getJavaPropertyName(), value);
            }

            return (ID) query.mapTo(Long.class).one();
        });
    }

    /**
     * Update an existing entity in the database using the table definition.
     * This method automatically generates the UPDATE statement based on column definitions.
     *
     * @param id The ID of the entity to update
     * @param entity The entity with updated values
     * @return true if the update was successful, false otherwise
     */
    public boolean update(ID id, T entity) {
        String sql = String.format(
            "UPDATE %s SET %s WHERE %s = :id",
            tableDefinition.getTableName(),
            tableDefinition.getUpdateSetClause(),
            tableDefinition.getIdColumn()
        );

        return jdbi.withHandle(handle -> {
            Update update = handle.createUpdate(sql);
            update.bind("id", id);

            // Bind all updatable column values
            for (ColumnDefinition<T> column : tableDefinition.getUpdatableColumns()) {
                Object value = column.getValue(entity);
                update.bind(column.getJavaPropertyName(), value);
            }

            int rowsAffected = update.execute();
            return rowsAffected > 0;
        });
    }

    /**
     * Delete an entity by its ID.
     *
     * @param id The ID of the entity to delete
     * @return true if the entity was deleted, false if not found
     */
    public boolean delete(ID id) {
        String sql = String.format("DELETE FROM %s WHERE %s = :id",
            tableDefinition.getTableName(), tableDefinition.getIdColumn());

        return jdbi.withHandle(handle -> {
            int rowsAffected = handle.createUpdate(sql)
                .bind("id", id)
                .execute();
            return rowsAffected > 0;
        });
    }

    /**
     * Check if an entity exists by its ID.
     *
     * @param id The ID to check
     * @return true if an entity with this ID exists, false otherwise
     */
    public boolean exists(ID id) {
        String sql = String.format("SELECT COUNT(*) FROM %s WHERE %s = :id",
            tableDefinition.getTableName(), tableDefinition.getIdColumn());

        return jdbi.withHandle(handle -> {
            long count = handle.createQuery(sql)
                .bind("id", id)
                .mapTo(Long.class)
                .one();
            return count > 0;
        });
    }

    /**
     * Delete all entities from the table.
     * WARNING: This will delete all data in the table!
     *
     * @return The number of entities deleted
     */
    public int deleteAll() {
        String sql = String.format("DELETE FROM %s", tableDefinition.getTableName());

        return jdbi.withHandle(handle ->
            handle.createUpdate(sql).execute()
        );
    }

    /**
     * Execute a custom query and return a list of results.
     * 
     * @param sql The SQL query to execute
     * @return List of entities matching the query
     */
    protected List<T> executeQuery(String sql) {
        return jdbi.withHandle(handle ->
            handle.createQuery(sql)
                .map(rowMapper)
                .list()
        );
    }

    /**
     * Execute a custom query with parameters and return a list of results.
     *
     * @param sql The SQL query to execute
     * @param params Parameters to bind to the query (alternating key-value pairs)
     * @return List of entities matching the query
     */
    protected List<T> executeQuery(String sql, Object... params) {
        return jdbi.withHandle(handle -> {
            var query = handle.createQuery(sql);

            // Bind parameters in pairs (key, value)
            for (int i = 0; i < params.length; i += 2) {
                if (i + 1 < params.length) {
                    query.bind((String) params[i], params[i + 1]);
                }
            }

            return query.map(rowMapper).list();
        });
    }

    /**
     * Execute a custom update/insert/delete statement.
     * 
     * @param sql The SQL statement to execute
     * @param params Parameters to bind to the statement (alternating key-value pairs)
     * @return The number of rows affected
     */
    protected int executeUpdate(String sql, Object... params) {
        return jdbi.withHandle(handle -> {
            Update update = handle.createUpdate(sql);
            
            // Bind parameters in pairs (key, value)
            for (int i = 0; i < params.length; i += 2) {
                if (i + 1 < params.length) {
                    update.bind((String) params[i], params[i + 1]);
                }
            }
            
            return update.execute();
        });
    }

    /**
     * Get the table definition for this DAO.
     *
     * @return The table definition
     */
    protected TableDefinition<T> getTableDefinition() {
        return tableDefinition;
    }

    /**
     * Get the table name for this DAO.
     *
     * @return The table name
     */
    protected String getTableName() {
        return tableDefinition.getTableName();
    }

    /**
     * Get the ID column name for this DAO.
     *
     * @return The ID column name
     */
    protected String getIdColumn() {
        return tableDefinition.getIdColumn();
    }

    /**
     * Get the JDBI instance.
     *
     * @return The JDBI instance
     */
    protected Jdbi getJdbi() {
        return jdbi;
    }
}

