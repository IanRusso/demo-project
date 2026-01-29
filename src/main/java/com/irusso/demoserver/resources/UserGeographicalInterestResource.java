package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserGeographicalInterest;
import com.irusso.demoserver.service.UserGeographicalInterestService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserGeographicalInterest operations.
 */
@Path("/api/user-geographical-interests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserGeographicalInterestResource {

    private final UserGeographicalInterestService userGeographicalInterestService;

    @Inject
    public UserGeographicalInterestResource(UserGeographicalInterestService userGeographicalInterestService) {
        this.userGeographicalInterestService = userGeographicalInterestService;
    }

    @GET
    public Response getAllUserGeographicalInterests() {
        List<UserGeographicalInterest> interests = userGeographicalInterestService.getAllUserGeographicalInterests();
        return Response.ok(ApiResponse.success(interests)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserGeographicalInterest(@PathParam("id") Long id) {
        Optional<UserGeographicalInterest> interest = userGeographicalInterestService.getUserGeographicalInterestById(id);
        if (interest.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User geographical interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(interest.get())).build();
    }

    @POST
    public Response createUserGeographicalInterest(@Valid UserGeographicalInterest interest) {
        Long id = userGeographicalInterestService.createUserGeographicalInterest(interest);
        interest.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User geographical interest created successfully", interest))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserGeographicalInterest(@PathParam("id") Long id, @Valid UserGeographicalInterest interest) {
        if (userGeographicalInterestService.getUserGeographicalInterestById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User geographical interest not found"))
                    .build();
        }
        interest.setId(id);
        userGeographicalInterestService.updateUserGeographicalInterest(interest);
        return Response.ok(ApiResponse.success("User geographical interest updated successfully", interest)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserGeographicalInterest(@PathParam("id") Long id) {
        boolean deleted = userGeographicalInterestService.deleteUserGeographicalInterest(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User geographical interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User geographical interest deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserGeographicalInterestsByUser(@PathParam("userId") Long userId) {
        List<UserGeographicalInterest> interests = userGeographicalInterestService.findByUserId(userId);
        return Response.ok(ApiResponse.success(interests)).build();
    }
}

