package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.EmploymentHistory;
import com.irusso.demoserver.service.EmploymentHistoryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for EmploymentHistory operations.
 */
@Path("/api/employment-history")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmploymentHistoryResource {

    private final EmploymentHistoryService employmentHistoryService;

    @Inject
    public EmploymentHistoryResource(EmploymentHistoryService employmentHistoryService) {
        this.employmentHistoryService = employmentHistoryService;
    }

    @GET
    public Response getAllEmploymentHistory() {
        List<EmploymentHistory> history = employmentHistoryService.getAllEmploymentHistory();
        return Response.ok(ApiResponse.success(history)).build();
    }

    @GET
    @Path("/{id}")
    public Response getEmploymentHistory(@PathParam("id") Long id) {
        Optional<EmploymentHistory> history = employmentHistoryService.getEmploymentHistoryById(id);
        if (history.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employment history not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(history.get())).build();
    }

    @POST
    public Response createEmploymentHistory(@Valid EmploymentHistory history) {
        Long id = employmentHistoryService.createEmploymentHistory(history);
        history.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Employment history created successfully", history))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmploymentHistory(@PathParam("id") Long id, @Valid EmploymentHistory history) {
        if (employmentHistoryService.getEmploymentHistoryById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employment history not found"))
                    .build();
        }
        history.setId(id);
        employmentHistoryService.updateEmploymentHistory(history);
        return Response.ok(ApiResponse.success("Employment history updated successfully", history)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteEmploymentHistory(@PathParam("id") Long id) {
        boolean deleted = employmentHistoryService.deleteEmploymentHistory(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Employment history not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Employment history deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getEmploymentHistoryByUser(@PathParam("userId") Long userId) {
        List<EmploymentHistory> history = employmentHistoryService.findByUserId(userId);
        return Response.ok(ApiResponse.success(history)).build();
    }
}

