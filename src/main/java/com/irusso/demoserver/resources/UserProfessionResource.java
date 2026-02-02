package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserProfession;
import com.irusso.demoserver.service.UserProfessionService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserProfession operations.
 */
@Path("/api/user-professions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserProfessionResource {

    private final UserProfessionService userProfessionService;

    @Inject
    public UserProfessionResource(UserProfessionService userProfessionService) {
        this.userProfessionService = userProfessionService;
    }

    @GET
    public Response getAllUserProfessions() {
        List<UserProfession> userProfessions = userProfessionService.getAllUserProfessions();
        return Response.ok(ApiResponse.success(userProfessions)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserProfession(@PathParam("id") Long id) {
        Optional<UserProfession> userProfession = userProfessionService.getUserProfessionById(id);
        if (userProfession.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User profession not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(userProfession.get())).build();
    }

    @POST
    public Response createUserProfession(@Valid UserProfession userProfession) {
        Long id = userProfessionService.createUserProfession(userProfession);
        userProfession.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User profession created successfully", userProfession))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserProfession(@PathParam("id") Long id, @Valid UserProfession userProfession) {
        if (userProfessionService.getUserProfessionById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User profession not found"))
                    .build();
        }
        userProfession.setId(id);
        userProfessionService.updateUserProfession(userProfession);
        return Response.ok(ApiResponse.success("User profession updated successfully", userProfession)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserProfession(@PathParam("id") Long id) {
        boolean deleted = userProfessionService.deleteUserProfession(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User profession not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User profession deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserProfessionsByUser(@PathParam("userId") Long userId) {
        List<UserProfession> userProfessions = userProfessionService.findByUserId(userId);
        return Response.ok(ApiResponse.success(userProfessions)).build();
    }

    @GET
    @Path("/user/{userId}/current")
    public Response getCurrentUserProfessionsByUser(@PathParam("userId") Long userId) {
        List<UserProfession> userProfessions = userProfessionService.findCurrentByUserId(userId);
        return Response.ok(ApiResponse.success(userProfessions)).build();
    }

    @GET
    @Path("/profession/{professionId}")
    public Response getUsersByProfession(@PathParam("professionId") Long professionId) {
        List<UserProfession> userProfessions = userProfessionService.findByProfessionId(professionId);
        return Response.ok(ApiResponse.success(userProfessions)).build();
    }
}

