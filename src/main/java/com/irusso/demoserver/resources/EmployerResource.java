package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.Employer;
import com.irusso.demoserver.service.EmployerService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for Employer operations.
 * Provides CRUD endpoints for managing employers.
 */
@Path("/api/employers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployerResource {

    private final EmployerService employerService;

    @Inject
    public EmployerResource(EmployerService employerService) {
        this.employerService = employerService;
    }

    /**
     * Get all employers.
     * GET /api/employers
     */
    @GET
    public Response getAllEmployers() {
        List<Employer> employers = employerService.getAllEmployers();
        return Response.ok(ApiResponse.success(employers)).build();
    }

    /**
     * Get a specific employer by ID.
     * GET /api/employers/{id}
     */
    @GET
    @Path("/{id}")
    public Response getEmployer(@PathParam("id") Long id) {
        Optional<Employer> employer = employerService.getEmployerById(id);
        if (employer.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(employer.get())).build();
    }

    /**
     * Create a new employer.
     * POST /api/employers
     */
    @POST
    public Response createEmployer(@Valid Employer employer) {
        Long id = employerService.createEmployer(employer);
        employer.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Employer created successfully", employer))
                .build();
    }

    /**
     * Update an existing employer.
     * PUT /api/employers/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateEmployer(@PathParam("id") Long id, @Valid Employer employer) {
        if (employerService.getEmployerById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer not found"))
                    .build();
        }
        employer.setId(id);
        employerService.updateEmployer(employer);
        return Response.ok(ApiResponse.success("Employer updated successfully", employer)).build();
    }

    /**
     * Delete an employer.
     * DELETE /api/employers/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteEmployer(@PathParam("id") Long id) {
        boolean deleted = employerService.deleteEmployer(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Employer deleted successfully", null)).build();
    }
}

