package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.Industry;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the industries table.
 * Provides CRUD operations and batch import for Industry entities.
 */
public class IndustryDao extends StandardDao<Industry, Long> {

    private static final String TABLE_NAME = "industries";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_NAME = "name";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    /**
     * Create the table definition for industries.
     */
    private static TableDefinition<Industry> createTableDefinition() {
        return TableDefinition.<Industry>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<Industry>builder()
                .columnName(COL_NAME)
                .javaType(String.class)
                .nullable(false)
                .getter(Industry::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<Industry>builder()
                .columnName(COL_CREATED_AT)
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)
                .getter(e -> Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt(((Timestamp) v).toInstant()))
                .build())
            .addColumn(ColumnDefinition.<Industry>builder()
                .columnName(COL_UPDATED_AT)
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(true)
                .getter(e -> Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setUpdatedAt(((Timestamp) v).toInstant()))
                .build())
            .build();
    }

    /**
     * RowMapper to convert database rows to Industry objects.
     */
    private static final RowMapper<Industry> INDUSTRY_MAPPER = (rs, ctx) -> {
        Industry industry = new Industry();
        industry.setId(rs.getLong(ID_COLUMN));
        industry.setName(rs.getString(COL_NAME));
        
        Timestamp createdAt = rs.getTimestamp(COL_CREATED_AT);
        if (createdAt != null) {
            industry.setCreatedAt(createdAt.toInstant());
        }
        
        Timestamp updatedAt = rs.getTimestamp(COL_UPDATED_AT);
        if (updatedAt != null) {
            industry.setUpdatedAt(updatedAt.toInstant());
        }
        
        return industry;
    };

    /**
     * Constructor for IndustryDao.
     *
     * @param jdbi The JDBI instance for database access
     */
    @Inject
    public IndustryDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), INDUSTRY_MAPPER);
    }

    /**
     * Batch insert industries.
     * This method is optimized for bulk imports from CSV files.
     *
     * @param industries List of industries to insert
     * @return Number of industries inserted
     */
    public int batchInsert(List<Industry> industries) {
        if (industries == null || industries.isEmpty()) {
            return 0;
        }

        String sql = """
            INSERT INTO industries (name, created_at, updated_at)
            VALUES (:name, :createdAt, :updatedAt)
            ON CONFLICT (name) DO UPDATE SET
                updated_at = EXCLUDED.updated_at
            """;

        return jdbi.withHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(sql);
            Timestamp now = Timestamp.from(Instant.now());

            for (Industry industry : industries) {
                batch.bind("name", industry.getName())
                    .bind("createdAt", now)
                    .bind("updatedAt", now)
                    .add();
            }

            int[] results = batch.execute();
            return results.length;
        });
    }

    /**
     * Get all industries ordered by name.
     *
     * @return List of all industries
     */
    public List<Industry> getAllOrdered() {
        String sql = String.format(
            "SELECT * FROM %s ORDER BY %s ASC",
            TABLE_NAME, COL_NAME
        );
        return executeQuery(sql);
    }

    /**
     * Search industries by name (case-insensitive partial match).
     *
     * @param searchTerm The search term
     * @return List of matching industries
     */
    public List<Industry> searchByName(String searchTerm) {
        String sql = String.format(
            "SELECT * FROM %s WHERE LOWER(%s) LIKE LOWER(:searchTerm) ORDER BY %s ASC",
            TABLE_NAME, COL_NAME, COL_NAME
        );
        return executeQuery(sql, "searchTerm", "%" + searchTerm + "%");
    }

    /**
     * Delete all industries (useful for re-importing).
     *
     * @return Number of industries deleted
     */
    public int deleteAll() {
        String sql = String.format("DELETE FROM %s", TABLE_NAME);
        return executeUpdate(sql);
    }
}

