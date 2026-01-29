package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.EmploymentHistory;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the employment_history table.
 */
public class EmploymentHistoryDao extends StandardDao<EmploymentHistory, Long> {

    private static final String TABLE_NAME = "employment_history";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_EMPLOYER_NAME = "employer_name";
    private static final String COL_JOB_TITLE = "job_title";
    private static final String COL_LOCATION = "location";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_IS_CURRENT = "is_current";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<EmploymentHistory> MAPPER = (rs, ctx) -> {
        EmploymentHistory history = new EmploymentHistory();
        history.setId(rs.getLong(ID_COLUMN));
        history.setUserId(rs.getLong(COL_USER_ID));
        history.setEmployerName(rs.getString(COL_EMPLOYER_NAME));
        history.setJobTitle(rs.getString(COL_JOB_TITLE));
        history.setLocation(rs.getString(COL_LOCATION));
        history.setStartDate(rs.getDate(COL_START_DATE));
        history.setEndDate(rs.getDate(COL_END_DATE));
        history.setIsCurrent(rs.getBoolean(COL_IS_CURRENT));
        history.setDescription(rs.getString(COL_DESCRIPTION));
        history.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        history.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return history;
    };
    
    @Inject
    public EmploymentHistoryDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<EmploymentHistory>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(EmploymentHistory::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_EMPLOYER_NAME)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getEmployerName)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_JOB_TITLE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getJobTitle)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_LOCATION)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getLocation)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_START_DATE)
                    .javaType(Date.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getStartDate)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_END_DATE)
                    .javaType(Date.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getEndDate)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_IS_CURRENT)
                    .javaType(Boolean.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getIsCurrent)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_DESCRIPTION)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(EmploymentHistory::getDescription)
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(h -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<EmploymentHistory>builder()
                    .columnName(COL_UPDATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(h -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<EmploymentHistory> findByUserId(Long userId) {
        String sql = "SELECT * FROM employment_history WHERE user_id = :userId ORDER BY start_date DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<EmploymentHistory> findCurrentByUserId(Long userId) {
        String sql = "SELECT * FROM employment_history WHERE user_id = :userId AND is_current = true";
        return executeQuery(sql, "userId", userId);
    }
    
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM employment_history WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

