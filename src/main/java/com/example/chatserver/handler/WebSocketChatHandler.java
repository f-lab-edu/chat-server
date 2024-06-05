package com.example.chatserver.handler;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.MessageEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {


    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {

        ChatDto chatDto = objectMapper.readValue(message.getPayload(), ChatDto.class);
        String userId = Objects.requireNonNull(session.getPrincipal()).getName();
        eventPublisher.publishEvent(new MessageEvent(this,userId,chatDto,session));

    }
}
