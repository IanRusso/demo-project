package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserConnection;
import com.irusso.demoserver.service.UserConnectionService;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

/**
 * REST resource for user connections.
 */
@Path("/api/user-connections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserConnectionResource {
    
    private final UserConnectionService userConnectionService;
    
    @Inject
    public UserConnectionResource(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }
    
    /**
     * Get all user connections.
     */
    @GET
    public Response getAllUserConnections() {
        List<UserConnection> connections = userConnectionService.getAllUserConnections();
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get a user connection by ID.
     */
    @GET
    @Path("/{id}")
    public Response getUserConnectionById(@PathParam("id") Long id) {
        Optional<UserConnection> connection = userConnectionService.getUserConnectionById(id);
        if (connection.isPresent()) {
            return Response.ok(ApiResponse.success(connection.get())).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.error("User connection not found"))
                .build();
        }
    }
    
    /**
     * Create a new user connection.
     */
    @POST
    public Response createUserConnection(UserConnection userConnection) {
        Long id = userConnectionService.createUserConnection(userConnection);
        userConnection.setId(id);
        return Response.status(Response.Status.CREATED)
            .entity(ApiResponse.success("User connection created successfully", userConnection))
            .build();
    }
    
    /**
     * Update an existing user connection.
     */
    @PUT
    @Path("/{id}")
    public Response updateUserConnection(@PathParam("id") Long id, UserConnection userConnection) {
        boolean updated = userConnectionService.updateUserConnection(id, userConnection);
        if (updated) {
            userConnection.setId(id);
            return Response.ok(ApiResponse.success("User connection updated successfully", userConnection)).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.error("User connection not found"))
                .build();
        }
    }
    
    /**
     * Delete a user connection.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteUserConnection(@PathParam("id") Long id) {
        boolean deleted = userConnectionService.deleteUserConnection(id);
        if (deleted) {
            return Response.ok(ApiResponse.success("User connection deleted successfully")).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(ApiResponse.error("User connection not found"))
                .build();
        }
    }
    
    /**
     * Get all connections for a specific user.
     */
    @GET
    @Path("/user/{userId}")
    public Response getConnectionsByUserId(@PathParam("userId") Long userId) {
        List<UserConnection> connections = userConnectionService.findByUserId(userId);
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get all accepted connections for a specific user.
     */
    @GET
    @Path("/user/{userId}/accepted")
    public Response getAcceptedConnectionsByUserId(@PathParam("userId") Long userId) {
        List<UserConnection> connections = userConnectionService.findAcceptedByUserId(userId);
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get all pending connections for a specific user.
     */
    @GET
    @Path("/user/{userId}/pending")
    public Response getPendingConnectionsByUserId(@PathParam("userId") Long userId) {
        List<UserConnection> connections = userConnectionService.findPendingByUserId(userId);
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get pending connection requests received by a user.
     */
    @GET
    @Path("/user/{userId}/pending/received")
    public Response getPendingReceivedByUserId(@PathParam("userId") Long userId) {
        List<UserConnection> connections = userConnectionService.findPendingReceivedByUserId(userId);
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get pending connection requests sent by a user.
     */
    @GET
    @Path("/user/{userId}/pending/sent")
    public Response getPendingSentByUserId(@PathParam("userId") Long userId) {
        List<UserConnection> connections = userConnectionService.findPendingSentByUserId(userId);
        return Response.ok(ApiResponse.success(connections)).build();
    }
    
    /**
     * Get connection between two specific users.
     */
    @GET
    @Path("/between/{userId1}/{userId2}")
    public Response getConnectionBetweenUsers(
            @PathParam("userId1") Long userId1,
            @PathParam("userId2") Long userId2) {
        List<UserConnection> connections = userConnectionService.findConnectionBetweenUsers(userId1, userId2);
        return Response.ok(ApiResponse.success(connections)).build();
    }
}

