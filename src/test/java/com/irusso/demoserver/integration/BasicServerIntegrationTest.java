package com.irusso.demoserver.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.application.GainfullyServerApplication;
import com.irusso.demoserver.application.model.GainfullyServerConfiguration;
import com.irusso.demoserver.db.model.Employer;
import com.irusso.demoserver.db.model.User;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Basic integration tests for the Gainfully Server.
 * These tests verify that the Dropwizard server starts correctly and basic CRUD operations work.
 */
@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BasicServerIntegrationTest {

    private static final DropwizardAppExtension<GainfullyServerConfiguration> APP =
            new DropwizardAppExtension<>(
                    GainfullyServerApplication.class,
                    ResourceHelpers.resourceFilePath("test-config.yml")
            );

    private static Client client;
    private static ObjectMapper objectMapper;
    private static String baseUrl;

    @BeforeAll
    static void setUpClass() {
        client = new JerseyClientBuilder().build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        baseUrl = String.format("http://localhost:%d", APP.getLocalPort());
    }

    @AfterAll
    static void tearDownClass() {
        if (client != null) {
            client.close();
        }
    }

    @Test
    @Order(1)
    void testServerIsRunning() {
        // Verify the server started successfully
        assertThat(APP.getLocalPort()).isGreaterThan(0);
        assertThat(APP.getAdminPort()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    void testHealthCheck() {
        // Test the health check endpoint
        Response response = client.target(String.format("http://localhost:%d/healthcheck", APP.getAdminPort()))
                .request()
                .get();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    @Order(3)
    void testUserCrudOperations() throws Exception {
        // Use a unique email to avoid conflicts
        String uniqueEmail = "user-" + UUID.randomUUID() + "@test.com";
        
        // CREATE - Test POST /api/users
        User newUser = new User();
        newUser.setName("Integration Test User");
        newUser.setEmail(uniqueEmail);

        Response createResponse = client.target(baseUrl + "/api/users")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(newUser));

        assertThat(createResponse.getStatus()).isEqualTo(201);

        String createJson = createResponse.readEntity(String.class);
        ApiResponse<User> createApiResponse = objectMapper.readValue(createJson, new TypeReference<ApiResponse<User>>() {});
        
        assertThat(createApiResponse.isSuccess()).isTrue();
        assertThat(createApiResponse.getData()).isNotNull();
        Long userId = createApiResponse.getData().getId();
        assertThat(userId).isNotNull();

        // READ - Test GET /api/users/{id}
        Response getResponse = client.target(baseUrl + "/api/users/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(200);

        String getJson = getResponse.readEntity(String.class);
        ApiResponse<User> getApiResponse = objectMapper.readValue(getJson, new TypeReference<ApiResponse<User>>() {});
        
        assertThat(getApiResponse.isSuccess()).isTrue();
        assertThat(getApiResponse.getData().getId()).isEqualTo(userId);
        assertThat(getApiResponse.getData().getEmail()).isEqualTo(uniqueEmail);

        // UPDATE - Test PUT /api/users/{id}
        User updatedUser = new User();
        updatedUser.setName("Updated Test User");
        updatedUser.setEmail(uniqueEmail);

        Response updateResponse = client.target(baseUrl + "/api/users/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updatedUser));

        assertThat(updateResponse.getStatus()).isEqualTo(200);

        String updateJson = updateResponse.readEntity(String.class);
        ApiResponse<User> updateApiResponse = objectMapper.readValue(updateJson, new TypeReference<ApiResponse<User>>() {});
        
        assertThat(updateApiResponse.isSuccess()).isTrue();
        assertThat(updateApiResponse.getData().getName()).isEqualTo("Updated Test User");

        // DELETE - Test DELETE /api/users/{id}
        Response deleteResponse = client.target(baseUrl + "/api/users/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertThat(deleteResponse.getStatus()).isEqualTo(200);

        // Verify deletion
        Response verifyResponse = client.target(baseUrl + "/api/users/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(verifyResponse.getStatus()).isEqualTo(404);
    }

    @Test
    @Order(4)
    void testEmployerCrudOperations() throws Exception {
        // Use a unique email to avoid conflicts
        String uniqueEmail = "employer-" + UUID.randomUUID() + "@test.com";
        
        // CREATE - Test POST /api/employers
        Employer newEmployer = new Employer();
        newEmployer.setName("Integration Test Company");
        newEmployer.setEmail(uniqueEmail);

        Response createResponse = client.target(baseUrl + "/api/employers")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(newEmployer));

        assertThat(createResponse.getStatus()).isEqualTo(201);

        String createJson = createResponse.readEntity(String.class);
        ApiResponse<Employer> createApiResponse = objectMapper.readValue(createJson, new TypeReference<ApiResponse<Employer>>() {});
        
        assertThat(createApiResponse.isSuccess()).isTrue();
        assertThat(createApiResponse.getData()).isNotNull();
        Long employerId = createApiResponse.getData().getId();
        assertThat(employerId).isNotNull();

        // READ - Test GET /api/employers/{id}
        Response getResponse = client.target(baseUrl + "/api/employers/" + employerId)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(getResponse.getStatus()).isEqualTo(200);

        String getJson = getResponse.readEntity(String.class);
        ApiResponse<Employer> getApiResponse = objectMapper.readValue(getJson, new TypeReference<ApiResponse<Employer>>() {});
        
        assertThat(getApiResponse.isSuccess()).isTrue();
        assertThat(getApiResponse.getData().getId()).isEqualTo(employerId);
        assertThat(getApiResponse.getData().getEmail()).isEqualTo(uniqueEmail);

        // LIST - Test GET /api/employers
        Response listResponse = client.target(baseUrl + "/api/employers")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(listResponse.getStatus()).isEqualTo(200);

        String listJson = listResponse.readEntity(String.class);
        ApiResponse<List<Employer>> listApiResponse = objectMapper.readValue(listJson, new TypeReference<ApiResponse<List<Employer>>>() {});
        
        assertThat(listApiResponse.isSuccess()).isTrue();
        assertThat(listApiResponse.getData()).isNotEmpty();
    }

    @Test
    @Order(5)
    void testGetNonExistentResource() {
        // Test GET /api/users/{id} with non-existent ID
        Response response = client.target(baseUrl + "/api/users/999999")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus()).isEqualTo(404);
    }
}

