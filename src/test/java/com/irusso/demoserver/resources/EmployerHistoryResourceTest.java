package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.EmployerHistory;
import com.irusso.demoserver.service.EmployerHistoryService;
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
class EmployerHistoryResourceTest {

    @Mock
    private EmployerHistoryService employerHistoryService;

    private EmployerHistoryResource employerHistoryResource;

    @BeforeEach
    void setUp() {
        employerHistoryResource = new EmployerHistoryResource(employerHistoryService);
    }

    @Test
    void testGetAllEmployerHistory() {
        // Arrange
        EmployerHistory hist1 = new EmployerHistory();
        hist1.setId(1L);
        hist1.setEmployerId(100L);

        EmployerHistory hist2 = new EmployerHistory();
        hist2.setId(2L);
        hist2.setEmployerId(200L);

        List<EmployerHistory> histories = Arrays.asList(hist1, hist2);
        when(employerHistoryService.getAllEmployerHistory()).thenReturn(histories);

        // Act
        Response response = employerHistoryResource.getAllEmployerHistory();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<EmployerHistory>> apiResponse = (ApiResponse<List<EmployerHistory>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(employerHistoryService, times(1)).getAllEmployerHistory();
    }

    @Test
    void testGetEmployerHistory_Found() {
        // Arrange
        Long histId = 1L;
        EmployerHistory history = new EmployerHistory();
        history.setId(histId);
        history.setEmployerId(100L);

        when(employerHistoryService.getEmployerHistoryById(histId)).thenReturn(Optional.of(history));

        // Act
        Response response = employerHistoryResource.getEmployerHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<EmployerHistory> apiResponse = (ApiResponse<EmployerHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(histId);
        verify(employerHistoryService, times(1)).getEmployerHistoryById(histId);
    }

    @Test
    void testGetEmployerHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        when(employerHistoryService.getEmployerHistoryById(histId)).thenReturn(Optional.empty());

        // Act
        Response response = employerHistoryResource.getEmployerHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<EmployerHistory> apiResponse = (ApiResponse<EmployerHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history not found");
        verify(employerHistoryService, times(1)).getEmployerHistoryById(histId);
    }

    @Test
    void testCreateEmployerHistory() {
        // Arrange
        EmployerHistory newHistory = new EmployerHistory();
        newHistory.setEmployerId(100L);
        newHistory.setEventType("hiring");

        Long generatedId = 123L;
        when(employerHistoryService.createEmployerHistory(newHistory)).thenReturn(generatedId);

        // Act
        Response response = employerHistoryResource.createEmployerHistory(newHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<EmployerHistory> apiResponse = (ApiResponse<EmployerHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(employerHistoryService, times(1)).createEmployerHistory(newHistory);
    }

    @Test
    void testUpdateEmployerHistory_Success() {
        // Arrange
        Long histId = 1L;
        EmployerHistory existingHistory = new EmployerHistory();
        existingHistory.setId(histId);
        existingHistory.setEmployerId(100L);

        EmployerHistory updatedHistory = new EmployerHistory();
        updatedHistory.setEventType("layoff");

        when(employerHistoryService.getEmployerHistoryById(histId)).thenReturn(Optional.of(existingHistory));
        when(employerHistoryService.updateEmployerHistory(any(EmployerHistory.class))).thenReturn(true);

        // Act
        Response response = employerHistoryResource.updateEmployerHistory(histId, updatedHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<EmployerHistory> apiResponse = (ApiResponse<EmployerHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(histId);
        verify(employerHistoryService, times(1)).getEmployerHistoryById(histId);
        verify(employerHistoryService, times(1)).updateEmployerHistory(any(EmployerHistory.class));
    }

    @Test
    void testUpdateEmployerHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        EmployerHistory updatedHistory = new EmployerHistory();
        updatedHistory.setEventType("layoff");

        when(employerHistoryService.getEmployerHistoryById(histId)).thenReturn(Optional.empty());

        // Act
        Response response = employerHistoryResource.updateEmployerHistory(histId, updatedHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<EmployerHistory> apiResponse = (ApiResponse<EmployerHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history not found");
        verify(employerHistoryService, times(1)).getEmployerHistoryById(histId);
        verify(employerHistoryService, never()).updateEmployerHistory(any(EmployerHistory.class));
    }

    @Test
    void testDeleteEmployerHistory_Success() {
        // Arrange
        Long histId = 1L;
        when(employerHistoryService.deleteEmployerHistory(histId)).thenReturn(true);

        // Act
        Response response = employerHistoryResource.deleteEmployerHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history deleted successfully");
        verify(employerHistoryService, times(1)).deleteEmployerHistory(histId);
    }

    @Test
    void testDeleteEmployerHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        when(employerHistoryService.deleteEmployerHistory(histId)).thenReturn(false);

        // Act
        Response response = employerHistoryResource.deleteEmployerHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer history not found");
        verify(employerHistoryService, times(1)).deleteEmployerHistory(histId);
    }
}

