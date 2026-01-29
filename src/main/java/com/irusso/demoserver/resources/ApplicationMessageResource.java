package com.irusso.demoserver.resources;

import com.google.inject.Inject;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.ApplicationMessage;
import com.irusso.demoserver.service.ApplicationMessageService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

/**
 * REST resource for ApplicationMessage operations.
 */
@Path("/api/application-messages")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ApplicationMessageResource {

    private final ApplicationMessageService applicationMessageService;

    @Inject
    public ApplicationMessageResource(ApplicationMessageService applicationMessageService) {
        this.applicationMessageService = applicationMessageService;
    }

    @GET
    public Response getAllApplicationMessages() {
        List<ApplicationMessage> messages = applicationMessageService.getAllApplicationMessages();
        return Response.ok(ApiResponse.success(messages)).build();
    }

    @GET
    @Path("/{id}")
    public Response getApplicationMessage(@PathParam("id") Long id) {
        Optional<ApplicationMessage> message = applicationMessageService.getApplicationMessageById(id);
        if (message.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application message not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success(message.get())).build();
    }

    @POST
    public Response createApplicationMessage(@Valid ApplicationMessage message) {
        Long id = applicationMessageService.createApplicationMessage(message);
        message.setId(id);
        return Response.status(Response.Status.CREATED)
                .entity(ApiResponse.success("Application message created successfully", message))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateApplicationMessage(@PathParam("id") Long id, @Valid ApplicationMessage message) {
        if (applicationMessageService.getApplicationMessageById(id).isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application message not found"))
                    .build();
        }
        message.setId(id);
        applicationMessageService.updateApplicationMessage(message);
        return Response.ok(ApiResponse.success("Application message updated successfully", message)).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteApplicationMessage(@PathParam("id") Long id) {
        boolean deleted = applicationMessageService.deleteApplicationMessage(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ApiResponse.error("Application message not found"))
                    .build();
        }
        return Response.ok(ApiResponse.success("Application message deleted successfully", null)).build();
    }

    @GET
    @Path("/application/{applicationId}")
    public Response getApplicationMessagesByApplication(@PathParam("applicationId") Long applicationId) {
        List<ApplicationMessage> messages = applicationMessageService.findByApplicationId(applicationId);
        return Response.ok(ApiResponse.success(messages)).build();
    }
}

