package com.irusso.demoserver.db.dao;

import com.google.inject.Inject;
import com.irusso.demoserver.db.model.City;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.PreparedBatch;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Data Access Object for the cities table.
 * Provides CRUD operations and custom queries for City entities with PostGIS support.
 */
public class CityDao extends StandardDao<City, Long> {

    private static final String TABLE_NAME = "cities";
    private static final String ID_COLUMN = "id";

    // Column name constants
    private static final String COL_GEONAME_ID = "geoname_id";
    private static final String COL_NAME = "name";
    private static final String COL_ASCII_NAME = "ascii_name";
    private static final String COL_LATITUDE = "latitude";
    private static final String COL_LONGITUDE = "longitude";
    private static final String COL_FEATURE_CLASS = "feature_class";
    private static final String COL_FEATURE_CODE = "feature_code";
    private static final String COL_COUNTRY_CODE = "country_code";
    private static final String COL_CC2 = "cc2";
    private static final String COL_ADMIN1_CODE = "admin1_code";
    private static final String COL_ADMIN2_CODE = "admin2_code";
    private static final String COL_ADMIN3_CODE = "admin3_code";
    private static final String COL_ADMIN4_CODE = "admin4_code";
    private static final String COL_POPULATION = "population";
    private static final String COL_ELEVATION = "elevation";
    private static final String COL_DEM = "dem";
    private static final String COL_TIMEZONE = "timezone";
    private static final String COL_MODIFICATION_DATE = "modification_date";
    private static final String COL_CREATED_AT = "created_at";
    private static final String COL_UPDATED_AT = "updated_at";

