package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.JobRequirement;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the job_requirements table.
 */
public class JobRequirementDao extends StandardDao<JobRequirement, Long> {

    private static final String TABLE_NAME = "job_requirements";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_JOB_POSTING_ID = "job_posting_id";
    private static final String COL_REQUIREMENT_TYPE = "requirement_type";
    private static final String COL_REQUIREMENT_CATEGORY = "requirement_category";
    private static final String COL_REQUIREMENT_TEXT = "requirement_text";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<JobRequirement> MAPPER = (rs, ctx) -> {
        JobRequirement req = new JobRequirement();
        req.setId(rs.getLong(ID_COLUMN));
        req.setJobPostingId(rs.getLong(COL_JOB_POSTING_ID));
        req.setRequirementType(rs.getString(COL_REQUIREMENT_TYPE));
        req.setRequirementCategory(rs.getString(COL_REQUIREMENT_CATEGORY));
        req.setRequirementText(rs.getString(COL_REQUIREMENT_TEXT));
        req.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return req;
    };

    @Inject
    public JobRequirementDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<JobRequirement>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<JobRequirement>builder()
                    .columnName(COL_JOB_POSTING_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(JobRequirement::getJobPostingId)
                    .build())
                .addColumn(ColumnDefinition.<JobRequirement>builder()
                    .columnName(COL_REQUIREMENT_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobRequirement::getRequirementType)
                    .build())
                .addColumn(ColumnDefinition.<JobRequirement>builder()
                    .columnName(COL_REQUIREMENT_CATEGORY)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobRequirement::getRequirementCategory)
                    .build())
                .addColumn(ColumnDefinition.<JobRequirement>builder()
                    .columnName(COL_REQUIREMENT_TEXT)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobRequirement::getRequirementText)
                    .build())
                .addColumn(ColumnDefinition.<JobRequirement>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(r -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<JobRequirement> findByJobPostingId(Long jobPostingId) {
        String sql = "SELECT * FROM job_requirements WHERE job_posting_id = :jobPostingId ORDER BY requirement_type, requirement_category";
        return executeQuery(sql, "jobPostingId", jobPostingId);
    }
    
    public List<JobRequirement> findHardRequirements(Long jobPostingId) {
        String sql = "SELECT * FROM job_requirements WHERE job_posting_id = :jobPostingId AND requirement_type = 'HARD'";
        return executeQuery(sql, "jobPostingId", jobPostingId);
    }
    
    public int deleteByJobPostingId(Long jobPostingId) {
        String sql = "DELETE FROM job_requirements WHERE job_posting_id = :jobPostingId";
        return executeUpdate(sql, "jobPostingId", jobPostingId);
    }
}

