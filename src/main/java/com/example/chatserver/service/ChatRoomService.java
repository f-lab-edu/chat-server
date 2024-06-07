package com.example.chatserver.service;

import com.example.chatserver.handler.RedisMessageHandler;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final StringRedisTemplate stringRedisTemplate;

    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }

    public void subscribe(String channel, WebSocketSession session) {
        Objects.requireNonNull(stringRedisTemplate.getConnectionFactory())
            .getConnection()
            .subscribe(getMessageHandler(session), channel.getBytes());
    }

    private RedisMessageHandler getMessageHandler(WebSocketSession session) {
        return new RedisMessageHandler(session);
    }
}
