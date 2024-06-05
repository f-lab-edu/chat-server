package com.example.chatserver.listener;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.MessageEvent;
import com.example.chatserver.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageListener {

    private final Map<String, WebSocketSession> activeUserMap = new ConcurrentHashMap<>();
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @EventListener
    @Transactional
    public void handleMessageEvent(MessageEvent event) {
        ChatDto chatDto = event.getChatDto();
        WebSocketSession session = event.getSession();
        switch (chatDto.getType()) {
            case ENTER -> enterChatRoom(chatDto, session);
            case TALK -> sendMessage(event.getUserId(), chatDto);
            case EXIT -> exitChatRoom(event.getUserId(), chatDto);
        }
        chatService.saveMessage(chatDto);
    }

    private void sendMessage(String userId, ChatDto chatDto) {
        log.info("send chatDto : {}", chatDto.toString());
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(userId))
                    entry.getValue().sendMessage(getTextMessage(chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("enter chatDto : {}", chatDto.toString());

        activeUserMap.put(session.getId(), session);
        for(Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                session.getId();
                entry.getValue().sendMessage(getTextMessage(chatDto));
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    private void exitChatRoom(String userId, ChatDto chatDto) {
        log.info("exit chatDto : {}", chatDto.toString());
        activeUserMap.remove(userId);
        for (Map.Entry<String, WebSocketSession> entry : activeUserMap.entrySet()) {
            try {
                if (!entry.getKey().equals(userId))
                    entry.getValue().sendMessage(getTextMessage(chatDto));
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
