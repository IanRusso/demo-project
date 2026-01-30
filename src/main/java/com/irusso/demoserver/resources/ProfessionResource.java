package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.api.ProfessionImportRequest;
import com.irusso.demoserver.db.model.PreparationLevel;
import com.irusso.demoserver.db.model.Profession;
import com.irusso.demoserver.service.ProfessionService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST resource for profession-related endpoints.
 * Provides CRUD operations and CSV import functionality.
 */
@Path("/api/professions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfessionResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfessionResource.class);

    private final ProfessionService professionService;

    @Inject
    public ProfessionResource(ProfessionService professionService) {
        this.professionService = professionService;
    }

    /**
     * Get all professions, optionally filtered by search term or preparation level.
     */
    @GET
    public Response getAllProfessions(
            @QueryParam("search") String searchTerm,
            @QueryParam("prep") Integer prepLevel) {
        
        List<Profession> professions;
        
        if (prepLevel != null) {
            try {
                PreparationLevel level = PreparationLevel.fromValue(prepLevel);
                professions = professionService.getProfessionsByPreparationLevel(level);
            } catch (IllegalArgumentException e) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Invalid preparation level. Must be 1-5."))
                        .build();
            }
        } else if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            professions = professionService.searchProfessions(searchTerm);
        } else {
            professions = professionService.getAllProfessions();
        }
        
        return Response.ok(ApiResponse.success(professions)).build();
    }

    /**
     * Get a profession by ID.
     */
    @GET
    @Path("/{id}")
    public Response getProfessionById(@PathParam("id") Long id) {
        Optional<Profession> profession = professionService.getProfessionById(id);
        if (profession.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Profession not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(profession.get())).build();
    }

    /**
     * Create a new profession.
     */
    @POST
    public Response createProfession(@Valid Profession profession) {
        Profession created = professionService.createProfession(profession);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Profession created successfully", created))
                .build();
    }

    /**
     * Update an existing profession.
     */
    @PUT
    @Path("/{id}")
    public Response updateProfession(@PathParam("id") Long id, @Valid Profession profession) {
        profession.setId(id);
        Profession updated = professionService.updateProfession(profession);
        return Response.ok(ApiResponse.success("Profession updated successfully", updated)).build();
    }

    /**
     * Delete a profession.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteProfession(@PathParam("id") Long id) {
        boolean deleted = professionService.deleteProfession(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Profession not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Profession deleted successfully")).build();
    }

    /**
     * Import professions from a CSV file URL.
     * This endpoint kicks off an async process to download, parse, and load professions.
     *
     * @param request Contains the URL to the CSV file
     * @return Immediate response indicating the import has started
     */
    @POST
    @Path("/import")
    public Response importProfessions(@Valid ProfessionImportRequest request) {
        LOGGER.info("Received profession import request for URL: {}", request.getUrl());

        // Validate URL format
        if (!isValidUrl(request.getUrl())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Invalid URL format"))
                    .build();
        }

        // Start async import
        professionService.importProfessionsFromUrl(request.getUrl())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        LOGGER.info("Profession import completed: {} professions imported, {} skipped in {} ms",
                                result.getProfessionsImported(), result.getProfessionsSkipped(), result.getDuration());
                    } else {
                        LOGGER.error("Profession import failed: {}", result.getErrorMessage());
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Profession import failed with exception", throwable);
                    return null;
                });

        // Return immediate response
        Map<String, String> responseData = new HashMap<>();
        responseData.put("status", "Import started");
        responseData.put("url", request.getUrl());
        responseData.put("message", "Profession import is processing in the background. Check server logs for progress.");

        return Response.status(Response.Status.ACCEPTED)
                .entity(ApiResponse.success("Profession import started successfully", responseData))
                .build();
    }

    /**
     * Validate URL format.
     */
    private boolean isValidUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        try {
            new java.net.URL(url);
            return url.startsWith("http://") || url.startsWith("https://") || url.startsWith("file://");
        } catch (Exception e) {
            return false;
        }
    }
}

