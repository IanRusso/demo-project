package com.irusso.demoserver.db.dao;

import com.irusso.demoserver.db.model.Employee;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the employees table.
 * Provides CRUD operations and custom queries for Employee entities.
 */
public class EmployeeDao extends StandardDao<Employee, Long> {

    private static final String TABLE_NAME = "employees";
    private static final String ID_COLUMN = "id";

    /**
     * Create the table definition for employees.
     */
    private static TableDefinition<Employee> createTableDefinition() {
        return TableDefinition.<Employee>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("name")
                .javaType(String.class)
                .nullable(false)
                .getter(Employee::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("email")
                .javaType(String.class)
                .nullable(false)
                .getter(Employee::getEmail)
                .setter((e, v) -> e.setEmail((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("phone_number")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getPhoneNumber)
                .setter((e, v) -> e.setPhoneNumber((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("location")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getLocation)
                .setter((e, v) -> e.setLocation((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("education_level")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getEducationLevel)
                .setter((e, v) -> e.setEducationLevel((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("summary")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getSummary)
                .setter((e, v) -> e.setSummary((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("profile_picture_url")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getProfilePictureUrl)
                .setter((e, v) -> e.setProfilePictureUrl((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("employment_status")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getEmploymentStatus)
                .setter((e, v) -> e.setEmploymentStatus((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("background_check_status")
                .javaType(String.class)
                .nullable(true)
                .getter(Employee::getBackgroundCheckStatus)
                .setter((e, v) -> e.setBackgroundCheckStatus((String) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("employee_rating")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(Employee::getEmployeeRating)
                .setter((e, v) -> e.setEmployeeRating((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("communication_rating")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(Employee::getCommunicationRating)
                .setter((e, v) -> e.setCommunicationRating((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("salary_expectations_min")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(Employee::getSalaryExpectationsMin)
                .setter((e, v) -> e.setSalaryExpectationsMin((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("salary_expectations_max")
                .javaType(BigDecimal.class)
                .nullable(true)
                .getter(Employee::getSalaryExpectationsMax)
                .setter((e, v) -> e.setSalaryExpectationsMax((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("actively_seeking")
                .javaType(Boolean.class)
                .nullable(true)
                .getter(Employee::getActivelySeeking)
                .setter((e, v) -> e.setActivelySeeking((Boolean) v))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
                .columnName("created_at")
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)
                .getter(e -> e.getCreatedAt() != null ? Timestamp.from(e.getCreatedAt()) : Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt(v != null ? ((Timestamp) v).toInstant() : null))
                .build())
            .addColumn(ColumnDefinition.<Employee>builder()
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
     * RowMapper to convert database rows to Employee objects.
     */
    private static final RowMapper<Employee> EMPLOYEE_MAPPER = (rs, ctx) -> {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));
        employee.setName(rs.getString("name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setLocation(rs.getString("location"));
        employee.setEducationLevel(rs.getString("education_level"));
        employee.setSummary(rs.getString("summary"));
        employee.setProfilePictureUrl(rs.getString("profile_picture_url"));
        employee.setEmploymentStatus(rs.getString("employment_status"));
        employee.setBackgroundCheckStatus(rs.getString("background_check_status"));
        employee.setEmployeeRating(rs.getBigDecimal("employee_rating"));
        employee.setCommunicationRating(rs.getBigDecimal("communication_rating"));
        employee.setSalaryExpectationsMin(rs.getBigDecimal("salary_expectations_min"));
        employee.setSalaryExpectationsMax(rs.getBigDecimal("salary_expectations_max"));
        employee.setActivelySeeking(rs.getBoolean("actively_seeking"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            employee.setCreatedAt(createdAt.toInstant());
        }

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            employee.setUpdatedAt(updatedAt.toInstant());
        }

        return employee;
    };

    /**
     * Constructor for EmployeeDao.
     *
     * @param jdbi The JDBI instance for database access
     */
    public EmployeeDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), EMPLOYEE_MAPPER);
    }



    /**
     * Find employees by email address.
     * 
     * @param email The email address to search for
     * @return List of employees with matching email (should be 0 or 1 due to unique constraint)
     */
    public List<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = :email";
        return executeQuery(sql, "email", email);
    }

    /**
     * Find employees who are actively seeking employment.
     * 
     * @return List of employees actively seeking jobs
     */
    public List<Employee> findActivelySeeking() {
        String sql = "SELECT * FROM employees WHERE actively_seeking = true ORDER BY updated_at DESC";
        return executeQuery(sql);
    }

    /**
     * Find employees by location.
     * 
     * @param location The location to search for
     * @return List of employees in the specified location
     */
    public List<Employee> findByLocation(String location) {
        String sql = "SELECT * FROM employees WHERE location ILIKE :location ORDER BY name";
        return executeQuery(sql, "location", "%" + location + "%");
    }

    /**
     * Find employees with a minimum rating.
     * 
     * @param minRating The minimum employee rating
     * @return List of employees with rating >= minRating
     */
    public List<Employee> findByMinimumRating(double minRating) {
        String sql = "SELECT * FROM employees WHERE employee_rating >= :minRating ORDER BY employee_rating DESC";
        return executeQuery(sql, "minRating", minRating);
    }

    /**
     * Update employee rating.
     * 
     * @param id The employee ID
     * @param rating The new rating (0-5)
     * @return true if update was successful
     */
    public boolean updateRating(Long id, double rating) {
        String sql = """
            UPDATE employees 
            SET employee_rating = :rating, updated_at = :updatedAt 
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
     * @param id The employee ID
     * @param rating The new communication rating (0-5)
     * @return true if update was successful
     */
    public boolean updateCommunicationRating(Long id, double rating) {
        String sql = """
            UPDATE employees 
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

