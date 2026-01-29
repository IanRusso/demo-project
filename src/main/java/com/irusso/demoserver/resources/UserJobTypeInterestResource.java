package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserJobTypeInterest;
import com.irusso.demoserver.service.UserJobTypeInterestService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserJobTypeInterest operations.
 */
@Path("/api/user-job-type-interests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserJobTypeInterestResource {

    private final UserJobTypeInterestService userJobTypeInterestService;

    @Inject
    public UserJobTypeInterestResource(UserJobTypeInterestService userJobTypeInterestService) {
        this.userJobTypeInterestService = userJobTypeInterestService;
    }

    @GET
    public Response getAllUserJobTypeInterests() {
        List<UserJobTypeInterest> interests = userJobTypeInterestService.getAllUserJobTypeInterests();
        return Response.ok(ApiResponse.success(interests)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserJobTypeInterest(@PathParam("id") Long id) {
        Optional<UserJobTypeInterest> interest = userJobTypeInterestService.getUserJobTypeInterestById(id);
        if (interest.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User job type interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(interest.get())).build();
    }

    @POST
    public Response createUserJobTypeInterest(@Valid UserJobTypeInterest interest) {
        Long id = userJobTypeInterestService.createUserJobTypeInterest(interest);
        interest.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User job type interest created successfully", interest))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserJobTypeInterest(@PathParam("id") Long id, @Valid UserJobTypeInterest interest) {
        if (userJobTypeInterestService.getUserJobTypeInterestById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User job type interest not found"))
                    .build();
        }
        interest.setId(id);
        userJobTypeInterestService.updateUserJobTypeInterest(interest);
        return Response.ok(ApiResponse.success("User job type interest updated successfully", interest)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserJobTypeInterest(@PathParam("id") Long id) {
        boolean deleted = userJobTypeInterestService.deleteUserJobTypeInterest(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User job type interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User job type interest deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserJobTypeInterestsByUser(@PathParam("userId") Long userId) {
        List<UserJobTypeInterest> interests = userJobTypeInterestService.findByUserId(userId);
        return Response.ok(ApiResponse.success(interests)).build();
    }
}

