package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.Application;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the applications table.
 */
public class ApplicationDao extends StandardDao<Application, Long> {

    private static final String TABLE_NAME = "applications";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_JOB_POSTING_ID = "job_posting_id";
    private static final String COL_COVER_LETTER = "cover_letter";
    private static final String COL_STATUS = "status";
    private static final String COL_APPLIED_AT = "applied_at";
    private static final String COL_REVIEWED_AT = "reviewed_at";
    private static final String COL_RESPONSE_DEADLINE = "response_deadline";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<Application> MAPPER = (rs, ctx) -> {
        Application app = new Application();
        app.setId(rs.getLong(ID_COLUMN));
        app.setUserId(rs.getLong(COL_USER_ID));
        app.setJobPostingId(rs.getLong(COL_JOB_POSTING_ID));
        app.setCoverLetter(rs.getString(COL_COVER_LETTER));
        app.setStatus(rs.getString(COL_STATUS));
        app.setAppliedAt(rs.getTimestamp(COL_APPLIED_AT));
        app.setReviewedAt(rs.getTimestamp(COL_REVIEWED_AT));
        app.setResponseDeadline(rs.getTimestamp(COL_RESPONSE_DEADLINE));
        app.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        app.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return app;
    };
    
    @Inject
    public ApplicationDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<Application>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(Application::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_JOB_POSTING_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(Application::getJobPostingId)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_COVER_LETTER)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(Application::getCoverLetter)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_STATUS)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(Application::getStatus)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_APPLIED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(a -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_REVIEWED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(Application::getReviewedAt)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_RESPONSE_DEADLINE)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(Application::getResponseDeadline)
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(a -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<Application>builder()
                    .columnName(COL_UPDATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(a -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<Application> findByUserId(Long userId) {
        String sql = "SELECT * FROM applications WHERE user_id = :userId ORDER BY applied_at DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<Application> findByJobPostingId(Long jobPostingId) {
        String sql = "SELECT * FROM applications WHERE job_posting_id = :jobPostingId ORDER BY applied_at DESC";
        return executeQuery(sql, "jobPostingId", jobPostingId);
    }
    
    public List<Application> findByStatus(String status) {
        String sql = "SELECT * FROM applications WHERE status = :status ORDER BY applied_at DESC";
        return executeQuery(sql, "status", status);
    }
    
    public List<Application> findByUserAndStatus(Long userId, String status) {
        String sql = "SELECT * FROM applications WHERE user_id = :userId AND status = :status ORDER BY applied_at DESC";
        return executeQuery(sql, "userId", userId, "status", status);
    }
    
    public boolean updateStatus(Long id, String status) {
        String sql = """
            UPDATE applications 
            SET status = :status, 
                reviewed_at = CASE WHEN :status != 'SUBMITTED' THEN :reviewedAt ELSE reviewed_at END,
                updated_at = :updatedAt 
            WHERE id = :id
            """;
        
        int rowsAffected = executeUpdate(sql, 
            "id", id, 
            "status", status,
            "reviewedAt", Timestamp.from(Instant.now()),
            "updatedAt", Timestamp.from(Instant.now())
        );
        return rowsAffected > 0;
    }
    
    public boolean hasApplied(Long userId, Long jobPostingId) {
        String sql = "SELECT COUNT(*) FROM applications WHERE user_id = :userId AND job_posting_id = :jobPostingId";
        return jdbi.withHandle(handle -> 
            handle.createQuery(sql)
                .bind("userId", userId)
                .bind("jobPostingId", jobPostingId)
                .mapTo(Integer.class)
                .one() > 0
        );
    }
}

