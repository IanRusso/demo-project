package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.SavedJob;
import com.irusso.demoserver.service.SavedJobService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for SavedJob operations.
 */
@Path("/api/saved-jobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SavedJobResource {

    private final SavedJobService savedJobService;

    @Inject
    public SavedJobResource(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    @GET
    public Response getAllSavedJobs() {
        List<SavedJob> savedJobs = savedJobService.getAllSavedJobs();
        return Response.ok(ApiResponse.success(savedJobs)).build();
    }

    @GET
    @Path("/{id}")
    public Response getSavedJob(@PathParam("id") Long id) {
        Optional<SavedJob> savedJob = savedJobService.getSavedJobById(id);
        if (savedJob.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Saved job not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(savedJob.get())).build();
    }

    @POST
    public Response createSavedJob(@Valid SavedJob savedJob) {
        Long id = savedJobService.createSavedJob(savedJob);
        savedJob.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Saved job created successfully", savedJob))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateSavedJob(@PathParam("id") Long id, @Valid SavedJob savedJob) {
        if (savedJobService.getSavedJobById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Saved job not found"))
                    .build();
        }
        savedJob.setId(id);
        savedJobService.updateSavedJob(savedJob);
        return Response.ok(ApiResponse.success("Saved job updated successfully", savedJob)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteSavedJob(@PathParam("id") Long id) {
        boolean deleted = savedJobService.deleteSavedJob(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Saved job not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Saved job deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getSavedJobsByUser(@PathParam("userId") Long userId) {
        List<SavedJob> savedJobs = savedJobService.findByUserId(userId);
        return Response.ok(ApiResponse.success(savedJobs)).build();
    }
}

