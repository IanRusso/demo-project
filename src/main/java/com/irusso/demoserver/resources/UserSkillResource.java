package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserSkill;
import com.irusso.demoserver.service.UserSkillService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for UserSkill operations.
 */
@Path("/api/user-skills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserSkillResource {

    private final UserSkillService userSkillService;

    @Inject
    public UserSkillResource(UserSkillService userSkillService) {
        this.userSkillService = userSkillService;
    }

    @GET
    public Response getAllUserSkills() {
        List<UserSkill> userSkills = userSkillService.getAllUserSkills();
        return Response.ok(ApiResponse.success(userSkills)).build();
    }

    @GET
    @Path("/{id}")
    public Response getUserSkill(@PathParam("id") Long id) {
        Optional<UserSkill> userSkill = userSkillService.getUserSkillById(id);
        if (userSkill.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User skill not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(userSkill.get())).build();
    }

    @POST
    public Response createUserSkill(@Valid UserSkill userSkill) {
        Long id = userSkillService.createUserSkill(userSkill);
        userSkill.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("User skill created successfully", userSkill))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUserSkill(@PathParam("id") Long id, @Valid UserSkill userSkill) {
        if (userSkillService.getUserSkillById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User skill not found"))
                    .build();
        }
        userSkill.setId(id);
        userSkillService.updateUserSkill(userSkill);
        return Response.ok(ApiResponse.success("User skill updated successfully", userSkill)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteUserSkill(@PathParam("id") Long id) {
        boolean deleted = userSkillService.deleteUserSkill(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("User skill not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("User skill deleted successfully", null)).build();
    }

    @GET
    @Path("/user/{userId}")
    public Response getUserSkillsByUser(@PathParam("userId") Long userId) {
        List<UserSkill> userSkills = userSkillService.findByUserId(userId);
        return Response.ok(ApiResponse.success(userSkills)).build();
    }
}

