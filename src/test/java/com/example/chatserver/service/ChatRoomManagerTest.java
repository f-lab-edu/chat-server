package com.example.chatserver.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.example.chatserver.dto.ChatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.socket.WebSocketSession;

public class ChatRoomManagerTest {

    @Mock
    private ChatRoomService chatRoomService;

    @InjectMocks
    private ChatRoomManager chatRoomManager;

    private ChatDto chatDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        chatDto = new ChatDto();
        chatDto.setUsername("testUser");
        chatDto.setChatRoomId(123L);
        chatDto.setMessage("Hello, World!");
    }

    @Test
    public void testEnter(){
        // Given
        String expectedChannel = "chatRoom:123";
        String expectedMessage = "testUser님이 입장하셨습니다.";
        String channel = "1";

        // When
        chatRoomManager.enter(chatDto, channel);

        // Then
        chatDto.setMessage(expectedMessage); // Adjust message after calling enter
        verify(chatRoomService, times(1)).publish(eq(expectedChannel), eq(getTextMessage(chatDto)));
    }

    @Test
    public void testSendMessage(){
        // Given
        String expectedChannel = "chatRoom:123";
        doNothing().when(chatRoomService).publish(eq(expectedChannel), any(String.class));

        // When
        chatRoomManager.sendMessage(chatDto);

        // Then
        verify(chatRoomService, times(1)).publish(eq(expectedChannel), eq(getTextMessage(chatDto)));
    }

    private String getTextMessage(ChatDto chatDto) {
        try {
            return new ObjectMapper().writeValueAsString(chatDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}