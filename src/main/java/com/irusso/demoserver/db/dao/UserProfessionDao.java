package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserProfession;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_professions table.
 */
public class UserProfessionDao extends StandardDao<UserProfession, Long> {

    private static final String TABLE_NAME = "user_professions";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_PROFESSION_ID = "profession_id";
    private static final String COL_IS_CURRENT = "is_current";
    private static final String COL_YEARS_OF_EXPERIENCE = "years_of_experience";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<UserProfession> MAPPER = (rs, ctx) -> {
        UserProfession userProfession = new UserProfession();
        userProfession.setId(rs.getLong(ID_COLUMN));
        userProfession.setUserId(rs.getLong(COL_USER_ID));
        userProfession.setProfessionId(rs.getLong(COL_PROFESSION_ID));
        userProfession.setIsCurrent(rs.getBoolean(COL_IS_CURRENT));
        
        int yearsOfExperience = rs.getInt(COL_YEARS_OF_EXPERIENCE);
        if (!rs.wasNull()) {
            userProfession.setYearsOfExperience(yearsOfExperience);
        }
        
        userProfession.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        userProfession.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return userProfession;
    };

    @Inject
    public UserProfessionDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserProfession>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_USER_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserProfession::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_PROFESSION_ID)
                    .javaType(Long.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserProfession::getProfessionId)
                    .build())
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_IS_CURRENT)
                    .javaType(Boolean.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserProfession::getIsCurrent)
                    .build())
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_YEARS_OF_EXPERIENCE)
                    .javaType(Integer.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserProfession::getYearsOfExperience)
                    .build())
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_CREATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(false)
                    .getter(p -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<UserProfession>builder()
                    .columnName(COL_UPDATED_AT)
                    .javaType(Timestamp.class)
                    .insertable(true)
                    .updatable(true)
                    .getter(p -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    /**
     * Find all professions for a specific user.
     */
    public List<UserProfession> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_professions WHERE user_id = :userId ORDER BY is_current DESC, years_of_experience DESC";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find current professions for a specific user.
     */
    public List<UserProfession> findCurrentByUserId(Long userId) {
        String sql = "SELECT * FROM user_professions WHERE user_id = :userId AND is_current = true";
        return executeQuery(sql, "userId", userId);
    }
    
    /**
     * Find all users associated with a specific profession.
     */
    public List<UserProfession> findByProfessionId(Long professionId) {
        String sql = "SELECT * FROM user_professions WHERE profession_id = :professionId ORDER BY is_current DESC, years_of_experience DESC";
        return executeQuery(sql, "professionId", professionId);
    }
    
    /**
     * Delete all professions for a specific user.
     */
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_professions WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

