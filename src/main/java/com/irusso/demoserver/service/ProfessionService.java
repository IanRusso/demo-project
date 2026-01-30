package com.irusso.demoserver.service;

import com.google.inject.Inject;
import com.irusso.demoserver.db.dao.ProfessionDao;
import com.irusso.demoserver.db.model.PreparationLevel;
import com.irusso.demoserver.db.model.Profession;
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
 * Service layer for Profession operations.
 * Handles business logic including CSV import.
 */
public class ProfessionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionService.class);
    private static final int BATCH_SIZE = 100;

    private final ProfessionDao professionDao;

    @Inject
    public ProfessionService(ProfessionDao professionDao) {
        this.professionDao = professionDao;
    }

    /**
     * Get all professions.
     */
    public List<Profession> getAllProfessions() {
        return professionDao.getAllOrdered();
    }

    /**
     * Get profession by ID.
     */
    public Optional<Profession> getProfessionById(Long id) {
        return professionDao.findById(id);
    }

    /**
     * Get professions by preparation level.
     */
    public List<Profession> getProfessionsByPreparationLevel(PreparationLevel prepLevel) {
        return professionDao.getByPreparationLevel(prepLevel);
    }

    /**
     * Search professions by name.
     */
    public List<Profession> searchProfessions(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllProfessions();
        }
        return professionDao.searchByName(searchTerm);
    }

    /**
     * Create a new profession.
     */
    public Profession createProfession(Profession profession) {
        Long id = professionDao.insert(profession);
        profession.setId(id);
        return profession;
    }

    /**
     * Update an existing profession.
     */
    public Profession updateProfession(Profession profession) {
        professionDao.update(profession.getId(), profession);
        return profession;
    }

    /**
     * Delete a profession.
     */
    public boolean deleteProfession(Long id) {
        return professionDao.delete(id);
    }

    /**
     * Import professions from a CSV file URL asynchronously.
     * Expected CSV format: ID,Job Zone,Occupation
     *
     * @param fileUrl URL to the CSV file
     * @return CompletableFuture that completes when import is done
     */
    public CompletableFuture<ImportResult> importProfessionsFromUrl(String fileUrl) {
        return CompletableFuture.supplyAsync(() -> {
            LOGGER.info("Starting profession import from URL: {}", fileUrl);
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
                LOGGER.info("Profession import completed successfully: {} imported, {} skipped in {} ms",
                        result.getProfessionsImported(), result.getProfessionsSkipped(), result.getDuration());

            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMessage(e.getMessage());
                result.setEndTime(System.currentTimeMillis());
                LOGGER.error("Profession import failed", e);
            }

            return result;
        });
    }

    /**
     * Parse CSV file and import professions.
     * Expected format: ID,Job Zone,Occupation
     */
    private void parseCsvFile(InputStream inputStream, ImportResult result) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<Profession> batch = new ArrayList<>();
        String line;
        int lineNumber = 0;

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            
            // Skip header line
            if (lineNumber == 1 && line.toLowerCase().contains("occupation")) {
                continue;
            }

            try {
                String[] fields = line.split(",", -1);
                if (fields.length < 3) {
                    LOGGER.warn("Skipping line {} - insufficient fields: {}", lineNumber, fields.length);
                    result.incrementSkipped();
                    continue;
                }

                // Parse: ID,Job Zone,Occupation
                String jobZoneStr = fields[1].trim();
                String professionName = fields[2].trim();
                
                if (professionName.isEmpty()) {
                    LOGGER.warn("Skipping line {} - empty profession name", lineNumber);
                    result.incrementSkipped();
                    continue;
                }

                // Parse Job Zone (can be empty or a decimal like "4.0")
                PreparationLevel prepLevel = null;
                if (!jobZoneStr.isEmpty()) {
                    try {
                        double jobZone = Double.parseDouble(jobZoneStr);
                        prepLevel = PreparationLevel.fromDouble(jobZone);
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Skipping line {} - invalid job zone: {}", lineNumber, jobZoneStr);
                        result.incrementSkipped();
                        continue;
                    }
                } else {
                    LOGGER.warn("Skipping line {} - empty job zone", lineNumber);
                    result.incrementSkipped();
                    continue;
                }

                Profession profession = new Profession(professionName, prepLevel);
                batch.add(profession);

                // Batch insert for performance
                if (batch.size() >= BATCH_SIZE) {
                    int inserted = professionDao.batchInsert(batch);
                    result.addProfessionsImported(inserted);
                    LOGGER.info("Imported batch of {} professions. Total: {}", inserted, result.getProfessionsImported());
                    batch.clear();
                }

            } catch (Exception e) {
                LOGGER.warn("Error parsing line {}: {}", lineNumber, e.getMessage());
                result.incrementSkipped();
            }
        }

        // Insert remaining batch
        if (!batch.isEmpty()) {
            int inserted = professionDao.batchInsert(batch);
            result.addProfessionsImported(inserted);
            LOGGER.info("Imported final batch of {} professions. Total: {}", inserted, result.getProfessionsImported());
        }
    }

    /**
     * Result object for import operations.
     */
    public static class ImportResult {
        private boolean success;
        private int professionsImported;
        private int professionsSkipped;
        private String errorMessage;
        private long startTime;
        private long endTime;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getProfessionsImported() {
            return professionsImported;
        }

        public void addProfessionsImported(int count) {
            this.professionsImported += count;
        }

        public int getProfessionsSkipped() {
            return professionsSkipped;
        }

        public void incrementSkipped() {
            this.professionsSkipped++;
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

