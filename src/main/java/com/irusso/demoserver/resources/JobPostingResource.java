package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.JobPosting;
import com.irusso.demoserver.service.JobPostingService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for JobPosting operations.
 * Provides CRUD endpoints for managing job postings.
 */
@Path("/api/job-postings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class JobPostingResource {

    private final JobPostingService jobPostingService;

    @Inject
    public JobPostingResource(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GET
    public Response getAllJobPostings() {
        List<JobPosting> jobPostings = jobPostingService.getAllJobPostings();
        return Response.ok(ApiResponse.success(jobPostings)).build();
    }

    @GET
    @Path("/{id}")
    public Response getJobPosting(@PathParam("id") Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job posting not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(jobPosting.get())).build();
    }

    @POST
    public Response createJobPosting(@Valid JobPosting jobPosting) {
        Long id = jobPostingService.createJobPosting(jobPosting);
        jobPosting.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Job posting created successfully", jobPosting))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateJobPosting(@PathParam("id") Long id, @Valid JobPosting jobPosting) {
        if (jobPostingService.getJobPostingById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job posting not found"))
                    .build();
        }
        jobPosting.setId(id);
        jobPostingService.updateJobPosting(jobPosting);
        return Response.ok(ApiResponse.success("Job posting updated successfully", jobPosting)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteJobPosting(@PathParam("id") Long id) {
        boolean deleted = jobPostingService.deleteJobPosting(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Job posting not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Job posting deleted successfully", null)).build();
    }

    @GET
    @Path("/employer/{employerId}")
    public Response getJobPostingsByEmployer(@PathParam("employerId") Long employerId) {
        List<JobPosting> jobPostings = jobPostingService.findByEmployerId(employerId);
        return Response.ok(ApiResponse.success(jobPostings)).build();
    }
}

