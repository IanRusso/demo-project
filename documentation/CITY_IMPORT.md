# City Import Feature

This document describes the city import functionality that allows loading geographic city data from tar.gz files into the PostGIS-enabled database.

## Overview

The city import feature provides a REST API endpoint that accepts a URL to a tar.gz file containing city data (in GeoNames format), downloads it asynchronously, decompresses it, parses the data, and loads it into the PostgreSQL database with PostGIS geospatial support.

## Database Schema

The `cities` table includes the following columns:

- **id**: Auto-incrementing primary key
- **geoname_id**: Unique GeoNames identifier
- **name**: City name
- **ascii_name**: ASCII version of the name
- **alternate_names**: Comma-separated list of alternate names
- **latitude**: Latitude coordinate (DECIMAL)
- **longitude**: Longitude coordinate (DECIMAL)
- **feature_class**: GeoNames feature class
- **feature_code**: GeoNames feature code
- **country_code**: ISO country code
- **cc2**: Alternate country codes
- **admin1_code**: Administrative division code (state/province)
- **admin2_code**: Administrative division code (county)
- **admin3_code**: Administrative division code
- **admin4_code**: Administrative division code
- **population**: City population
- **elevation**: Elevation in meters
- **dem**: Digital elevation model
- **timezone**: Timezone identifier
- **modification_date**: Last modification date
- **location**: PostGIS GEOGRAPHY point (automatically computed from lat/lon)
- **created_at**: Record creation timestamp
- **updated_at**: Record update timestamp

### Indexes

- **idx_cities_name**: Index on city name for fast name searches
- **idx_cities_country_code**: Index on country code
- **idx_cities_population**: Index on population (descending)
- **idx_cities_geoname_id**: Index on GeoNames ID
- **idx_cities_location**: PostGIS spatial index (GIST) for location-based queries

## API Endpoints

### Import Cities from URL

**Endpoint:** `POST /api/cities/import`

**Request Body:**
```json
{
  "url": "https://example.com/cities.tar.gz"
}
```

**Response (202 Accepted):**
```json
{
  "success": true,
  "message": "City import started successfully",
  "data": {
    "status": "Import started",
    "url": "https://example.com/cities.tar.gz",
    "message": "City import is processing in the background. Check server logs for progress."
  }
}
```

**Notes:**
- The import runs asynchronously in the background
- The endpoint returns immediately with a 202 Accepted status
- Progress and completion status are logged to the server logs
- Supports HTTP, HTTPS, and file:// URLs

### Get All Cities

**Endpoint:** `GET /api/cities`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "geonameId": 5128581,
      "name": "New York City",
      "asciiName": "New York City",
      "latitude": 40.7127837,
      "longitude": -74.0059413,
      "countryCode": "US",
      "population": 8175133,
      "timezone": "America/New_York"
    }
  ]
}
```

### Get City by ID

**Endpoint:** `GET /api/cities/{id}`

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "geonameId": 5128581,
    "name": "New York City",
    "asciiName": "New York City",
    "latitude": 40.7127837,
    "longitude": -74.0059413,
    "countryCode": "US",
    "population": 8175133,
    "timezone": "America/New_York"
  }
}
```

### Search Cities by Name

**Endpoint:** `GET /api/cities/search?name={searchTerm}`

**Example:** `GET /api/cities/search?name=New York`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "New York City",
      "population": 8175133
    }
  ]
}
```

### Get Cities by Country

**Endpoint:** `GET /api/cities/country/{countryCode}`

**Example:** `GET /api/cities/country/US`

**Response:**
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "New York City",
      "countryCode": "US",
      "population": 8175133
    }
  ]
}
```

## Data Format

The import feature expects data in **GeoNames tab-delimited format**:

```
geonameid   name   asciiname   alternatenames   latitude   longitude   feature_class   feature_code   country_code   cc2   admin1_code   admin2_code   admin3_code   admin4_code   population   elevation   dem   timezone   modification_date
```

### Example Line:
```
5128581	New York City	New York City	NYC,Nueva York	40.7127837	-74.0059413	P	PPL	US		NY	061			8175133		10	America/New_York	2023-01-15
```

## Usage Examples

### Using curl

