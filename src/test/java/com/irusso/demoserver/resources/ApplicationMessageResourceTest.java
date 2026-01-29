package com.irusso.demoserver.resources;

import com.irusso.demoserver.api.ApiResponse;
import com.irusso.demoserver.db.model.ApplicationMessage;
import com.irusso.demoserver.service.ApplicationMessageService;
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
class ApplicationMessageResourceTest {

    @Mock
    private ApplicationMessageService applicationMessageService;

    private ApplicationMessageResource applicationMessageResource;

    @BeforeEach
    void setUp() {
        applicationMessageResource = new ApplicationMessageResource(applicationMessageService);
    }

    @Test
    void testGetAllApplicationMessages() {
        // Arrange
        ApplicationMessage msg1 = new ApplicationMessage();
        msg1.setId(1L);
        msg1.setApplicationId(100L);

        ApplicationMessage msg2 = new ApplicationMessage();
        msg2.setId(2L);
        msg2.setApplicationId(200L);

        List<ApplicationMessage> messages = Arrays.asList(msg1, msg2);
        when(applicationMessageService.getAllApplicationMessages()).thenReturn(messages);

        // Act
        Response response = applicationMessageResource.getAllApplicationMessages();

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<List<ApplicationMessage>> apiResponse = (ApiResponse<List<ApplicationMessage>>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData()).hasSize(2);
        verify(applicationMessageService, times(1)).getAllApplicationMessages();
    }

    @Test
    void testGetApplicationMessage_Found() {
        // Arrange
        Long msgId = 1L;
        ApplicationMessage message = new ApplicationMessage();
        message.setId(msgId);
        message.setApplicationId(100L);

        when(applicationMessageService.getApplicationMessageById(msgId)).thenReturn(Optional.of(message));

        // Act
        Response response = applicationMessageResource.getApplicationMessage(msgId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<ApplicationMessage> apiResponse = (ApiResponse<ApplicationMessage>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getData().getId()).isEqualTo(msgId);
        verify(applicationMessageService, times(1)).getApplicationMessageById(msgId);
    }

    @Test
    void testGetApplicationMessage_NotFound() {
        // Arrange
        Long msgId = 999L;
        when(applicationMessageService.getApplicationMessageById(msgId)).thenReturn(Optional.empty());

        // Act
        Response response = applicationMessageResource.getApplicationMessage(msgId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<ApplicationMessage> apiResponse = (ApiResponse<ApplicationMessage>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message not found");
        verify(applicationMessageService, times(1)).getApplicationMessageById(msgId);
    }

    @Test
    void testCreateApplicationMessage() {
        // Arrange
        ApplicationMessage newMessage = new ApplicationMessage();
        newMessage.setApplicationId(100L);
        newMessage.setMessageText("Hello");

        Long generatedId = 123L;
        when(applicationMessageService.createApplicationMessage(newMessage)).thenReturn(generatedId);

        // Act
        Response response = applicationMessageResource.createApplicationMessage(newMessage);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        ApiResponse<ApplicationMessage> apiResponse = (ApiResponse<ApplicationMessage>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message created successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(generatedId);
        verify(applicationMessageService, times(1)).createApplicationMessage(newMessage);
    }

    @Test
    void testUpdateApplicationMessage_Success() {
        // Arrange
        Long msgId = 1L;
        ApplicationMessage existingMessage = new ApplicationMessage();
        existingMessage.setId(msgId);
        existingMessage.setApplicationId(100L);

        ApplicationMessage updatedMessage = new ApplicationMessage();
        updatedMessage.setMessageText("Updated message");

        when(applicationMessageService.getApplicationMessageById(msgId)).thenReturn(Optional.of(existingMessage));
        when(applicationMessageService.updateApplicationMessage(any(ApplicationMessage.class))).thenReturn(true);

        // Act
        Response response = applicationMessageResource.updateApplicationMessage(msgId, updatedMessage);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<ApplicationMessage> apiResponse = (ApiResponse<ApplicationMessage>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message updated successfully");
        assertThat(apiResponse.getData().getId()).isEqualTo(msgId);
        verify(applicationMessageService, times(1)).getApplicationMessageById(msgId);
        verify(applicationMessageService, times(1)).updateApplicationMessage(any(ApplicationMessage.class));
    }

    @Test
    void testUpdateApplicationMessage_NotFound() {
        // Arrange
        Long msgId = 999L;
        ApplicationMessage updatedMessage = new ApplicationMessage();
        updatedMessage.setMessageText("Updated message");

        when(applicationMessageService.getApplicationMessageById(msgId)).thenReturn(Optional.empty());

        // Act
        Response response = applicationMessageResource.updateApplicationMessage(msgId, updatedMessage);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<ApplicationMessage> apiResponse = (ApiResponse<ApplicationMessage>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message not found");
        verify(applicationMessageService, times(1)).getApplicationMessageById(msgId);
        verify(applicationMessageService, never()).updateApplicationMessage(any(ApplicationMessage.class));
    }

    @Test
    void testDeleteApplicationMessage_Success() {
        // Arrange
        Long msgId = 1L;
        when(applicationMessageService.deleteApplicationMessage(msgId)).thenReturn(true);

        // Act
        Response response = applicationMessageResource.deleteApplicationMessage(msgId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isTrue();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message deleted successfully");
        verify(applicationMessageService, times(1)).deleteApplicationMessage(msgId);
    }

    @Test
    void testDeleteApplicationMessage_NotFound() {
        // Arrange
        Long msgId = 999L;
        when(applicationMessageService.deleteApplicationMessage(msgId)).thenReturn(false);

        // Act
        Response response = applicationMessageResource.deleteApplicationMessage(msgId);

        // Assert
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        ApiResponse<Object> apiResponse = (ApiResponse<Object>) response.getEntity();
        assertThat(apiResponse.isSuccess()).isFalse();
        assertThat(apiResponse.getMessage()).isEqualTo("Application message not found");
        verify(applicationMessageService, times(1)).deleteApplicationMessage(msgId);
    }
}

