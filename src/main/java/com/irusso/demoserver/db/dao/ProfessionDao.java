package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.PreparationLevel;
import com.irusso.demoserver.db.model.Profession;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the professions table.
 * Provides CRUD operations and batch import for Profession entities.
 */
public class ProfessionDao extends StandardDao<Profession, Long> {

    private static final String TABLE_NAME = "professions";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_NAME = "name";
    private static final String COL_PREP = "prep";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    /**
     * Create the table definition for professions.
     */
    private static TableDefinition<Profession> createTableDefinition() {
        return TableDefinition.<Profession>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<Profession>builder()
                .columnName(COL_NAME)
                .javaType(String.class)
                .nullable(false)
                .getter(Profession::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<Profession>builder()
                .columnName(COL_PREP)
                .javaType(Integer.class)
                .nullable(false)
                .getter(e -> e.getPrep() != null ? e.getPrep().getValue() : null)
                .setter((e, v) -> e.setPrep(v != null ? PreparationLevel.fromValue((Integer) v) : null))
                .build())
            .addColumn(ColumnDefinition.<Profession>builder()
                .columnName(COL_CREATED_AT)
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)
                .getter(e -> Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt(((Timestamp) v).toInstant()))
                .build())
            .addColumn(ColumnDefinition.<Profession>builder()
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
     * RowMapper to convert database rows to Profession objects.
     */
    private static final RowMapper<Profession> PROFESSION_MAPPER = (rs, ctx) -> {
        Profession profession = new Profession();
        profession.setId(rs.getLong(ID_COLUMN));
        profession.setName(rs.getString(COL_NAME));
        
        int prepValue = rs.getInt(COL_PREP);
        if (!rs.wasNull()) {
            profession.setPrep(PreparationLevel.fromValue(prepValue));
        }
        
        Timestamp createdAt = rs.getTimestamp(COL_CREATED_AT);
        if (createdAt != null) {
            profession.setCreatedAt(createdAt.toInstant());
        }
        
        Timestamp updatedAt = rs.getTimestamp(COL_UPDATED_AT);
        if (updatedAt != null) {
            profession.setUpdatedAt(updatedAt.toInstant());
        }
        
        return profession;
    };

    /**
     * Constructor for ProfessionDao.
     *
     * @param jdbi The JDBI instance for database access
     */
    @Inject
    public ProfessionDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), PROFESSION_MAPPER);
    }

    /**
     * Batch insert professions.
     * This method is optimized for bulk imports from CSV files.
     *
     * @param professions List of professions to insert
     * @return Number of professions inserted
     */
    public int batchInsert(List<Profession> professions) {
        if (professions == null || professions.isEmpty()) {
            return 0;
        }

        String sql = """
            INSERT INTO professions (name, prep, created_at, updated_at)
            VALUES (:name, :prep, :createdAt, :updatedAt)
            ON CONFLICT (name) DO UPDATE SET
                prep = EXCLUDED.prep,
                updated_at = EXCLUDED.updated_at
            """;

        return jdbi.withHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(sql);
            Timestamp now = Timestamp.from(Instant.now());

            for (Profession profession : professions) {
                batch.bind("name", profession.getName())
                    .bind("prep", profession.getPrep() != null ? profession.getPrep().getValue() : null)
                    .bind("createdAt", now)
                    .bind("updatedAt", now)
                    .add();
            }

            int[] results = batch.execute();
            return results.length;
        });
    }

    /**
     * Get all professions ordered by name.
     *
     * @return List of all professions
     */
    public List<Profession> getAllOrdered() {
        String sql = String.format(
            "SELECT * FROM %s ORDER BY %s ASC",
            TABLE_NAME, COL_NAME
        );
        return executeQuery(sql);
    }

    /**
     * Get professions by preparation level.
     *
     * @param prepLevel The preparation level
     * @return List of professions with the specified preparation level
     */
    public List<Profession> getByPreparationLevel(PreparationLevel prepLevel) {
        String sql = String.format(
            "SELECT * FROM %s WHERE %s = :prep ORDER BY %s ASC",
            TABLE_NAME, COL_PREP, COL_NAME
        );
        return executeQuery(sql, "prep", prepLevel.getValue());
    }

    /**
     * Search professions by name (case-insensitive partial match).
     *
     * @param searchTerm The search term
     * @return List of matching professions
     */
    public List<Profession> searchByName(String searchTerm) {
        String sql = String.format(
            "SELECT * FROM %s WHERE LOWER(%s) LIKE LOWER(:searchTerm) ORDER BY %s ASC",
            TABLE_NAME, COL_NAME, COL_NAME
        );
        return executeQuery(sql, "searchTerm", "%" + searchTerm + "%");
    }

    /**
     * Delete all professions (useful for re-importing).
     *
     * @return Number of professions deleted
     */
    public int deleteAll() {
        String sql = String.format("DELETE FROM %s", TABLE_NAME);
        return executeUpdate(sql);
    }
}

