package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.UserGeographicalInterest;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the user_geographical_interests table.
 */
public class UserGeographicalInterestDao extends StandardDao<UserGeographicalInterest, Long> {

    private static final String TABLE_NAME = "user_geographical_interests";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_USER_ID = "user_id";
    private static final String COL_LOCATION = "location";
    private static final String COL_IS_HARD_REQUIREMENT = "is_hard_requirement";
    private static final String COL_CREATED_AT = "created_at";

    private static final RowMapper<UserGeographicalInterest> MAPPER = (rs, ctx) -> {
        UserGeographicalInterest interest = new UserGeographicalInterest();
        interest.setId(rs.getLong(ID_COLUMN));
        interest.setUserId(rs.getLong(COL_USER_ID));
        interest.setLocation(rs.getString(COL_LOCATION));
        interest.setIsHardRequirement(rs.getBoolean(COL_IS_HARD_REQUIREMENT));
        interest.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        return interest;
    };

    @Inject
    public UserGeographicalInterestDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<UserGeographicalInterest>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<UserGeographicalInterest>builder()
                    .columnName(COL_USER_ID)
                    .insertable(true)
                    .updatable(false)
                    .getter(UserGeographicalInterest::getUserId)
                    .build())
                .addColumn(ColumnDefinition.<UserGeographicalInterest>builder()
                    .columnName(COL_LOCATION)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserGeographicalInterest::getLocation)
                    .build())
                .addColumn(ColumnDefinition.<UserGeographicalInterest>builder()
                    .columnName(COL_IS_HARD_REQUIREMENT)
                    .insertable(true)
                    .updatable(true)
                    .getter(UserGeographicalInterest::getIsHardRequirement)
                    .build())
                .addColumn(ColumnDefinition.<UserGeographicalInterest>builder()
                    .columnName(COL_CREATED_AT)
                    .insertable(true)
                    .updatable(false)
                    .getter(i -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<UserGeographicalInterest> findByUserId(Long userId) {
        String sql = "SELECT * FROM user_geographical_interests WHERE user_id = :userId ORDER BY location";
        return executeQuery(sql, "userId", userId);
    }
    
    public List<UserGeographicalInterest> findHardRequirementsByUserId(Long userId) {
        String sql = "SELECT * FROM user_geographical_interests WHERE user_id = :userId AND is_hard_requirement = true";
        return executeQuery(sql, "userId", userId);
    }
    
    public int deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_geographical_interests WHERE user_id = :userId";
        return executeUpdate(sql, "userId", userId);
    }
}

