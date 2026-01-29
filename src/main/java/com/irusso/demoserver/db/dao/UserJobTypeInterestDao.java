package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserJobTypeInterest;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_job_type_interests table.
 */
public class UserJobTypeInterestDao extends StandardDao<UserJobTypeInterest, Long> {

    private static final String TABLE_NAME = "user_job_type_interests";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_JOB_TYPE = "job_type";
    private static final String COL_IS_HARD_REQUIREMENT = "is_hard_requirement";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<UserJobTypeInterest> MAPPER = (rs, ctx) -> {
        UserJobTypeInterest interest = new UserJobTypeInterest();
        interest.setId(rs.getLong(ID_COLUMN));
        interest.setUserId(rs.getLong(COL_USER_ID));
        interest.setJobType(rs.getString(COL_JOB_TYPE));
        interest.setIsHardRequirement(rs.getBoolean(COL_IS_HARD_REQUIREMENT));
        interest.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return interest;
    };

    @Inject
    public UserJobTypeInterestDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserJobTypeInterest>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserJobTypeInterest>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserJobTypeInterest::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserJobTypeInterest>builder()
                    .columnName(COL_JOB_TYPE)
                    .javaType(String.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserJobTypeInterest::getJobType)
                    .build())
                .addColumn(ColumnDefinition.<UserJobTypeInterest>builder()
                    .columnName(COL_IS_HARD_REQUIREMENT)
                    .javaType(Boolean.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserJobTypeInterest::getIsHardRequirement)
                    .build())
                .addColumn(ColumnDefinition.<UserJobTypeInterest>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(i -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<UserJobTypeInterest> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_job_type_interests WHERE user_id = :userId ORDER BY job_type";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<UserJobTypeInterest> findHardRequirementsByUserId(Long userId) {
        String sql = "SELECT * FROM user_job_type_interests WHERE user_id = :userId AND is_hard_requirement = true";
        return executeQuery(sql, "userId", userId);
    }
    
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_job_type_interests WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

