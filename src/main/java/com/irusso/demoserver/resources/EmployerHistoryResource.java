package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.EmployerHistory;
import com.irusso.demoserver.service.EmployerHistoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for EmployerHistory operations.
 */
@Path("/api/employer-history")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployerHistoryResource {

    private final EmployerHistoryService employerHistoryService;

    @Inject
    public EmployerHistoryResource(EmployerHistoryService employerHistoryService) {
        this.employerHistoryService = employerHistoryService;
    }

    @GET
    public Response getAllEmployerHistory() {
        List<EmployerHistory> history = employerHistoryService.getAllEmployerHistory();
        return Response.ok(ApiResponse.success(history)).build();
    }

    @GET
    @Path("/{id}")
    public Response getEmployerHistory(@PathParam("id") Long id) {
        Optional<EmployerHistory> history = employerHistoryService.getEmployerHistoryById(id);
        if (history.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer history not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(history.get())).build();
    }

    @POST
    public Response createEmployerHistory(@Valid EmployerHistory history) {
        Long id = employerHistoryService.createEmployerHistory(history);
        history.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Employer history created successfully", history))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmployerHistory(@PathParam("id") Long id, @Valid EmployerHistory history) {
        if (employerHistoryService.getEmployerHistoryById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer history not found"))
                    .build();
        }
        history.setId(id);
        employerHistoryService.updateEmployerHistory(history);
        return Response.ok(ApiResponse.success("Employer history updated successfully", history)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmployerHistory(@PathParam("id") Long id) {
        boolean deleted = employerHistoryService.deleteEmployerHistory(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employer history not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Employer history deleted successfully", null)).build();
    }

    @GET
    @Path("/employer/{employerId}")
    public Response getEmployerHistoryByEmployer(@PathParam("employerId") Long employerId) {
        List<EmployerHistory> history = employerHistoryService.findByEmployerId(employerId);
        return Response.ok(ApiResponse.success(history)).build();
    }
}

