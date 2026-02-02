package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserIndustry;
import com.irusso.demoserver.service.UserIndustryService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserIndustry operations.
 */
@Path("/api/user-industries")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserIndustryResource {

    private final UserIndustryService userIndustryService;

    @Inject
    public UserIndustryResource(UserIndustryService userIndustryService) {
        this.userIndustryService = userIndustryService;
    }

    @GET
    public Response getAllUserIndustries() {
        List<UserIndustry> userIndustries = userIndustryService.getAllUserIndustries();
        return Response.ok(ApiResponse.success(userIndustries)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserIndustry(@PathParam("id") Long id) {
        Optional<UserIndustry> userIndustry = userIndustryService.getUserIndustryById(id);
        if (userIndustry.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User industry not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(userIndustry.get())).build();
    }

    @POST
    public Response createUserIndustry(@Valid UserIndustry userIndustry) {
        Long id = userIndustryService.createUserIndustry(userIndustry);
        userIndustry.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User industry created successfully", userIndustry))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserIndustry(@PathParam("id") Long id, @Valid UserIndustry userIndustry) {
        if (userIndustryService.getUserIndustryById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User industry not found"))
                    .build();
        }
        userIndustry.setId(id);
        userIndustryService.updateUserIndustry(userIndustry);
        return Response.ok(ApiResponse.success("User industry updated successfully", userIndustry)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserIndustry(@PathParam("id") Long id) {
        boolean deleted = userIndustryService.deleteUserIndustry(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User industry not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User industry deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserIndustriesByUser(@PathParam("userId") Long userId) {
        List<UserIndustry> userIndustries = userIndustryService.findByUserId(userId);
        return Response.ok(ApiResponse.success(userIndustries)).build();
    }

    @GET
    @Path("/user/{userId}/current")
    public Response getCurrentUserIndustriesByUser(@PathParam("userId") Long userId) {
        List<UserIndustry> userIndustries = userIndustryService.findCurrentByUserId(userId);
        return Response.ok(ApiResponse.success(userIndustries)).build();
    }

    @GET
    @Path("/industry/{industryId}")
    public Response getUsersByIndustry(@PathParam("industryId") Long industryId) {
        List<UserIndustry> userIndustries = userIndustryService.findByIndustryId(industryId);
        return Response.ok(ApiResponse.success(userIndustries)).build();
    }
}

