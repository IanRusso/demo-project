package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.Application;
import com.irusso.demoserver.service.ApplicationService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for Application operations.
 * Provides CRUD endpoints for managing applications.
 */
@Path("/api/applications")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationResource {

    private final ApplicationService applicationService;

    @Inject
    public ApplicationResource(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GET
    public Response getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        return Response.ok(ApiResponse.success(applications)).build();
    }

    @GET
    @Path("/{id}")
    public Response getApplication(@PathParam("id") Long id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        if (application.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(application.get())).build();
    }

    @POST
    public Response createApplication(@Valid Application application) {
        Long id = applicationService.createApplication(application);
        application.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Application created successfully", application))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateApplication(@PathParam("id") Long id, @Valid Application application) {
        if (applicationService.getApplicationById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application not found"))
                    .build();
        }
        application.setId(id);
        applicationService.updateApplication(application);
        return Response.ok(ApiResponse.success("Application updated successfully", application)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteApplication(@PathParam("id") Long id) {
        boolean deleted = applicationService.deleteApplication(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Application deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getApplicationsByUser(@PathParam("userId") Long userId) {
        List<Application> applications = applicationService.findByUserId(userId);
        return Response.ok(ApiResponse.success(applications)).build();
    }

    @GET
    @Path("/job-posting/{jobPostingId}")
    public Response getApplicationsByJobPosting(@PathParam("jobPostingId") Long jobPostingId) {
        List<Application> applications = applicationService.findByJobPostingId(jobPostingId);
        return Response.ok(ApiResponse.success(applications)).build();
    }
}

