package com.example.chatserver.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

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
}
