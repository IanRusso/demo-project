package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.EmploymentHistory;
import com.irusso.demoserver.service.EmploymentHistoryService;
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
class EmploymentHistoryResourceTest {

    @Mock
    private EmploymentHistoryService employmentHistoryService;

    private EmploymentHistoryResource employmentHistoryResource;

    @BeforeEach
    void setUp() {
        employmentHistoryResource = new EmploymentHistoryResource(employmentHistoryService);
    }

    @Test
    void testGetAllEmploymentHistory() {
        // Arrange
        EmploymentHistory hist1 = new EmploymentHistory();
        hist1.setId(1L);
        hist1.setUserId(100L);

        EmploymentHistory hist2 = new EmploymentHistory();
        hist2.setId(2L);
        hist2.setUserId(200L);

        List<EmploymentHistory> histories = Arrays.asList(hist1, hist2);
        when(employmentHistoryService.getAllEmploymentHistory()).thenReturn(histories);

        // Act
        Response response = employmentHistoryResource.getAllEmploymentHistory();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<EmploymentHistory>> apiResponse = (ApiResponse<List<EmploymentHistory>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(employmentHistoryService, times(1)).getAllEmploymentHistory();
    }

    @Test
    void testGetEmploymentHistory_Found() {
        // Arrange
        Long histId = 1L;
        EmploymentHistory history = new EmploymentHistory();
        history.setId(histId);
        history.setUserId(100L);

        when(employmentHistoryService.getEmploymentHistoryById(histId)).thenReturn(Optional.of(history));

        // Act
        Response response = employmentHistoryResource.getEmploymentHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<EmploymentHistory> apiResponse = (ApiResponse<EmploymentHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(histId);
        verify(employmentHistoryService, times(1)).getEmploymentHistoryById(histId);
    }

    @Test
    void testGetEmploymentHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        when(employmentHistoryService.getEmploymentHistoryById(histId)).thenReturn(Optional.empty());

        // Act
        Response response = employmentHistoryResource.getEmploymentHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<EmploymentHistory> apiResponse = (ApiResponse<EmploymentHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history not found");
        verify(employmentHistoryService, times(1)).getEmploymentHistoryById(histId);
    }

    @Test
    void testCreateEmploymentHistory() {
        // Arrange
        EmploymentHistory newHistory = new EmploymentHistory();
        newHistory.setUserId(100L);
        newHistory.setJobTitle("Engineer");

        Long generatedId = 123L;
        when(employmentHistoryService.createEmploymentHistory(newHistory)).thenReturn(generatedId);

        // Act
        Response response = employmentHistoryResource.createEmploymentHistory(newHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<EmploymentHistory> apiResponse = (ApiResponse<EmploymentHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(employmentHistoryService, times(1)).createEmploymentHistory(newHistory);
    }

    @Test
    void testUpdateEmploymentHistory_Success() {
        // Arrange
        Long histId = 1L;
        EmploymentHistory existingHistory = new EmploymentHistory();
        existingHistory.setId(histId);
        existingHistory.setUserId(100L);

        EmploymentHistory updatedHistory = new EmploymentHistory();
        updatedHistory.setJobTitle("Senior Engineer");

        when(employmentHistoryService.getEmploymentHistoryById(histId)).thenReturn(Optional.of(existingHistory));
        when(employmentHistoryService.updateEmploymentHistory(any(EmploymentHistory.class))).thenReturn(true);

        // Act
        Response response = employmentHistoryResource.updateEmploymentHistory(histId, updatedHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<EmploymentHistory> apiResponse = (ApiResponse<EmploymentHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(histId);
        verify(employmentHistoryService, times(1)).getEmploymentHistoryById(histId);
        verify(employmentHistoryService, times(1)).updateEmploymentHistory(any(EmploymentHistory.class));
    }

    @Test
    void testUpdateEmploymentHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        EmploymentHistory updatedHistory = new EmploymentHistory();
        updatedHistory.setJobTitle("Senior Engineer");

        when(employmentHistoryService.getEmploymentHistoryById(histId)).thenReturn(Optional.empty());

        // Act
        Response response = employmentHistoryResource.updateEmploymentHistory(histId, updatedHistory);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<EmploymentHistory> apiResponse = (ApiResponse<EmploymentHistory>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history not found");
        verify(employmentHistoryService, times(1)).getEmploymentHistoryById(histId);
        verify(employmentHistoryService, never()).updateEmploymentHistory(any(EmploymentHistory.class));
    }

    @Test
    void testDeleteEmploymentHistory_Success() {
        // Arrange
        Long histId = 1L;
        when(employmentHistoryService.deleteEmploymentHistory(histId)).thenReturn(true);

        // Act
        Response response = employmentHistoryResource.deleteEmploymentHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history deleted successfully");
        verify(employmentHistoryService, times(1)).deleteEmploymentHistory(histId);
    }

    @Test
    void testDeleteEmploymentHistory_NotFound() {
        // Arrange
        Long histId = 999L;
        when(employmentHistoryService.deleteEmploymentHistory(histId)).thenReturn(false);

        // Act
        Response response = employmentHistoryResource.deleteEmploymentHistory(histId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employment history not found");
        verify(employmentHistoryService, times(1)).deleteEmploymentHistory(histId);
    }
}

