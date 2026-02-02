package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserIndustry;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_industries table.
 */
public class UserIndustryDao extends StandardDao<UserIndustry, Long> {

    private static final String TABLE_NAME = "user_industries";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_INDUSTRY_ID = "industry_id";
    private static final String COL_IS_CURRENT = "is_current";
    private static final String COL_YEARS_OF_EXPERIENCE = "years_of_experience";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<UserIndustry> MAPPER = (rs, ctx) -> {
        UserIndustry userIndustry = new UserIndustry();
        userIndustry.setId(rs.getLong(ID_COLUMN));
        userIndustry.setUserId(rs.getLong(COL_USER_ID));
        userIndustry.setIndustryId(rs.getLong(COL_INDUSTRY_ID));
        userIndustry.setIsCurrent(rs.getBoolean(COL_IS_CURRENT));
        
        int yearsOfExperience = rs.getInt(COL_YEARS_OF_EXPERIENCE);
        if (!rs.wasNull()) {
            userIndustry.setYearsOfExperience(yearsOfExperience);
        }
        
        userIndustry.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        userIndustry.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return userIndustry;
    };

    @Inject
    public UserIndustryDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserIndustry>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserIndustry::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_INDUSTRY_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserIndustry::getIndustryId)
                    .build())
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_IS_CURRENT)
                    .javaType(Boolean.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserIndustry::getIsCurrent)
                    .build())
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_YEARS_OF_EXPERIENCE)
                    .javaType(Integer.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserIndustry::getYearsOfExperience)
                    .build())
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(i -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<UserIndustry>builder()
                    .columnName(COL_UPDATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(i -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    /**
     * Find all industries for a specific user.
     */
    public List<UserIndustry> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_industries WHERE user_id = :userId ORDER BY is_current DESC, years_of_experience DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find current industries for a specific user.
     */
    public List<UserIndustry> findCurrentByUserId(Long userId) {
        String sql = "SELECT * FROM user_industries WHERE user_id = :userId AND is_current = true";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find all users associated with a specific industry.
     */
    public List<UserIndustry> findByIndustryId(Long industryId) {
        String sql = "SELECT * FROM user_industries WHERE industry_id = :industryId ORDER BY is_current DESC, years_of_experience DESC";
        return executeQuery(sql, "industryId", industryId);
    }
    
    /**
     * Delete all industries for a specific user.
     */
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_industries WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