    /**
     * Create the table definition for cities.
     */
    private static TableDefinition<City> createTableDefinition() {
        return TableDefinition.<City>builder()
            .tableName(TABLE_NAME)
            .idColumn(ID_COLUMN)
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_GEONAME_ID)
                .javaType(Long.class)
                .nullable(true)
                .getter(City::getGeonameId)
                .setter((e, v) -> e.setGeonameId((Long) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_NAME)
                .javaType(String.class)
                .nullable(false)
                .getter(City::getName)
                .setter((e, v) -> e.setName((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ASCII_NAME)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getAsciiName)
                .setter((e, v) -> e.setAsciiName((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_LATITUDE)
                .javaType(BigDecimal.class)
                .nullable(false)
                .getter(City::getLatitude)
                .setter((e, v) -> e.setLatitude((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_LONGITUDE)
                .javaType(BigDecimal.class)
                .nullable(false)
                .getter(City::getLongitude)
                .setter((e, v) -> e.setLongitude((BigDecimal) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_FEATURE_CLASS)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getFeatureClass)
                .setter((e, v) -> e.setFeatureClass((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_FEATURE_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getFeatureCode)
                .setter((e, v) -> e.setFeatureCode((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_COUNTRY_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getCountryCode)
                .setter((e, v) -> e.setCountryCode((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_CC2)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getCc2)
                .setter((e, v) -> e.setCc2((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ADMIN1_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getAdmin1Code)
                .setter((e, v) -> e.setAdmin1Code((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ADMIN2_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getAdmin2Code)
                .setter((e, v) -> e.setAdmin2Code((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ADMIN3_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getAdmin3Code)
                .setter((e, v) -> e.setAdmin3Code((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ADMIN4_CODE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getAdmin4Code)
                .setter((e, v) -> e.setAdmin4Code((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_POPULATION)
                .javaType(Long.class)
                .nullable(true)
                .getter(City::getPopulation)
                .setter((e, v) -> e.setPopulation((Long) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_ELEVATION)
                .javaType(Integer.class)
                .nullable(true)
                .getter(City::getElevation)
                .setter((e, v) -> e.setElevation((Integer) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_DEM)
                .javaType(Integer.class)
                .nullable(true)
                .getter(City::getDem)
                .setter((e, v) -> e.setDem((Integer) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_TIMEZONE)
                .javaType(String.class)
                .nullable(true)
                .getter(City::getTimezone)
                .setter((e, v) -> e.setTimezone((String) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_MODIFICATION_DATE)
                .javaType(Date.class)
                .nullable(true)
                .getter(City::getModificationDate)
                .setter((e, v) -> e.setModificationDate((Date) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_CREATED_AT)
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(false)
                .getter(e -> e.getCreatedAt() != null ? e.getCreatedAt() : Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setCreatedAt((Timestamp) v))
                .build())
            .addColumn(ColumnDefinition.<City>builder()
                .columnName(COL_UPDATED_AT)
                .javaType(Timestamp.class)
                .nullable(false)
                .insertable(true)
                .updatable(true)
                .getter(e -> Timestamp.from(Instant.now()))
                .setter((e, v) -> e.setUpdatedAt((Timestamp) v))
                .build())
            .build();
    }

    /**
     * RowMapper to convert database rows to City objects.
     */
    private static final RowMapper<City> CITY_MAPPER = (rs, ctx) -> {
        City city = new City();
        city.setId(rs.getLong(ID_COLUMN));
        city.setGeonameId(rs.getLong(COL_GEONAME_ID));
        city.setName(rs.getString(COL_NAME));
        city.setAsciiName(rs.getString(COL_ASCII_NAME));
        city.setLatitude(rs.getBigDecimal(COL_LATITUDE));
        city.setLongitude(rs.getBigDecimal(COL_LONGITUDE));
        city.setFeatureClass(rs.getString(COL_FEATURE_CLASS));
        city.setFeatureCode(rs.getString(COL_FEATURE_CODE));
        city.setCountryCode(rs.getString(COL_COUNTRY_CODE));
        city.setCc2(rs.getString(COL_CC2));
        city.setAdmin1Code(rs.getString(COL_ADMIN1_CODE));
        city.setAdmin2Code(rs.getString(COL_ADMIN2_CODE));
        city.setAdmin3Code(rs.getString(COL_ADMIN3_CODE));
        city.setAdmin4Code(rs.getString(COL_ADMIN4_CODE));
        city.setPopulation(rs.getLong(COL_POPULATION));
        city.setElevation((Integer) rs.getObject(COL_ELEVATION));
        city.setDem((Integer) rs.getObject(COL_DEM));
        city.setTimezone(rs.getString(COL_TIMEZONE));
        city.setModificationDate(rs.getDate(COL_MODIFICATION_DATE));
        city.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
        city.setUpdatedAt(rs.getTimestamp(COL_UPDATED_AT));
        return city;
    };

    /**
     * Constructor for CityDao.
     *
     * @param jdbi The JDBI instance for database access
     */
    @Inject
    public CityDao(Jdbi jdbi) {
        super(jdbi, createTableDefinition(), CITY_MAPPER);
    }

    /**
     * Batch insert cities with PostGIS location data.
     * This method is optimized for bulk imports.
     *
     * @param cities List of cities to insert
     * @return Number of cities inserted
     */
    public int batchInsert(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            return 0;
        }

        String sql = """
            INSERT INTO cities (
                geoname_id, name, ascii_name, latitude, longitude,
                feature_class, feature_code, country_code, cc2, admin1_code, admin2_code,
                admin3_code, admin4_code, population, elevation, dem, timezone,
                modification_date, location, created_at, updated_at
            ) VALUES (
                :geonameId, :name, :asciiName, :latitude, :longitude,
                :featureClass, :featureCode, :countryCode, :cc2, :admin1Code, :admin2Code,
                :admin3Code, :admin4Code, :population, :elevation, :dem, :timezone,
                :modificationDate, ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)::geography,
                :createdAt, :updatedAt
            )
            ON CONFLICT (geoname_id) DO UPDATE SET
                name = EXCLUDED.name,
                ascii_name = EXCLUDED.ascii_name,
                latitude = EXCLUDED.latitude,
                longitude = EXCLUDED.longitude,
                population = EXCLUDED.population,
                updated_at = EXCLUDED.updated_at
            """;

        return jdbi.withHandle(handle -> {
            PreparedBatch batch = handle.prepareBatch(sql);
            Timestamp now = Timestamp.from(Instant.now());

            for (City city : cities) {
                batch.bind("geonameId", city.getGeonameId())
                    .bind("name", city.getName())
                    .bind("asciiName", city.getAsciiName())
                    .bind("latitude", city.getLatitude())
                    .bind("longitude", city.getLongitude())
                    .bind("featureClass", city.getFeatureClass())
                    .bind("featureCode", city.getFeatureCode())
                    .bind("countryCode", city.getCountryCode())
                    .bind("cc2", city.getCc2())
                    .bind("admin1Code", city.getAdmin1Code())
                    .bind("admin2Code", city.getAdmin2Code())
                    .bind("admin3Code", city.getAdmin3Code())
                    .bind("admin4Code", city.getAdmin4Code())
                    .bind("population", city.getPopulation())
                    .bind("elevation", city.getElevation())
                    .bind("dem", city.getDem())
                    .bind("timezone", city.getTimezone())
                    .bind("modificationDate", city.getModificationDate())
                    .bind("createdAt", now)
                    .bind("updatedAt", now)
                    .add();
            }

            int[] results = batch.execute();
            return results.length;
        });
    }

    /**
     * Find cities by country code.
     */
    public List<City> findByCountryCode(String countryCode) {
        String sql = "SELECT * FROM cities WHERE country_code = :countryCode ORDER BY population DESC";
        return executeQuery(sql, "countryCode", countryCode);
    }

    /**
     * Find cities by name (case-insensitive).
     */
    public List<City> findByName(String name) {
        String sql = "SELECT * FROM cities WHERE name ILIKE :name ORDER BY population DESC";
        return executeQuery(sql, "name", "%" + name + "%");
    }

    /**
     * Delete all cities (useful for re-importing data).
     */
    public int deleteAll() {
        String sql = "DELETE FROM cities";
        return executeUpdate(sql);
    }
}

