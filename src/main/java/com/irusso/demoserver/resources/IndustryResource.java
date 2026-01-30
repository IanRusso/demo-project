package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.api.IndustryImportRequest;
import com.irusso.demoserver.db.model.Industry;
import com.irusso.demoserver.service.IndustryService;
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
 * REST resource for industry-related endpoints.
 * Provides CRUD operations and CSV import functionality.
 */
@Path("/api/industries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IndustryResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndustryResource.class);

    private final IndustryService industryService;

    @Inject
    public IndustryResource(IndustryService industryService) {
        this.industryService = industryService;
    }

    /**
     * Get all industries.
     */
    @GET
    public Response getAllIndustries(@QueryParam("search") String searchTerm) {
        List<Industry> industries;
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            industries = industryService.searchIndustries(searchTerm);
        } else {
            industries = industryService.getAllIndustries();
        }
        return Response.ok(ApiResponse.success(industries)).build();
    }

    /**
     * Get an industry by ID.
     */
    @GET
    @Path("/{id}")
    public Response getIndustryById(@PathParam("id") Long id) {
        Optional<Industry> industry = industryService.getIndustryById(id);
        if (industry.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Industry not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(industry.get())).build();
    }

    /**
     * Create a new industry.
     */
    @POST
    public Response createIndustry(@Valid Industry industry) {
        Industry created = industryService.createIndustry(industry);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Industry created successfully", created))
                .build();
    }

    /**
     * Update an existing industry.
     */
    @PUT
    @Path("/{id}")
    public Response updateIndustry(@PathParam("id") Long id, @Valid Industry industry) {
        industry.setId(id);
        Industry updated = industryService.updateIndustry(industry);
        return Response.ok(ApiResponse.success("Industry updated successfully", updated)).build();
    }

    /**
     * Delete an industry.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteIndustry(@PathParam("id") Long id) {
        boolean deleted = industryService.deleteIndustry(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Industry not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Industry deleted successfully")).build();
    }

    /**
     * Import industries from a CSV file URL.
     * This endpoint kicks off an async process to download, parse, and load industries.
     *
     * @param request Contains the URL to the CSV file
     * @return Immediate response indicating the import has started
     */
    @POST
    @Path("/import")
    public Response importIndustries(@Valid IndustryImportRequest request) {
        LOGGER.info("Received industry import request for URL: {}", request.getUrl());

        // Validate URL format
        if (!isValidUrl(request.getUrl())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Invalid URL format"))
                    .build();
        }

        // Start async import
        industryService.importIndustriesFromUrl(request.getUrl())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        LOGGER.info("Industry import completed: {} industries imported, {} skipped in {} ms",
                                result.getIndustriesImported(), result.getIndustriesSkipped(), result.getDuration());
                    } else {
                        LOGGER.error("Industry import failed: {}", result.getErrorMessage());
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("Industry import failed with exception", throwable);
                    return null;
                });

        // Return immediate response
        Map<String, String> responseData = new HashMap<>();
        responseData.put("status", "Import started");
        responseData.put("url", request.getUrl());
        responseData.put("message", "Industry import is processing in the background. Check server logs for progress.");

        return Response.status(Response.Status.ACCEPTED)
                .entity(ApiResponse.success("Industry import started successfully", responseData))
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

