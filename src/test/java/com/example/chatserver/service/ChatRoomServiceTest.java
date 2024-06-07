package com.example.chatserver.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.WebSocketSession;

public class ChatRoomServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock StringRedisTemplate의 getConnectionFactory() 메서드
        when(stringRedisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
    }

    @Test
    public void testPublish() {
        // Given
        String channel = "testChannel";
        String message = "testMessage";

        // When
        chatRoomService.publish(channel, message);

        // Then
        verify(stringRedisTemplate, times(1)).convertAndSend(eq(channel), eq(message));
    }

    @Test
    public void testSubscribe() {
        // Given
        String channel = "testChannel";
        WebSocketSession session = mock(WebSocketSession.class);
        RedisConnection redisConnection = mock(StringRedisConnection.class);

        // Redis 연결을 Mocking
        when(Objects.requireNonNull(stringRedisTemplate.getConnectionFactory()).getConnection()).thenReturn(redisConnection);

        // When
        chatRoomService.subscribe(channel, session);

        // Then
        verify(redisConnection, times(1)).subscribe(any(MessageListener.class), any(byte[].class));
    }
}
