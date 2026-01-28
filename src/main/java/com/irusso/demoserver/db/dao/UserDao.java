package com.irusso.demoserver.db.dao;

import com.irusso.demoserver.db.model.User;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the users table.
 * Provides CRUD operations and custom queries for User entities.
 */
public class UserDao extends StandardDao<User, Long> {

    private static final String TABLE_NAME = "users";
    private static final String ID_COLUMN = "id";

    /**
     * Create the table definition for users.
     */
    private static TableDefinition<User> createTableDefinition() {
        return TableDefinition.<User>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("name")
                .javaType(String.class)
                .nullable(false)
                .getter(User::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("email")
                .javaType(String.class)
                .nullable(false)
                .getter(User::getEmail)
                .setter((e, v) -> e.setEmail((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("phone_number")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getPhoneNumber)
                .setter((e, v) -> e.setPhoneNumber((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("location")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getLocation)
                .setter((e, v) -> e.setLocation((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("education_level")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getEducationLevel)
                .setter((e, v) -> e.setEducationLevel((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("summary")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getSummary)
                .setter((e, v) -> e.setSummary((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("profile_picture_url")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getProfilePictureUrl)
                .setter((e, v) -> e.setProfilePictureUrl((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("employment_status")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getEmploymentStatus)
                .setter((e, v) -> e.setEmploymentStatus((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("background_check_status")
                .javaType(String.class)
                .nullable(true)
                .getter(User::getBackgroundCheckStatus)
                .setter((e, v) -> e.setBackgroundCheckStatus((String) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("user_rating")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(User::getUserRating)
                .setter((e, v) -> e.setUserRating((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("communication_rating")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(User::getCommunicationRating)
                .setter((e, v) -> e.setCommunicationRating((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("salary_expectations_min")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(User::getSalaryExpectationsMin)
                .setter((e, v) -> e.setSalaryExpectationsMin((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("salary_expectations_max")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(User::getSalaryExpectationsMax)
                .setter((e, v) -> e.setSalaryExpectationsMax((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("actively_seeking")
                .javaType(Boolean.class)
                .nullable(true)
                .getter(User::getActivelySeeking)
                .setter((e, v) -> e.setActivelySeeking((Boolean) v))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("created_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)
                .getter(e -> e.getCreatedAt() != null ? Timestamp.from(e.getCreatedAt()) : Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt(v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .addColumn(ColumnDefinition.<User>builder()
                .columnName("updated_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(true)
                .getter(e -> Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setUpdatedAt(v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .build();
    }

    /**
     * RowMapper to convert database rows to User objects.
     */
    private static final RowMapper<User> USER_MAPPER = (rs, ctx) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setLocation(rs.getString("location"));
        user.setEducationLevel(rs.getString("education_level"));
        user.setSummary(rs.getString("summary"));
        user.setProfilePictureUrl(rs.getString("profile_picture_url"));
        user.setEmploymentStatus(rs.getString("employment_status"));
        user.setBackgroundCheckStatus(rs.getString("background_check_status"));
        user.setUserRating(rs.getBigDecimal("user_rating"));
        user.setCommunicationRating(rs.getBigDecimal("communication_rating"));
        user.setSalaryExpectationsMin(rs.getBigDecimal("salary_expectations_min"));
        user.setSalaryExpectationsMax(rs.getBigDecimal("salary_expectations_max"));
        user.setActivelySeeking(rs.getBoolean("actively_seeking"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            user.setCreatedAt(createdAt.toInstant());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            user.setUpdatedAt(updatedAt.toInstant());
        }

        return user;
    };

    /**
     * Constructor for UserDao.
     *
     * @param jdbi The JDBI instance for database access
     */
    public UserDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), USER_MAPPER);
    }



    /**
     * Find users by email address.
     *
     * @param email The email address to search for
     * @return List of users with matching email (should be 0 or 1 due to unique constraint)
     */
    public List<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :email";
        return executeQuery(sql, "email", email);
    }

    /**
     * Find users who are actively seeking employment.
     *
     * @return List of users actively seeking jobs
     */
    public List<User> findActivelySeeking() {
        String sql = "SELECT * FROM users WHERE actively_seeking = true ORDER BY updated_at DESC";
        return executeQuery(sql);
    }

    /**
     * Find users by location.
     *
     * @param location The location to search for
     * @return List of users in the specified location
     */
    public List<User> findByLocation(String location) {
        String sql = "SELECT * FROM users WHERE location ILIKE :location ORDER BY name";
        return executeQuery(sql, "location", "%" + location + "%");
    }

    /**
     * Find users with a minimum rating.
     *
     * @param minRating The minimum user rating
     * @return List of users with rating >= minRating
     */
    public List<User> findByMinimumRating(double minRating) {
        String sql = "SELECT * FROM users WHERE user_rating >= :minRating ORDER BY user_rating DESC";
        return executeQuery(sql, "minRating", minRating);
    }

    /**
     * Update user rating.
     *
     * @param id The user ID
     * @param rating The new rating (0-5)
     * @return true if update was successful
     */
    public boolean updateRating(Long id, double rating) {
        String sql = """
            UPDATE users
            SET user_rating = :rating, updated_at = :updatedAt
            WHERE id = :id
            """;

        int rowsAffected = executeUpdate(sql,
            "id", id,
            "rating", rating,
            "updatedAt", Timestamp.from(Instant.now())
        );
        return rowsAffected > 0;
    }

    /**
     * Update communication rating.
     *
     * @param id The user ID
     * @param rating The new communication rating (0-5)
     * @return true if update was successful
     */
    public boolean updateCommunicationRating(Long id, double rating) {
        String sql = """
            UPDATE users
            SET communication_rating = :rating, updated_at = :updatedAt
            WHERE id = :id
            """;

        int rowsAffected = executeUpdate(sql,
            "id", id,
            "rating", rating,
            "updatedAt", Timestamp.from(Instant.now())
        );
        return rowsAffected > 0;
    }
}

