package com.example.chatserver.handler;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.service.ChatRoomManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ChatRoomManager chatRoomManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        ChatDto chatDto = objectMapper.readValue(message.getPayload(), ChatDto.class);
        switch (chatDto.getType()){
            case ENTER ->  enterChatRoom(chatDto, session);
            case TALK -> sendMessage(chatDto);
        }
    }

    private void sendMessage(ChatDto chatDto) {
        log.info("send chatDto : " + chatDto.toString());
        chatRoomManager.sendMessage(chatDto);
    }

    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("enter chatDto : " + chatDto.toString());
        chatRoomManager.enter(chatDto, session);
    }
}
