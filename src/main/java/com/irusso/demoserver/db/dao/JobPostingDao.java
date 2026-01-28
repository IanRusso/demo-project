package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.JobPosting;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the job_postings table.
 */
public class JobPostingDao extends StandardDao<JobPosting, Long> {

    private static final String TABLE_NAME = "job_postings";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_EMPLOYER_ID = "employer_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_RESPONSIBILITIES = "responsibilities";
    private static final String COL_LOCATION = "location";
    private static final String COL_FIELD = "field";
    private static final String COL_EXPERIENCE_LEVEL = "experience_level";
    private static final String COL_SALARY_MIN = "salary_min";
    private static final String COL_SALARY_MAX = "salary_max";
    private static final String COL_STATUS = "status";
    private static final String COL_POSTED_DATE = "posted_date";
    private static final String COL_CLOSED_DATE = "closed_date";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<JobPosting> MAPPER = (rs, ctx) -> {
        JobPosting job = new JobPosting();
        job.setId(rs.getLong(ID_COLUMN));
        job.setEmployerId(rs.getLong(COL_EMPLOYER_ID));
        job.setTitle(rs.getString(COL_TITLE));
        job.setDescription(rs.getString(COL_DESCRIPTION));
        job.setResponsibilities(rs.getString(COL_RESPONSIBILITIES));
        job.setLocation(rs.getString(COL_LOCATION));
        job.setField(rs.getString(COL_FIELD));
        job.setExperienceLevel(rs.getString(COL_EXPERIENCE_LEVEL));
        job.setSalaryMin(rs.getBigDecimal(COL_SALARY_MIN));
        job.setSalaryMax(rs.getBigDecimal(COL_SALARY_MAX));
        job.setStatus(rs.getString(COL_STATUS));
        job.setPostedDate(rs.getTimestamp(COL_POSTED_DATE));
        job.setClosedDate(rs.getTimestamp(COL_CLOSED_DATE));
        job.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        job.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return job;
    };
    
    @Inject
    public JobPostingDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<JobPosting>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_EMPLOYER_ID)
                    .insertable(true)
                    .updatable(false)
                    .getter(JobPosting::getEmployerId)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_TITLE)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getTitle)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_DESCRIPTION)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getDescription)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_RESPONSIBILITIES)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getResponsibilities)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_LOCATION)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getLocation)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_FIELD)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getField)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_EXPERIENCE_LEVEL)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getExperienceLevel)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_SALARY_MIN)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getSalaryMin)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_SALARY_MAX)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getSalaryMax)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_STATUS)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getStatus)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_POSTED_DATE)
                    .insertable(true)
                    .updatable(false)
                    .getter(j -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_CLOSED_DATE)
                    .insertable(true)
                    .updatable(true)
                    .getter(JobPosting::getClosedDate)
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_CREATED_AT)
                    .insertable(true)
                    .updatable(false)
                    .getter(j -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<JobPosting>builder()
                    .columnName(COL_UPDATED_AT)
                    .insertable(true)
                    .updatable(true)
                    .getter(j -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<JobPosting> findByEmployerId(Long employerId) {
        String sql = "SELECT * FROM job_postings WHERE employer_id = :employerId ORDER BY posted_date DESC";
        return executeQuery(sql, "employerId", employerId);
    }
    
    public List<JobPosting> findActiveJobs() {
        String sql = "SELECT * FROM job_postings WHERE status = 'ACTIVE' ORDER BY posted_date DESC";
        return executeQuery(sql);
    }
    
    public List<JobPosting> findByStatus(String status) {
        String sql = "SELECT * FROM job_postings WHERE status = :status ORDER BY posted_date DESC";
        return executeQuery(sql, "status", status);
    }
    
    public List<JobPosting> findByField(String field) {
        String sql = "SELECT * FROM job_postings WHERE field ILIKE :field AND status = 'ACTIVE' ORDER BY posted_date DESC";
        return executeQuery(sql, "field", "%" + field + "%");
    }
    
    public List<JobPosting> findByLocation(String location) {
        String sql = "SELECT * FROM job_postings WHERE location ILIKE :location AND status = 'ACTIVE' ORDER BY posted_date DESC";
        return executeQuery(sql, "location", "%" + location + "%");
    }
    
    public boolean updateStatus(Long id, String status) {
        String sql = """
            UPDATE job_postings 
            SET status = :status, 
                closed_date = CASE WHEN :status IN ('CLOSED', 'FILLED') THEN :closedDate ELSE closed_date END,
                updated_at = :updatedAt 
            WHERE id = :id
            """;
        
        int rowsAffected = executeUpdate(sql, 
            "id", id, 
            "status", status,
            "closedDate", Timestamp.from(Instant.now()),
            "updatedAt", Timestamp.from(Instant.now())
        );
        return rowsAffected > 0;
    }
}

