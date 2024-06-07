package com.example.chatserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final StringRedisTemplate stringRedisTemplate;


    public void publish(String channel, String message) {
        stringRedisTemplate.convertAndSend(channel, message);
    }


}
