package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.User;
import com.irusso.demoserver.service.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for User operations.
 * Provides CRUD endpoints for managing users.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     * GET /api/users
     */
    @GET
    public Response getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Response.ok(ApiResponse.success(users)).build();
    }

    /**
     * Get a specific user by ID.
     * GET /api/users/{id}
     */
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        Optional<User> user = userService.getUserById(id);
        if (user.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(user.get())).build();
    }

    /**
     * Create a new user.
     * POST /api/users
     */
    @POST
    public Response createUser(@Valid User user) {
        Long id = userService.createUser(user);
        user.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User created successfully", user))
                .build();
    }

    /**
     * Update an existing user.
     * PUT /api/users/{id}
     */
    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, @Valid User user) {
        if (userService.getUserById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        user.setId(id);
        userService.updateUser(user);
        return Response.ok(ApiResponse.success("User updated successfully", user)).build();
    }

    /**
     * Delete a user.
     * DELETE /api/users/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        boolean deleted = userService.deleteUser(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User deleted successfully", null)).build();
    }
}

