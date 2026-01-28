package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserFieldOfInterest;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_fields_of_interest table.
 */
public class UserFieldOfInterestDao extends StandardDao<UserFieldOfInterest, Long> {

    private static final String TABLE_NAME = "user_fields_of_interest";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_FIELD_NAME = "field_name";
    private static final String COL_IS_HARD_REQUIREMENT = "is_hard_requirement";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<UserFieldOfInterest> MAPPER = (rs, ctx) -> {
        UserFieldOfInterest interest = new UserFieldOfInterest();
        interest.setId(rs.getLong(ID_COLUMN));
        interest.setUserId(rs.getLong(COL_USER_ID));
        interest.setFieldName(rs.getString(COL_FIELD_NAME));
        interest.setIsHardRequirement(rs.getBoolean(COL_IS_HARD_REQUIREMENT));
        interest.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return interest;
    };

    @Inject
    public UserFieldOfInterestDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserFieldOfInterest>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserFieldOfInterest>builder()
                    .columnName(COL_USER_ID)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserFieldOfInterest::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserFieldOfInterest>builder()
                    .columnName(COL_FIELD_NAME)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserFieldOfInterest::getFieldName)
                    .build())
                .addColumn(ColumnDefinition.<UserFieldOfInterest>builder()
                    .columnName(COL_IS_HARD_REQUIREMENT)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserFieldOfInterest::getIsHardRequirement)
                    .build())
                .addColumn(ColumnDefinition.<UserFieldOfInterest>builder()
                    .columnName(COL_CREATED_AT)
                    .insertable(true)
                    .updatable(false)
                    .getter(i -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<UserFieldOfInterest> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_fields_of_interest WHERE user_id = :userId ORDER BY field_name";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<UserFieldOfInterest> findHardRequirementsByUserId(Long userId) {
        String sql = "SELECT * FROM user_fields_of_interest WHERE user_id = :userId AND is_hard_requirement = true";
        return executeQuery(sql, "userId", userId);
    }
    
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_fields_of_interest WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

