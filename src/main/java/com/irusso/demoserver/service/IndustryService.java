package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.IndustryDao;
import com.irusso.demoserver.db.model.Industry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Service layer for Industry operations.
 * Handles business logic including CSV import.
 */
public class IndustryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndustryService.class);
    private static final int BATCH_SIZE = 100;

    private final IndustryDao industryDao;

    @Inject
    public IndustryService(IndustryDao industryDao) {
        this.industryDao = industryDao;
    }

    /**
     * Get all industries.
     */
    public List<Industry> getAllIndustries() {
        return industryDao.getAllOrdered();
    }

    /**
     * Get industry by ID.
     */
    public Optional<Industry> getIndustryById(Long id) {
        return industryDao.findById(id);
    }

    /**
     * Search industries by name.
     */
    public List<Industry> searchIndustries(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllIndustries();
        }
        return industryDao.searchByName(searchTerm);
    }

    /**
     * Create a new industry.
     */
    public Industry createIndustry(Industry industry) {
        Long id = industryDao.insert(industry);
        industry.setId(id);
        return industry;
    }

    /**
     * Update an existing industry.
     */
    public Industry updateIndustry(Industry industry) {
        industryDao.update(industry.getId(), industry);
        return industry;
    }

    /**
     * Delete an industry.
     */
    public boolean deleteIndustry(Long id) {
        return industryDao.delete(id);
    }

    /**
     * Import industries from a CSV file URL asynchronously.
     * Expected CSV format: ID,Industry
     *
     * @param fileUrl URL to the CSV file
     * @return CompletableFuture that completes when import is done
     */
    public CompletableFuture<ImportResult> importIndustriesFromUrl(String fileUrl) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Starting industry import from URL: {}", fileUrl);
            ImportResult result = new ImportResult();
            result.setStartTime(System.currentTimeMillis());

            try {
                // Download and process the file
                URL url = new URL(fileUrl);
                InputStream inputStream;

                // Handle file:// URLs differently from http:// URLs
                if (fileUrl.startsWith("file://")) {
                    inputStream = url.openStream();
                } else {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(30000);
                    connection.setReadTimeout(60000);

                    int responseCode = connection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new RuntimeException("Failed to download file. HTTP response code: " + responseCode);
                    }

                    inputStream = connection.getInputStream();
                }

                try (InputStream stream = inputStream) {
                    parseCsvFile(stream, result);
                }

                result.setSuccess(true);
                result.setEndTime(System.currentTimeMillis());
                LOGGER.info("Industry import completed successfully: {} imported, {} skipped in {} ms",
                        result.getIndustriesImported(), result.getIndustriesSkipped(), result.getDuration());

            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
                result.setEndTime(System.currentTimeMillis());
                LOGGER.error("Industry import failed", e);
            }

            return result;
        });
    }

    /**
     * Parse CSV file and import industries.
     * Expected format: ID,Industry
     */
    private void parseCsvFile(InputStream inputStream, ImportResult result) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<Industry> batch = new ArrayList<>();
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            
            // Skip header line
            if (lineNumber == 1 && line.toLowerCase().contains("industry")) {
                continue;
            }

            try {
                String[] fields = line.split(",", -1);
                if (fields.length < 2) {
                    LOGGER.warn("Skipping line {} - insufficient fields: {}", lineNumber, fields.length);
                    result.incrementSkipped();
                    continue;
                }

                // Parse: ID,Industry
                String industryName = fields[1].trim();
                
                if (industryName.isEmpty()) {
                    LOGGER.warn("Skipping line {} - empty industry name", lineNumber);
                    result.incrementSkipped();
                    continue;
                }

                Industry industry = new Industry(industryName);
                batch.add(industry);

                // Batch insert for performance
                if (batch.size() >= BATCH_SIZE) {
                    int inserted = industryDao.batchInsert(batch);
                    result.addIndustriesImported(inserted);
                    LOGGER.info("Imported batch of {} industries. Total: {}", inserted, result.getIndustriesImported());
                    batch.clear();
                }

            } catch (Exception e) {
                LOGGER.warn("Error parsing line {}: {}", lineNumber, e.getMessage());
                result.incrementSkipped();
            }
        }

        // Insert remaining batch
        if (!batch.isEmpty()) {
            int inserted = industryDao.batchInsert(batch);
            result.addIndustriesImported(inserted);
            LOGGER.info("Imported final batch of {} industries. Total: {}", inserted, result.getIndustriesImported());
        }
    }

    /**
     * Result object for import operations.
     */
    public static class ImportResult {
        private boolean success;
        private int industriesImported;
        private int industriesSkipped;
        private String errorMessage;
        private long startTime;
        private long endTime;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getIndustriesImported() {
            return industriesImported;
        }

        public void addIndustriesImported(int count) {
            this.industriesImported += count;
        }

        public int getIndustriesSkipped() {
            return industriesSkipped;
        }

        public void incrementSkipped() {
            this.industriesSkipped++;
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

