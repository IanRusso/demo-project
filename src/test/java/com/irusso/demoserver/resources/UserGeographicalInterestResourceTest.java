package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.UserGeographicalInterest;
import com.irusso.demoserver.service.UserGeographicalInterestService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserGeographicalInterestResourceTest {

    @Mock
    private UserGeographicalInterestService userGeographicalInterestService;

    private UserGeographicalInterestResource userGeographicalInterestResource;

    @BeforeEach
    void setUp() {
        userGeographicalInterestResource = new UserGeographicalInterestResource(userGeographicalInterestService);
    }

    @Test
    void testGetAllUserGeographicalInterests() {
        // Arrange
        UserGeographicalInterest geo1 = new UserGeographicalInterest();
        geo1.setId(1L);
        geo1.setUserId(100L);

        UserGeographicalInterest geo2 = new UserGeographicalInterest();
        geo2.setId(2L);
        geo2.setUserId(200L);

        List<UserGeographicalInterest> geos = Arrays.asList(geo1, geo2);
        when(userGeographicalInterestService.getAllUserGeographicalInterests()).thenReturn(geos);

        // Act
        Response response = userGeographicalInterestResource.getAllUserGeographicalInterests();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<UserGeographicalInterest>> apiResponse = (ApiResponse<List<UserGeographicalInterest>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(userGeographicalInterestService, times(1)).getAllUserGeographicalInterests();
    }

    @Test
    void testGetUserGeographicalInterest_Found() {
        // Arrange
        Long geoId = 1L;
        UserGeographicalInterest geo = new UserGeographicalInterest();
        geo.setId(geoId);
        geo.setUserId(100L);

        when(userGeographicalInterestService.getUserGeographicalInterestById(geoId)).thenReturn(Optional.of(geo));

        // Act
        Response response = userGeographicalInterestResource.getUserGeographicalInterest(geoId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserGeographicalInterest> apiResponse = (ApiResponse<UserGeographicalInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(geoId);
        verify(userGeographicalInterestService, times(1)).getUserGeographicalInterestById(geoId);
    }

    @Test
    void testGetUserGeographicalInterest_NotFound() {
        // Arrange
        Long geoId = 999L;
        when(userGeographicalInterestService.getUserGeographicalInterestById(geoId)).thenReturn(Optional.empty());

        // Act
        Response response = userGeographicalInterestResource.getUserGeographicalInterest(geoId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserGeographicalInterest> apiResponse = (ApiResponse<UserGeographicalInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest not found");
        verify(userGeographicalInterestService, times(1)).getUserGeographicalInterestById(geoId);
    }

    @Test
    void testCreateUserGeographicalInterest() {
        // Arrange
        UserGeographicalInterest newGeo = new UserGeographicalInterest();
        newGeo.setUserId(100L);
        newGeo.setLocation("San Francisco");

        Long generatedId = 123L;
        when(userGeographicalInterestService.createUserGeographicalInterest(newGeo)).thenReturn(generatedId);

        // Act
        Response response = userGeographicalInterestResource.createUserGeographicalInterest(newGeo);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<UserGeographicalInterest> apiResponse = (ApiResponse<UserGeographicalInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(userGeographicalInterestService, times(1)).createUserGeographicalInterest(newGeo);
    }

    @Test
    void testUpdateUserGeographicalInterest_Success() {
        // Arrange
        Long geoId = 1L;
        UserGeographicalInterest existingGeo = new UserGeographicalInterest();
        existingGeo.setId(geoId);
        existingGeo.setUserId(100L);

        UserGeographicalInterest updatedGeo = new UserGeographicalInterest();
        updatedGeo.setLocation("New York");

        when(userGeographicalInterestService.getUserGeographicalInterestById(geoId)).thenReturn(Optional.of(existingGeo));
        when(userGeographicalInterestService.updateUserGeographicalInterest(any(UserGeographicalInterest.class))).thenReturn(true);

        // Act
        Response response = userGeographicalInterestResource.updateUserGeographicalInterest(geoId, updatedGeo);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<UserGeographicalInterest> apiResponse = (ApiResponse<UserGeographicalInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(geoId);
        verify(userGeographicalInterestService, times(1)).getUserGeographicalInterestById(geoId);
        verify(userGeographicalInterestService, times(1)).updateUserGeographicalInterest(any(UserGeographicalInterest.class));
    }

    @Test
    void testUpdateUserGeographicalInterest_NotFound() {
        // Arrange
        Long geoId = 999L;
        UserGeographicalInterest updatedGeo = new UserGeographicalInterest();
        updatedGeo.setLocation("New York");

        when(userGeographicalInterestService.getUserGeographicalInterestById(geoId)).thenReturn(Optional.empty());

        // Act
        Response response = userGeographicalInterestResource.updateUserGeographicalInterest(geoId, updatedGeo);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<UserGeographicalInterest> apiResponse = (ApiResponse<UserGeographicalInterest>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest not found");
        verify(userGeographicalInterestService, times(1)).getUserGeographicalInterestById(geoId);
        verify(userGeographicalInterestService, never()).updateUserGeographicalInterest(any(UserGeographicalInterest.class));
    }

    @Test
    void testDeleteUserGeographicalInterest_Success() {
        // Arrange
        Long geoId = 1L;
        when(userGeographicalInterestService.deleteUserGeographicalInterest(geoId)).thenReturn(true);

        // Act
        Response response = userGeographicalInterestResource.deleteUserGeographicalInterest(geoId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest deleted successfully");
        verify(userGeographicalInterestService, times(1)).deleteUserGeographicalInterest(geoId);
    }

    @Test
    void testDeleteUserGeographicalInterest_NotFound() {
        // Arrange
        Long geoId = 999L;
        when(userGeographicalInterestService.deleteUserGeographicalInterest(geoId)).thenReturn(false);

        // Act
        Response response = userGeographicalInterestResource.deleteUserGeographicalInterest(geoId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("User geographical interest not found");
        verify(userGeographicalInterestService, times(1)).deleteUserGeographicalInterest(geoId);
    }
}

