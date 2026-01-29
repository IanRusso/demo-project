package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.SavedJob;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the saved_jobs table.
 */
public class SavedJobDao extends StandardDao<SavedJob, Long> {

    private static final String TABLE_NAME = "saved_jobs";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_JOB_POSTING_ID = "job_posting_id";
    private static final String COL_SAVED_AT = "saved_at";
    private static final String COL_NOTES = "notes";

    private static final RowMapper<SavedJob> MAPPER = (rs, ctx) -> {
        SavedJob saved = new SavedJob();
        saved.setId(rs.getLong(ID_COLUMN));
        saved.setUserId(rs.getLong(COL_USER_ID));
        saved.setJobPostingId(rs.getLong(COL_JOB_POSTING_ID));
        saved.setSavedAt(rs.getTimestamp(COL_SAVED_AT));
        saved.setNotes(rs.getString(COL_NOTES));
        return saved;
    };

    @Inject
    public SavedJobDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<SavedJob>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<SavedJob>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(SavedJob::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<SavedJob>builder()
                    .columnName(COL_JOB_POSTING_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(SavedJob::getJobPostingId)
                    .build())
                .addColumn(ColumnDefinition.<SavedJob>builder()
                    .columnName(COL_SAVED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(s -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<SavedJob>builder()
                    .columnName(COL_NOTES)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(SavedJob::getNotes)
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<SavedJob> findByUserId(Long userId) {
        String sql = "SELECT * FROM saved_jobs WHERE user_id = :userId ORDER BY saved_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<SavedJob> findByJobPostingId(Long jobPostingId) {
        String sql = "SELECT * FROM saved_jobs WHERE job_posting_id = :jobPostingId";
        return executeQuery(sql, "jobPostingId", jobPostingId);
    }
    
    public boolean isSaved(Long userId, Long jobPostingId) {
        String sql = "SELECT COUNT(*) FROM saved_jobs WHERE user_id = :userId AND job_posting_id = :jobPostingId";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                .bind("userId", userId)
                .bind("jobPostingId", jobPostingId)
                .mapTo(Integer.class)
                .one() > 0
        );
    }
    
    public int deleteByUserAndJob(Long userId, Long jobPostingId) {
        String sql = "DELETE FROM saved_jobs WHERE user_id = :userId AND job_posting_id = :jobPostingId";
        return executeUpdate(sql, "userId", userId, "jobPostingId", jobPostingId);
    }
}

