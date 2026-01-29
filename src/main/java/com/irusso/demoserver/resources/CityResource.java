package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.api.CityImportRequest;
import com.irusso.demoserver.db.model.City;
import com.irusso.demoserver.service.CityService;
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
 * REST resource for city-related endpoints.
 * Provides CRUD operations and city import functionality.
 */
@Path("/api/cities")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CityResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CityResource.class);

    private final CityService cityService;

    @Inject
    public CityResource(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * Get all cities.
     */
    @GET
    public Response getAllCities() {
        List<City> cities = cityService.getAllCities();
        return Response.ok(ApiResponse.success(cities)).build();
    }

    /**
     * Get a city by ID.
     */
    @GET
    @Path("/{id}")
    public Response getCityById(@PathParam("id") Long id) {
        Optional<City> city = cityService.getCityById(id);
        if (city.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("City not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(city.get())).build();
    }

    /**
     * Create a new city.
     */
    @POST
    public Response createCity(@Valid City city) {
        Long id = cityService.createCity(city);
        city.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("City created successfully", city))
                .build();
    }

    /**
     * Update an existing city.
     */
    @PUT
    @Path("/{id}")
    public Response updateCity(@PathParam("id") Long id, @Valid City city) {
        city.setId(id);
        boolean updated = cityService.updateCity(city);
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("City not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("City updated successfully", city)).build();
    }

    /**
     * Delete a city by ID.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCity(@PathParam("id") Long id) {
        boolean deleted = cityService.deleteCity(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("City not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("City deleted successfully", null)).build();
    }

    /**
     * Find cities by country code.
     */
    @GET
    @Path("/country/{countryCode}")
    public Response getCitiesByCountry(@PathParam("countryCode") String countryCode) {
        List<City> cities = cityService.findByCountryCode(countryCode);
        return Response.ok(ApiResponse.success(cities)).build();
    }

    /**
     * Find cities by name.
     */
    @GET
    @Path("/search")
    public Response searchCitiesByName(@QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Name parameter is required"))
                    .build();
        }
        List<City> cities = cityService.findByName(name);
        return Response.ok(ApiResponse.success(cities)).build();
    }

    /**
     * Import cities from a tar.gz file URL.
     * This endpoint kicks off an async process to download, decompress, parse, and load cities.
     *
     * @param request Contains the URL to the tar.gz file
     * @return Immediate response indicating the import has started
     */
    @POST
    @Path("/import")
    public Response importCities(@Valid CityImportRequest request) {
        LOGGER.info("Received city import request for URL: {}", request.getUrl());

        // Validate URL format
        if (!isValidUrl(request.getUrl())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Invalid URL format"))
                    .build();
        }

        // Start async import
        cityService.importCitiesFromUrl(request.getUrl())
                .thenAccept(result -> {
                    if (result.isSuccess()) {
                        LOGGER.info("City import completed: {} cities imported, {} skipped in {} ms",
                                result.getCitiesImported(), result.getCitiesSkipped(), result.getDuration());
                    } else {
                        LOGGER.error("City import failed: {}", result.getErrorMessage());
                    }
                })
                .exceptionally(throwable -> {
                    LOGGER.error("City import failed with exception", throwable);
                    return null;
                });

        // Return immediate response
        Map<String, String> responseData = new HashMap<>();
        responseData.put("status", "Import started");
        responseData.put("url", request.getUrl());
        responseData.put("message", "City import is processing in the background. Check server logs for progress.");

        return Response.status(Response.Status.ACCEPTED)
                .entity(ApiResponse.success("City import started successfully", responseData))
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

