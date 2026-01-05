package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.api.User;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * REST resource for User operations.
 * Provides CRUD endpoints for managing users.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public UserResource() {
        // Initialize with some sample data
        createUser(new User(null, "John Doe", "john.doe@example.com", "Admin"));
        createUser(new User(null, "Jane Smith", "jane.smith@example.com", "User"));
        createUser(new User(null, "Bob Johnson", "bob.johnson@example.com", "User"));
    }

    /**
     * Get all users.
     * GET /api/users
     */
    @GET
    public Response getAllUsers() {
        List<User> userList = new ArrayList<>(users.values());
        return Response.ok(ApiResponse.success(userList)).build();
    }

    /**
     * Get a specific user by ID.
     * GET /api/users/{id}
     */
    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") Long id) {
        User user = users.get(id);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(user)).build();
    }

    /**
     * Create a new user.
     * POST /api/users
     */
    @POST
    public Response createUser(@Valid User user) {
        Long id = idCounter.incrementAndGet();
        user.setId(id);
        users.put(id, user);
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
        if (!users.containsKey(id)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        user.setId(id);
        users.put(id, user);
        return Response.ok(ApiResponse.success("User updated successfully", user)).build();
    }

    /**
     * Delete a user.
     * DELETE /api/users/{id}
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUser(@PathParam("id") Long id) {
        User removed = users.remove(id);
        if (removed == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User deleted successfully", null)).build();
    }
}

