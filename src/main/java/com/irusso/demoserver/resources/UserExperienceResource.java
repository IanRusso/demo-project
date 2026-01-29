package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserExperience;
import com.irusso.demoserver.service.UserExperienceService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserExperience operations.
 * Provides CRUD endpoints for managing user experiences and achievements.
 */
@Path("/api/user-experiences")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserExperienceResource {

    private final UserExperienceService userExperienceService;

    @Inject
    public UserExperienceResource(UserExperienceService userExperienceService) {
        this.userExperienceService = userExperienceService;
    }

    @GET
    public Response getAllUserExperiences() {
        List<UserExperience> experiences = userExperienceService.getAllUserExperiences();
        return Response.ok(ApiResponse.success(experiences)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserExperience(@PathParam("id") Long id) {
        Optional<UserExperience> experience = userExperienceService.getUserExperienceById(id);
        if (experience.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User experience not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(experience.get())).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserExperiencesByUserId(@PathParam("userId") Long userId) {
        List<UserExperience> experiences = userExperienceService.getUserExperiencesByUserId(userId);
        return Response.ok(ApiResponse.success(experiences)).build();
    }

    @GET
    @Path("/user/{userId}/type/{experienceType}")
    public Response getUserExperiencesByUserIdAndType(
            @PathParam("userId") Long userId,
            @PathParam("experienceType") String experienceType) {
        List<UserExperience> experiences = userExperienceService.getUserExperiencesByUserIdAndType(userId, experienceType);
        return Response.ok(ApiResponse.success(experiences)).build();
    }

    @GET
    @Path("/user/{userId}/current")
    public Response getCurrentUserExperiencesByUserId(@PathParam("userId") Long userId) {
        List<UserExperience> experiences = userExperienceService.getCurrentUserExperiencesByUserId(userId);
        return Response.ok(ApiResponse.success(experiences)).build();
    }

    @POST
    public Response createUserExperience(@Valid UserExperience experience) {
        Long id = userExperienceService.createUserExperience(experience);
        experience.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User experience created successfully", experience))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserExperience(@PathParam("id") Long id, @Valid UserExperience experience) {
        boolean updated = userExperienceService.updateUserExperience(id, experience);
        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User experience not found"))
                    .build();
        }
        experience.setId(id);
        return Response.ok(ApiResponse.success("User experience updated successfully", experience)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserExperience(@PathParam("id") Long id) {
        boolean deleted = userExperienceService.deleteUserExperience(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User experience not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User experience deleted successfully", null)).build();
    }
}

