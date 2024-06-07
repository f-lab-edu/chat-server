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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

@ExtendWith(MockitoExtension.class)
public class ChatRoomManagerTest {

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private WebSocketSession session;

    @InjectMocks
    private ChatRoomManager chatRoomManager;

    private ChatDto chatDto;

    @BeforeEach
    public void setUp() {
        chatDto = new ChatDto();
        chatDto.setUsername("testUser");
        chatDto.setChatRoomId(123L);
        chatDto.setMessage("Hello, World!");
    }

    @Test
    public void testEnter(){
        // Arrange
        String expectedChannel = "chatRoom:123";
        String expectedMessage = "testUser님이 입장하셨습니다.";

        // Mockito의 any 메서드를 사용하여 임의의 WebSocketSession을 받아들이도록 설정
        doNothing().when(chatRoomService).subscribe(eq(expectedChannel), any(WebSocketSession.class));
        doNothing().when(chatRoomService).publish(eq(expectedChannel), any(String.class));

        // Act
        chatRoomManager.enter(chatDto, session);

        // Assert
        verify(chatRoomService, times(1)).subscribe(eq(expectedChannel), any(WebSocketSession.class));
        chatDto.setMessage(expectedMessage); // Adjust message after calling enter
        verify(chatRoomService, times(1)).publish(eq(expectedChannel), eq(getTextMessage(chatDto)));
    }

    @Test
    public void testSendMessage(){
        // Arrange
        String expectedChannel = "chatRoom:123";

        doNothing().when(chatRoomService).publish(eq(expectedChannel), any(String.class));

        // Act
        chatRoomManager.sendMessage(chatDto);

        // Assert
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