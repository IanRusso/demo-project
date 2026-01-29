-- Add cities table with PostGIS support for geospatial data
-- This table stores city information from GeoNames or similar data sources

CREATE TABLE cities (
    id BIGSERIAL PRIMARY KEY,
    geoname_id BIGINT UNIQUE,
    name VARCHAR(200) NOT NULL,
    ascii_name VARCHAR(200),
    alternate_names TEXT,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    feature_class VARCHAR(1),
    feature_code VARCHAR(10),
    country_code VARCHAR(2),
    cc2 VARCHAR(200),
    admin1_code VARCHAR(20),
    admin2_code VARCHAR(80),
    admin3_code VARCHAR(20),
    admin4_code VARCHAR(20),
    population BIGINT,
    elevation INTEGER,
    dem INTEGER,
    timezone VARCHAR(40),
    modification_date DATE,
    location GEOGRAPHY(POINT, 4326),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for common queries
CREATE INDEX idx_cities_name ON cities(name);
CREATE INDEX idx_cities_country_code ON cities(country_code);
CREATE INDEX idx_cities_population ON cities(population DESC);
CREATE INDEX idx_cities_geoname_id ON cities(geoname_id);

-- Create spatial index for location-based queries
CREATE INDEX idx_cities_location ON cities USING GIST(location);

-- Add comments for documentation
COMMENT ON TABLE cities IS 'Cities data from GeoNames or similar sources with PostGIS geospatial support';
COMMENT ON COLUMN cities.geoname_id IS 'GeoNames unique identifier';
COMMENT ON COLUMN cities.location IS 'PostGIS geography point (latitude, longitude) in WGS84';
COMMENT ON COLUMN cities.feature_class IS 'GeoNames feature class (e.g., P for populated place)';
COMMENT ON COLUMN cities.feature_code IS 'GeoNames feature code (e.g., PPL for populated place)';

