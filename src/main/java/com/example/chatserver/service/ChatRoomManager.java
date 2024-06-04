package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.model.UserConnection;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ChatRoomManager {

    private final Map<String, UserConnection> activeUserMap = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public void enterChatRoom(ChatDto chatDto, UserConnection userConnection) {
        String userId = userConnection.getUserId();
        activeUserMap.put(userId, userConnection);
        for(Map.Entry<String, UserConnection> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(userId))
                    entry.getValue().getSession().sendMessage(getTextMessage(chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void exitChatRoom(String userId, ChatDto chatDto) {
        activeUserMap.remove(userId);
        for (Map.Entry<String, UserConnection> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(userId))
                    entry.getValue().getSession().sendMessage(getTextMessage(chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public void sendMessage(String userId, ChatDto chatDto) {
        for (Map.Entry<String, UserConnection> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(userId))
                    entry.getValue().getSession().sendMessage(getTextMessage(chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private TextMessage getTextMessage(ChatDto chatDto) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(chatDto));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