```bash
# Import cities from a URL
curl -X POST http://localhost:8080/api/cities/import \
  -H "Content-Type: application/json" \
  -d '{"url": "https://download.geonames.org/export/dump/cities15000.zip"}'

# Import from local file
curl -X POST http://localhost:8080/api/cities/import \
  -H "Content-Type: application/json" \
  -d '{"url": "file:///path/to/cities.tar.gz"}'

# Search for cities
curl http://localhost:8080/api/cities/search?name=London

# Get cities in a country
curl http://localhost:8080/api/cities/country/US
```

### Using the existing data file

The project includes a sample data file at `data/cities15000.tar`. To import it:

```bash
# First, compress it to tar.gz if needed
cd data
gzip -k cities15000.tar

# Then import via API
curl -X POST http://localhost:8080/api/cities/import \
  -H "Content-Type: application/json" \
  -d '{"url": "file:///Users/ian.russo/git/demo-project/data/cities15000.tar.gz"}'
```

## Implementation Details

### Async Processing

- Uses Java's `CompletableFuture` for asynchronous execution
- Dedicated thread pool (`ExecutorService`) with 2 threads
- Non-blocking - API returns immediately while import runs in background

### Batch Insertion

- Cities are inserted in batches of 1000 for optimal performance
- Uses JDBI3 `PreparedBatch` for efficient bulk inserts
- Includes `ON CONFLICT` handling to update existing cities

### PostGIS Integration

- Location data is stored as PostGIS GEOGRAPHY type
- Automatically computed from latitude/longitude using `ST_MakePoint`
- Spatial index (GIST) enables fast proximity queries
- Uses SRID 4326 (WGS84 coordinate system)

### Error Handling

- Download failures are logged with HTTP status codes
- Parsing errors skip invalid lines and continue processing
- All errors are logged with detailed messages
- Import result includes counts of imported and skipped cities

### Logging

The import process logs:
- Import start with URL
- File processing progress
- Batch insertion progress with running totals
- Final results (cities imported, skipped, duration)
- Any errors encountered

Example log output:
```
INFO  CityService - Starting city import from URL: file:///data/cities15000.tar.gz
INFO  CityService - Processing file: cities15000.txt
INFO  CityService - Imported batch of 1000 cities. Total: 1000
INFO  CityService - Imported batch of 1000 cities. Total: 2000
INFO  CityService - City import completed successfully. Imported 23018 cities in 5432 ms
```

## PostGIS Spatial Queries

Once cities are imported, you can perform spatial queries:

### Find cities within a radius
```sql
SELECT name, country_code, population
FROM cities
WHERE ST_DWithin(
  location,
  ST_MakePoint(-74.0059413, 40.7127837)::geography,
  50000  -- 50km radius
)
ORDER BY population DESC;
```

### Find nearest cities
```sql
SELECT name, country_code,
  ST_Distance(location, ST_MakePoint(-74.0059413, 40.7127837)::geography) as distance_meters
FROM cities
ORDER BY location <-> ST_MakePoint(-74.0059413, 40.7127837)::geography
LIMIT 10;
```

## Performance Considerations

- **Batch Size**: Default 1000 cities per batch (configurable in `CityService.BATCH_SIZE`)
- **Thread Pool**: 2 threads for async processing (configurable in `CityService.EXECUTOR`)
- **Spatial Index**: GIST index on location column for fast spatial queries
- **Upsert Logic**: Uses `ON CONFLICT` to update existing cities instead of failing

## Troubleshooting

### PostGIS Not Available

If PostGIS is not installed, the migration will fail. Install PostGIS:

**macOS:**
```bash
brew install postgis
```

**Ubuntu:**
```bash
sudo apt install postgresql-15-postgis-3
```

Then re-run the database setup:
```bash
./scripts/setup-db.sh
```

### Import Not Starting

Check server logs for errors. Common issues:
- Invalid URL format
- Network connectivity issues
- File not found (for file:// URLs)
- Insufficient permissions

### Slow Import

For large datasets:
- Increase batch size in `CityService.BATCH_SIZE`
- Increase thread pool size in `CityService.EXECUTOR`
- Ensure database has sufficient resources
- Consider disabling indexes during import and rebuilding after

## Future Enhancements

Potential improvements:
- Job status tracking with unique job IDs
- Progress reporting endpoint
- Cancellation support
- Multiple file format support (CSV, JSON)
- Incremental updates
- Data validation and cleansing
- Duplicate detection

