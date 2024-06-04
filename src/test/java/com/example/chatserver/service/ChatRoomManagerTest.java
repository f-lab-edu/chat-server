package com.example.chatserver.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.model.UserConnection;
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
        //Given
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        when(sessionMock.getAttributes()).thenReturn(Map.of("email", "user1@example.com"));
        UserConnection userConnection = new UserConnection("user1@example.com", sessionMock);

        ChatDto chatDto = new ChatDto();

        WebSocketSession otherSessionMock = mock(WebSocketSession.class);
        when(otherSessionMock.getAttributes()).thenReturn(Map.of("email", "user2@example.com"));
        UserConnection otherUserConnection = new UserConnection("user2@example.com", otherSessionMock);
        chatRoomManager.getActiveUserMap().put("user2@example.com", otherUserConnection);

        // When
        chatRoomManager.enterChatRoom(chatDto, userConnection);

        // Then
        assertTrue(chatRoomManager.getActiveUserMap().containsKey("user1@example.com"));
        verify(sessionMock, never()).sendMessage(any());

        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(otherSessionMock).sendMessage(captor.capture());
        assertTrue(captor.getValue().getPayload().contains("ENTER"));
    }

    @Test
    public void testExitChatRoom() throws Exception {
        // Given
        WebSocketSession sessionMock = mock(WebSocketSession.class);
        UserConnection userConnection = new UserConnection("user1@example.com", sessionMock);

        ChatDto chatDto = new ChatDto();  // initialize with necessary values
        chatDto.setUsername("user1@example.com");
        chatRoomManager.getActiveUserMap().put("user1@example.com", userConnection);

        // When
        chatRoomManager.exitChatRoom("user1@example.com", chatDto);

        // Then
        assertFalse(chatRoomManager.getActiveUserMap().containsKey("user1@example.com"));
        verify(sessionMock, never()).sendMessage(any());
    }

    @Test
    public void testSendMessage() throws Exception {
        // Given
        WebSocketSession session1Mock = mock(WebSocketSession.class);
        WebSocketSession session2Mock = mock(WebSocketSession.class);

        UserConnection userConnection1 = new UserConnection("user1@example.com", session1Mock);
        UserConnection userConnection2 = new UserConnection("user2@example.com", session2Mock);

        chatRoomManager.getActiveUserMap().put("user1@example.com", userConnection1);
        chatRoomManager.getActiveUserMap().put("user2@example.com", userConnection2);

        ChatDto chatDto = new ChatDto();  // initialize with necessary values

        // When
        chatRoomManager.sendMessage("user1@example.com", chatDto);

        // Then
        ArgumentCaptor<TextMessage> captor = ArgumentCaptor.forClass(TextMessage.class);
        verify(session2Mock).sendMessage(captor.capture());
        assertTrue(captor.getValue().getPayload().contains("TALK"));
        verify(session1Mock, never()).sendMessage(any());
    }
}