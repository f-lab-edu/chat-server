package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.WebSocketMessage;
import com.example.chatserver.dto.WebSocketMessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ChatRoomManager {

    private final Map<String, WebSocketSession> ActiveUserMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        String username = (String) session.getAttributes().get("email");
        ActiveUserMap.put(username, session);
        for(Map.Entry<String, WebSocketSession> entry : ActiveUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(username))
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.ENTER, chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void exitChatRoom(String email, ChatDto chatDto) {
        ActiveUserMap.remove(chatDto.getUsername());
        for(Map.Entry<String, WebSocketSession> entry : ActiveUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(email))
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.EXIT, chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void sendMessage(String email, ChatDto chatDto) {
        for(Map.Entry<String, WebSocketSession> entry : ActiveUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(email))
                    entry.getValue().sendMessage(getTextMessage(WebSocketMessageType.TALK, chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private TextMessage getTextMessage(WebSocketMessageType type, ChatDto chatDto) {
        try {
            return new TextMessage(
                objectMapper.writeValueAsString(
                    new WebSocketMessage(type, chatDto)
                ));
        }catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
