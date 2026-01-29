package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.api.ChangePasswordRequest;
import com.irusso.demoserver.api.LoginRequest;
import com.irusso.demoserver.api.RegisterRequest;
import com.irusso.demoserver.db.model.User;
import com.irusso.demoserver.service.UserService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

/**
 * REST resource for authentication operations.
 * Provides endpoints for user registration, login, and password management.
 */
@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final UserService userService;

    @Inject
    public AuthResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user.
     * POST /api/auth/register
     */
    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        try {
            // Create user object
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());

            // Register user with password
            Long userId = userService.registerUser(user, request.getPassword());
            user.setId(userId);

            return Response.status(Response.Status.CREATED)
                    .entity(ApiResponse.success("User registered successfully", user))
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }

    /**
     * Login a user.
     * POST /api/auth/login
     */
    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        Optional<User> userOpt = userService.authenticateUser(request.getEmail(), request.getPassword());

        if (userOpt.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(ApiResponse.error("Invalid email or password"))
                    .build();
        }

        return Response.ok(ApiResponse.success("Login successful", userOpt.get())).build();
    }

    /**
     * Change password for a user.
     * POST /api/auth/change-password/{userId}
     */
    @POST
    @Path("/change-password/{userId}")
    public Response changePassword(
            @PathParam("userId") Long userId,
            @Valid ChangePasswordRequest request) {
        try {
            boolean success = userService.changePassword(
                    userId,
                    request.getCurrentPassword(),
                    request.getNewPassword()
            );

            if (!success) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(ApiResponse.error("Current password is incorrect or user not found"))
                        .build();
            }

            return Response.ok(ApiResponse.success("Password changed successfully", null)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }

    /**
     * Set/reset password for a user (admin function).
     * POST /api/auth/set-password/{userId}
     */
    @POST
    @Path("/set-password/{userId}")
    public Response setPassword(
            @PathParam("userId") Long userId,
            @QueryParam("password") String password) {
        if (password == null || password.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error("Password is required"))
                    .build();
        }

        try {
            boolean success = userService.setPassword(userId, password);

            if (!success) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity(ApiResponse.error("User not found"))
                        .build();
            }

            return Response.ok(ApiResponse.success("Password set successfully", null)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ApiResponse.error(e.getMessage()))
                    .build();
        }
    }
}

