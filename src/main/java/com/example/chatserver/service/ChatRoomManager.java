package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Getter
@Component
@RequiredArgsConstructor
public class ChatRoomManager {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatRoomService chatRoomService;

    public void enter(ChatDto chatDto, WebSocketSession session) {
        String username = chatDto.getUsername();
        String channel = "chatRoom:"+chatDto.getChatRoomId();
        chatRoomService.subscribe(channel, session);

        chatDto.setMessage(username + "님이 입장하셨습니다.");
        chatRoomService.publish(channel, getTextMessage(chatDto));
    }

    public void sendMessage(ChatDto chatDto) {
        String channel = "chatRoom:"+chatDto.getChatRoomId();
        chatRoomService.publish(channel, getTextMessage(chatDto));
    }

    private String getTextMessage(ChatDto chatDto) {
        try {
            return objectMapper.writeValueAsString(chatDto);
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
