package com.example.chatserver.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.chatserver.dto.ChatDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public class ChatRoomManagerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ChatRoomManager chatRoomManager;

    @BeforeEach
    public void setUp() {
        chatRoomManager = new ChatRoomManager(objectMapper);
    }

    @Test
    public void testEnterChatRoom() throws Exception {
        // Given
        WebSocketSession session = mock(WebSocketSession.class);
        when(session.getAttributes()).thenReturn(Map.of("email", "user1@example.com"));
        ChatDto chatDto = new ChatDto();  // initialize with necessary values

        // Mock another session to see if the message is broadcasted
        WebSocketSession otherSession = mock(WebSocketSession.class);
        when(otherSession.getAttributes()).thenReturn(Map.of("email", "user2@example.com"));
        chatRoomManager.getActiveUserMap().put("user2@example.com", otherSession);

        // When
        chatRoomManager.enterChatRoom(chatDto, session);

        // Then
        assertTrue(chatRoomManager.getActiveUserMap().containsKey("user1@example.com"));
        verify(session, never()).sendMessage(any());

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(otherSession).sendMessage(captor.capture());
        assertTrue(captor.getValue().getPayload().contains("ENTER"));
    }

    @Test
    public void testExitChatRoom() throws Exception {
        // Given
        WebSocketSession session = mock(WebSocketSession.class);
        ChatDto chatDto = new ChatDto();  // initialize with necessary values
        chatDto.setUsername("user1@example.com");
        chatRoomManager.getActiveUserMap().put("user1@example.com", session);

        // When
        chatRoomManager.exitChatRoom("user1@example.com", chatDto);

        // Then
        assertFalse(chatRoomManager.getActiveUserMap().containsKey("user1@example.com"));
        verify(session, never()).sendMessage(any());
    }

    @Test
    public void testSendMessage() throws Exception {
        // Given
        WebSocketSession session1 = mock(WebSocketSession.class);
        WebSocketSession session2 = mock(WebSocketSession.class);

        chatRoomManager.getActiveUserMap().put("user1@example.com", session1);
        chatRoomManager.getActiveUserMap().put("user2@example.com", session2);

        ChatDto chatDto = new ChatDto();  // initialize with necessary values

        // When
        chatRoomManager.sendMessage("user1@example.com", chatDto);

        // Then
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session2).sendMessage(captor.capture());
        assertTrue(captor.getValue().getPayload().contains("TALK"));
        verify(session1, never()).sendMessage(any());
    }
}