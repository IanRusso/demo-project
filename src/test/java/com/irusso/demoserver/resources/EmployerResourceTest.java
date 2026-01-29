package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.Employer;
import com.irusso.demoserver.service.EmployerService;
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
class EmployerResourceTest {

    @Mock
    private EmployerService employerService;

    private EmployerResource employerResource;

    @BeforeEach
    void setUp() {
        employerResource = new EmployerResource(employerService);
    }

    @Test
    void testGetAllEmployers() {
        // Arrange
        Employer emp1 = new Employer();
        emp1.setId(1L);
        emp1.setName("Company A");

        Employer emp2 = new Employer();
        emp2.setId(2L);
        emp2.setName("Company B");

        List<Employer> employers = Arrays.asList(emp1, emp2);
        when(employerService.getAllEmployers()).thenReturn(employers);

        // Act
        Response response = employerResource.getAllEmployers();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<Employer>> apiResponse = (ApiResponse<List<Employer>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(employerService, times(1)).getAllEmployers();
    }

    @Test
    void testGetEmployer_Found() {
        // Arrange
        Long empId = 1L;
        Employer employer = new Employer();
        employer.setId(empId);
        employer.setName("Company A");

        when(employerService.getEmployerById(empId)).thenReturn(Optional.of(employer));

        // Act
        Response response = employerResource.getEmployer(empId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Employer> apiResponse = (ApiResponse<Employer>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(empId);
        verify(employerService, times(1)).getEmployerById(empId);
    }

    @Test
    void testGetEmployer_NotFound() {
        // Arrange
        Long empId = 999L;
        when(employerService.getEmployerById(empId)).thenReturn(Optional.empty());

        // Act
        Response response = employerResource.getEmployer(empId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Employer> apiResponse = (ApiResponse<Employer>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer not found");
        verify(employerService, times(1)).getEmployerById(empId);
    }

    @Test
    void testCreateEmployer() {
        // Arrange
        Employer newEmployer = new Employer();
        newEmployer.setName("New Company");

        Long generatedId = 123L;
        when(employerService.createEmployer(newEmployer)).thenReturn(generatedId);

        // Act
        Response response = employerResource.createEmployer(newEmployer);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<Employer> apiResponse = (ApiResponse<Employer>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(employerService, times(1)).createEmployer(newEmployer);
    }

    @Test
    void testUpdateEmployer_Success() {
        // Arrange
        Long empId = 1L;
        Employer existingEmployer = new Employer();
        existingEmployer.setId(empId);
        existingEmployer.setName("Old Company");

        Employer updatedEmployer = new Employer();
        updatedEmployer.setName("Updated Company");

        when(employerService.getEmployerById(empId)).thenReturn(Optional.of(existingEmployer));
        when(employerService.updateEmployer(any(Employer.class))).thenReturn(true);

        // Act
        Response response = employerResource.updateEmployer(empId, updatedEmployer);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Employer> apiResponse = (ApiResponse<Employer>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(empId);
        verify(employerService, times(1)).getEmployerById(empId);
        verify(employerService, times(1)).updateEmployer(any(Employer.class));
    }

    @Test
    void testUpdateEmployer_NotFound() {
        // Arrange
        Long empId = 999L;
        Employer updatedEmployer = new Employer();
        updatedEmployer.setName("Updated Company");

        when(employerService.getEmployerById(empId)).thenReturn(Optional.empty());

        // Act
        Response response = employerResource.updateEmployer(empId, updatedEmployer);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Employer> apiResponse = (ApiResponse<Employer>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer not found");
        verify(employerService, times(1)).getEmployerById(empId);
        verify(employerService, never()).updateEmployer(any(Employer.class));
    }

    @Test
    void testDeleteEmployer_Success() {
        // Arrange
        Long empId = 1L;
        when(employerService.deleteEmployer(empId)).thenReturn(true);

        // Act
        Response response = employerResource.deleteEmployer(empId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer deleted successfully");
        verify(employerService, times(1)).deleteEmployer(empId);
    }

    @Test
    void testDeleteEmployer_NotFound() {
        // Arrange
        Long empId = 999L;
        when(employerService.deleteEmployer(empId)).thenReturn(false);

        // Act
        Response response = employerResource.deleteEmployer(empId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Employer not found");
        verify(employerService, times(1)).deleteEmployer(empId);
    }
}

