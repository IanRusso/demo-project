package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.CityDao;
import com.irusso.demoserver.db.model.City;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service layer for City operations.
 * Handles business logic including async import from tar.gz files.
 */
public class CityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityService.class);
    private static final int BATCH_SIZE = 1000;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);

    private final CityDao cityDao;

    @Inject
    public CityService(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    /**
     * Get all cities.
     */
    public List<City> getAllCities() {
        return cityDao.findAll();
    }

    /**
     * Get a city by ID.
     */
    public Optional<City> getCityById(Long id) {
        return cityDao.findById(id);
    }

    /**
     * Create a new city.
     */
    public Long createCity(City city) {
        return cityDao.insert(city);
    }

    /**
     * Update an existing city.
     */
    public boolean updateCity(City city) {
        return cityDao.update(city.getId(), city);
    }

    /**
     * Delete a city by ID.
     */
    public boolean deleteCity(Long id) {
        return cityDao.delete(id);
    }

    /**
     * Find cities by country code.
     */
    public List<City> findByCountryCode(String countryCode) {
        return cityDao.findByCountryCode(countryCode);
    }

    /**
     * Find cities by name.
     */
    public List<City> findByName(String name) {
        return cityDao.findByName(name);
    }

    /**
     * Import cities from a tar.gz file URL asynchronously.
     * This method downloads, decompresses, parses, and loads city data into the database.
     *
     * @param fileUrl URL to the tar.gz file containing city data
     * @return CompletableFuture that completes when import is done
     */
    public CompletableFuture<ImportResult> importCitiesFromUrl(String fileUrl) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Starting city import from URL: {}", fileUrl);
            ImportResult result = new ImportResult();
            result.setStartTime(System.currentTimeMillis());

            try {
                // Download and process the file
                URL url = new URL(fileUrl);
                InputStream inputStream;

                // Handle file:// URLs differently from http:// URLs
                if (fileUrl.startsWith("file://")) {
                    // For file URLs, just open the stream directly
                    inputStream = new BufferedInputStream(url.openStream());
                } else {
                    // For HTTP URLs, use HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(60000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Failed to download file. HTTP response code: " + responseCode);
                    }

                    inputStream = new BufferedInputStream(connection.getInputStream());
                }

                try (InputStream stream = inputStream) {
                    processTarGzFile(stream, result);
                }

                result.setSuccess(true);
                result.setEndTime(System.currentTimeMillis());
                LOGGER.info("City import completed successfully. Imported {} cities in {} ms",
                    result.getCitiesImported(), result.getDuration());

            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
                result.setEndTime(System.currentTimeMillis());
                LOGGER.error("City import failed: {}", e.getMessage(), e);
            }

            return result;
        }, EXECUTOR);
    }

    /**
     * Process a tar.gz file and import cities.
     */
    private void processTarGzFile(InputStream inputStream, ImportResult result) throws Exception {
        try (GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(inputStream);
             TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {

            TarArchiveEntry entry;
            while ((entry = tarIn.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".txt")) {
                    LOGGER.info("Processing file: {}", entry.getName());
                    parseCityFile(tarIn, result);
                }
            }
        }
    }

    /**
     * Parse city data from a text file (GeoNames format).
     * GeoNames format is tab-delimited with the following columns:
     * 0: geonameid
     * 1: name
     * 2: asciiname
     * 3: alternatenames (skipped - not stored)
     * 4: latitude
     * 5: longitude
     * 6: feature class
     * 7: feature code
     * 8: country code
     * 9: cc2
     * 10: admin1 code
     * 11: admin2 code
     * 12: admin3 code
     * 13: admin4 code
     * 14: population
     * 15: elevation
     * 16: dem
     * 17: timezone
     * 18: modification date
     */
    private void parseCityFile(InputStream inputStream, ImportResult result) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<City> batch = new ArrayList<>();
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            try {
                String[] fields = line.split("\t", -1);
                if (fields.length < 19) {
                    LOGGER.warn("Skipping line {} - insufficient fields: {}", lineNumber, fields.length);
                    result.incrementSkipped();
                    continue;
                }

                City city = parseCity(fields);
                batch.add(city);

                // Batch insert for performance
                if (batch.size() >= BATCH_SIZE) {
                    int inserted = cityDao.batchInsert(batch);
                    result.addCitiesImported(inserted);
                    LOGGER.info("Imported batch of {} cities. Total: {}", inserted, result.getCitiesImported());
                    batch.clear();
                }

            } catch (Exception e) {
                LOGGER.warn("Error parsing line {}: {}", lineNumber, e.getMessage());
                result.incrementSkipped();
            }
        }

        // Insert remaining cities
        if (!batch.isEmpty()) {
            int inserted = cityDao.batchInsert(batch);
            result.addCitiesImported(inserted);
            LOGGER.info("Imported final batch of {} cities. Total: {}", inserted, result.getCitiesImported());
        }
    }

    /**
     * Parse a single city from GeoNames tab-delimited format.
     */
    private City parseCity(String[] fields) {
        City city = new City();

        city.setGeonameId(parseLong(fields[0]));
        city.setName(fields[1]);
        city.setAsciiName(fields[2]);
        // Skip fields[3] - alternate names (not stored)
        city.setLatitude(parseBigDecimal(fields[4]));
        city.setLongitude(parseBigDecimal(fields[5]));
        city.setFeatureClass(fields[6]);
        city.setFeatureCode(fields[7]);
        city.setCountryCode(fields[8]);
        city.setCc2(fields[9]);
        city.setAdmin1Code(fields[10]);
        city.setAdmin2Code(fields[11]);
        city.setAdmin3Code(fields[12]);
        city.setAdmin4Code(fields[13]);
        city.setPopulation(parseLong(fields[14]));
        city.setElevation(parseInteger(fields[15]));
        city.setDem(parseInteger(fields[16]));
        city.setTimezone(fields[17]);
        city.setModificationDate(parseDate(fields[18]));

        return city;
    }

    private Long parseLong(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date parseDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Result object for import operations.
     */
    public static class ImportResult {
        private boolean success;
        private int citiesImported;
        private int citiesSkipped;
        private String errorMessage;
        private long startTime;
        private long endTime;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getCitiesImported() {
            return citiesImported;
        }

        public void addCitiesImported(int count) {
            this.citiesImported += count;
        }

        public int getCitiesSkipped() {
            return citiesSkipped;
        }

        public void incrementSkipped() {
            this.citiesSkipped++;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getDuration() {
            return endTime - startTime;
        }
    }
}

