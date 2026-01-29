package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.irusso.demoserver.db.model.UserExperience;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for UserExperience entities.
 * Provides CRUD operations and custom queries for user experiences.
 */
@Singleton
public class UserExperienceDao extends StandardDao<UserExperience, Long> {

    private static final String TABLE_NAME = "user_experiences";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_TITLE = "title";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_EXPERIENCE_TYPE = "experience_type";
    private static final String COL_START_DATE = "start_date";
    private static final String COL_END_DATE = "end_date";
    private static final String COL_IS_CURRENT = "is_current";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<UserExperience> USER_EXPERIENCE_MAPPER = (rs, ctx) -> {
        UserExperience experience = new UserExperience();
        experience.setId(rs.getLong(ID_COLUMN));
        experience.setUserId(rs.getLong(COL_USER_ID));
        experience.setTitle(rs.getString(COL_TITLE));
        experience.setDescription(rs.getString(COL_DESCRIPTION));
        experience.setExperienceType(rs.getString(COL_EXPERIENCE_TYPE));
        experience.setStartDate(rs.getDate(COL_START_DATE));
        experience.setEndDate(rs.getDate(COL_END_DATE));
        experience.setIsCurrent((Boolean) rs.getObject(COL_IS_CURRENT));
        experience.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        experience.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return experience;
    };

    @Inject
    public UserExperienceDao(Jdbi jdbi) {
        super(jdbi,
                TableDefinition.<UserExperience>builder()
                        .tableName(TABLE_NAME)
                        .idColumn(ID_COLUMN)
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_USER_ID)
                                .javaType(Long.class)
                                .insertable(true)
                                .updatable(false)
                                .getter(UserExperience::getUserId)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_TITLE)
                                .javaType(String.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getTitle)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_DESCRIPTION)
                                .javaType(String.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getDescription)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_EXPERIENCE_TYPE)
                                .javaType(String.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getExperienceType)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_START_DATE)
                                .javaType(java.sql.Date.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getStartDate)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_END_DATE)
                                .javaType(java.sql.Date.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getEndDate)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_IS_CURRENT)
                                .javaType(Boolean.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(UserExperience::getIsCurrent)
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_CREATED_AT)
                                .javaType(Timestamp.class)
                                .insertable(true)
                                .updatable(false)
                                .getter(e -> Timestamp.from(Instant.now()))
                                .build())
                        .addColumn(ColumnDefinition.<UserExperience>builder()
                                .columnName(COL_UPDATED_AT)
                                .javaType(Timestamp.class)
                                .insertable(true)
                                .updatable(true)
                                .getter(e -> Timestamp.from(Instant.now()))
                                .build())
                        .build(),
                USER_EXPERIENCE_MAPPER
        );
    }

    /**
     * Find all experiences for a specific user.
     *
     * @param userId the user ID
     * @return list of user experiences
     */
    public List<UserExperience> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_experiences WHERE user_id = :userId ORDER BY start_date DESC NULLS LAST, created_at DESC";
        return executeQuery(sql, "userId", userId);
    }

    /**
     * Find experiences by type for a specific user.
     *
     * @param userId the user ID
     * @param experienceType the experience type
     * @return list of user experiences
     */
    public List<UserExperience> findByUserIdAndType(Long userId, String experienceType) {
        String sql = "SELECT * FROM user_experiences WHERE user_id = :userId AND experience_type = :experienceType ORDER BY start_date DESC NULLS LAST, created_at DESC";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .bind("experienceType", experienceType)
                        .map(USER_EXPERIENCE_MAPPER)
                        .list()
        );
    }

    /**
     * Find current experiences for a specific user.
     *
     * @param userId the user ID
     * @return list of current user experiences
     */
    public List<UserExperience> findCurrentByUserId(Long userId) {
        String sql = "SELECT * FROM user_experiences WHERE user_id = :userId AND is_current = true ORDER BY start_date DESC NULLS LAST";
        return jdbi.withHandle(handle ->
                handle.createQuery(sql)
                        .bind("userId", userId)
                        .map(USER_EXPERIENCE_MAPPER)
                        .list()
        );
    }
}

