package com.irusso.demoserver.service;

import com.irusso.demoserver.db.dao.ApplicationMessageDao;
import com.irusso.demoserver.db.model.ApplicationMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationMessageServiceTest {

    @Mock
    private ApplicationMessageDao applicationMessageDao;

    private ApplicationMessageService applicationMessageService;

    @BeforeEach
    void setUp() {
        applicationMessageService = new ApplicationMessageService(applicationMessageDao);
    }

    @Test
    void testGetAllApplicationMessages() {
        // Arrange
        ApplicationMessage msg1 = new ApplicationMessage();
        msg1.setId(1L);
        msg1.setApplicationId(100L);
        msg1.setMessageText("Hello");

        ApplicationMessage msg2 = new ApplicationMessage();
        msg2.setId(2L);
        msg2.setApplicationId(101L);
        msg2.setMessageText("Thank you");

        List<ApplicationMessage> expectedMessages = Arrays.asList(msg1, msg2);
        when(applicationMessageDao.findAll()).thenReturn(expectedMessages);

        // Act
        List<ApplicationMessage> actualMessages = applicationMessageService.getAllApplicationMessages();

        // Assert
        assertThat(actualMessages).hasSize(2);
        assertThat(actualMessages).containsExactlyElementsOf(expectedMessages);
        verify(applicationMessageDao, times(1)).findAll();
    }

    @Test
    void testGetApplicationMessageById_Found() {
        // Arrange
        ApplicationMessage message = new ApplicationMessage();
        message.setId(1L);
        message.setApplicationId(100L);
        message.setSenderType("user");

        when(applicationMessageDao.findById(1L)).thenReturn(Optional.of(message));

        // Act
        Optional<ApplicationMessage> result = applicationMessageService.getApplicationMessageById(1L);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        assertThat(result.get().getSenderType()).isEqualTo("user");
        verify(applicationMessageDao, times(1)).findById(1L);
    }

    @Test
    void testGetApplicationMessageById_NotFound() {
        // Arrange
        when(applicationMessageDao.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<ApplicationMessage> result = applicationMessageService.getApplicationMessageById(999L);

        // Assert
        assertThat(result).isEmpty();
        verify(applicationMessageDao, times(1)).findById(999L);
    }

    @Test
    void testCreateApplicationMessage() {
        // Arrange
        ApplicationMessage newMessage = new ApplicationMessage();
        newMessage.setApplicationId(100L);
        newMessage.setMessageText("New message");

        when(applicationMessageDao.insert(newMessage)).thenReturn(1L);

        // Act
        Long messageId = applicationMessageService.createApplicationMessage(newMessage);

        // Assert
        assertThat(messageId).isEqualTo(1L);
        verify(applicationMessageDao, times(1)).insert(newMessage);
    }

    @Test
    void testUpdateApplicationMessage_Success() {
        // Arrange
        ApplicationMessage message = new ApplicationMessage();
        message.setId(1L);
        message.setMessageText("Updated message");

        when(applicationMessageDao.update(1L, message)).thenReturn(true);

        // Act
        boolean result = applicationMessageService.updateApplicationMessage(message);

        // Assert
        assertThat(result).isTrue();
        verify(applicationMessageDao, times(1)).update(1L, message);
    }

    @Test
    void testUpdateApplicationMessage_Failure() {
        // Arrange
        ApplicationMessage message = new ApplicationMessage();
        message.setId(999L);
        message.setMessageText("Failed update");

        when(applicationMessageDao.update(999L, message)).thenReturn(false);

        // Act
        boolean result = applicationMessageService.updateApplicationMessage(message);

        // Assert
        assertThat(result).isFalse();
        verify(applicationMessageDao, times(1)).update(999L, message);
    }

    @Test
    void testDeleteApplicationMessage_Success() {
        // Arrange
        when(applicationMessageDao.delete(1L)).thenReturn(true);

        // Act
        boolean result = applicationMessageService.deleteApplicationMessage(1L);

        // Assert
        assertThat(result).isTrue();
        verify(applicationMessageDao, times(1)).delete(1L);
    }

    @Test
    void testDeleteApplicationMessage_Failure() {
        // Arrange
        when(applicationMessageDao.delete(999L)).thenReturn(false);

        // Act
        boolean result = applicationMessageService.deleteApplicationMessage(999L);

        // Assert
        assertThat(result).isFalse();
        verify(applicationMessageDao, times(1)).delete(999L);
    }
}

