package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.Employer;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the employers table.
 */
public class EmployerDao extends StandardDao<Employer, Long> {

    private static final String TABLE_NAME = "employers";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_NAME = "name";
    private static final String COL_EMAIL = "email";
    private static final String COL_PHONE_NUMBER = "phone_number";
    private static final String COL_LOCATION = "location";
    private static final String COL_COMPANY_SIZE = "company_size";
    private static final String COL_COMPANY_TYPE = "company_type";
    private static final String COL_DESCRIPTION = "description";
    private static final String COL_COMPANY_PICTURE_URL = "company_picture_url";
    private static final String COL_EMPLOYER_RATING = "employer_rating";
    private static final String COL_COMMUNICATION_RATING = "communication_rating";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    private static final RowMapper<Employer> MAPPER = (rs, ctx) -> {
        Employer employer = new Employer();
        employer.setId(rs.getLong(ID_COLUMN));
        employer.setName(rs.getString(COL_NAME));
        employer.setEmail(rs.getString(COL_EMAIL));
        employer.setPhoneNumber(rs.getString(COL_PHONE_NUMBER));
        employer.setLocation(rs.getString(COL_LOCATION));
        employer.setCompanySize(rs.getString(COL_COMPANY_SIZE));
        employer.setCompanyType(rs.getString(COL_COMPANY_TYPE));
        employer.setDescription(rs.getString(COL_DESCRIPTION));
        employer.setCompanyPictureUrl(rs.getString(COL_COMPANY_PICTURE_URL));
        employer.setEmployerRating(rs.getBigDecimal(COL_EMPLOYER_RATING));
        employer.setCommunicationRating(rs.getBigDecimal(COL_COMMUNICATION_RATING));
        employer.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        employer.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return employer;
    };
    
    @Inject
    public EmployerDao(Jdbi jdbi) {
        super(jdbi,
            TableDefinition.<Employer>builder()
                .tableName(TABLE_NAME)
                .idColumn(ID_COLUMN)
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_NAME)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getName)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_EMAIL)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getEmail)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_PHONE_NUMBER)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getPhoneNumber)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_LOCATION)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getLocation)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_COMPANY_SIZE)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getCompanySize)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_COMPANY_TYPE)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getCompanyType)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_DESCRIPTION)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getDescription)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_COMPANY_PICTURE_URL)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getCompanyPictureUrl)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_EMPLOYER_RATING)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getEmployerRating)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_COMMUNICATION_RATING)
                    .insertable(true)
                    .updatable(true)
                    .getter(Employer::getCommunicationRating)
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_CREATED_AT)
                    .insertable(true)
                    .updatable(false)
                    .getter(e -> Timestamp.from(Instant.now()))
                    .build())
                .addColumn(ColumnDefinition.<Employer>builder()
                    .columnName(COL_UPDATED_AT)
                    .insertable(true)
                    .updatable(true)
                    .getter(e -> Timestamp.from(Instant.now()))
                    .build())
                .build(),
            MAPPER
        );
    }
    
    public List<Employer> findByEmail(String email) {
        String sql = "SELECT * FROM employers WHERE email = :email";
        return executeQuery(sql, "email", email);
    }
    
    public List<Employer> findByLocation(String location) {
        String sql = "SELECT * FROM employers WHERE location ILIKE :location ORDER BY name";
        return executeQuery(sql, "location", "%" + location + "%");
    }
    
    public List<Employer> findByMinimumRating(double minRating) {
        String sql = "SELECT * FROM employers WHERE employer_rating >= :minRating ORDER BY employer_rating DESC";
        return executeQuery(sql, "minRating", minRating);
    }
    
    public boolean updateRating(Long id, double rating) {
        String sql = """
            UPDATE employers 
            SET employer_rating = :rating, updated_at = :updatedAt 
            WHERE id = :id
            """;
        
        int rowsAffected = executeUpdate(sql, 
            "id", id, 
            "rating", rating, 
            "updatedAt", Timestamp.from(Instant.now())
        );
        return rowsAffected > 0;
    }
    
    public boolean updateCommunicationRating(Long id, double rating) {
        String sql = """
            UPDATE employers 
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

