package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.JobRequirement;
import com.irusso.demoserver.service.JobRequirementService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for JobRequirement operations.
 */
@Path("/api/job-requirements")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobRequirementResource {

    private final JobRequirementService jobRequirementService;

    @Inject
    public JobRequirementResource(JobRequirementService jobRequirementService) {
        this.jobRequirementService = jobRequirementService;
    }

    @GET
    public Response getAllJobRequirements() {
        List<JobRequirement> requirements = jobRequirementService.getAllJobRequirements();
        return Response.ok(ApiResponse.success(requirements)).build();
    }

    @GET
    @Path("/{id}")
    public Response getJobRequirement(@PathParam("id") Long id) {
        Optional<JobRequirement> requirement = jobRequirementService.getJobRequirementById(id);
        if (requirement.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job requirement not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(requirement.get())).build();
    }

    @POST
    public Response createJobRequirement(@Valid JobRequirement requirement) {
        Long id = jobRequirementService.createJobRequirement(requirement);
        requirement.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Job requirement created successfully", requirement))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateJobRequirement(@PathParam("id") Long id, @Valid JobRequirement requirement) {
        if (jobRequirementService.getJobRequirementById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job requirement not found"))
                    .build();
        }
        requirement.setId(id);
        jobRequirementService.updateJobRequirement(requirement);
        return Response.ok(ApiResponse.success("Job requirement updated successfully", requirement)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteJobRequirement(@PathParam("id") Long id) {
        boolean deleted = jobRequirementService.deleteJobRequirement(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job requirement not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Job requirement deleted successfully", null)).build();
    }

    @GET
    @Path("/job-posting/{jobPostingId}")
    public Response getJobRequirementsByJobPosting(@PathParam("jobPostingId") Long jobPostingId) {
        List<JobRequirement> requirements = jobRequirementService.findByJobPostingId(jobPostingId);
        return Response.ok(ApiResponse.success(requirements)).build();
    }
}

