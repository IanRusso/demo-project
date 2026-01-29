package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserFieldOfInterest;
import com.irusso.demoserver.service.UserFieldOfInterestService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserFieldOfInterest operations.
 */
@Path("/api/user-fields-of-interest")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserFieldOfInterestResource {

    private final UserFieldOfInterestService userFieldOfInterestService;

    @Inject
    public UserFieldOfInterestResource(UserFieldOfInterestService userFieldOfInterestService) {
        this.userFieldOfInterestService = userFieldOfInterestService;
    }

    @GET
    public Response getAllUserFieldsOfInterest() {
        List<UserFieldOfInterest> interests = userFieldOfInterestService.getAllUserFieldsOfInterest();
        return Response.ok(ApiResponse.success(interests)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserFieldOfInterest(@PathParam("id") Long id) {
        Optional<UserFieldOfInterest> interest = userFieldOfInterestService.getUserFieldOfInterestById(id);
        if (interest.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User field of interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(interest.get())).build();
    }

    @POST
    public Response createUserFieldOfInterest(@Valid UserFieldOfInterest interest) {
        Long id = userFieldOfInterestService.createUserFieldOfInterest(interest);
        interest.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User field of interest created successfully", interest))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserFieldOfInterest(@PathParam("id") Long id, @Valid UserFieldOfInterest interest) {
        if (userFieldOfInterestService.getUserFieldOfInterestById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User field of interest not found"))
                    .build();
        }
        interest.setId(id);
        userFieldOfInterestService.updateUserFieldOfInterest(interest);
        return Response.ok(ApiResponse.success("User field of interest updated successfully", interest)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserFieldOfInterest(@PathParam("id") Long id) {
        boolean deleted = userFieldOfInterestService.deleteUserFieldOfInterest(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User field of interest not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User field of interest deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserFieldsOfInterestByUser(@PathParam("userId") Long userId) {
        List<UserFieldOfInterest> interests = userFieldOfInterestService.findByUserId(userId);
        return Response.ok(ApiResponse.success(interests)).build();
    }
}

