package com.example.chatserver.handler;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.WebSocketMessage;
import com.example.chatserver.service.ChatRoomManager;
import com.example.chatserver.service.ChatService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
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

    private final Map<Long, ChatRoomManager> chatRoomMap = new ConcurrentHashMap<>();
    private final ChatService chatService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String userId = Objects.requireNonNull(session.getPrincipal()).getName();
        WebSocketMessage webSocketMessage = objectMapper.readValue(message.getPayload(), WebSocketMessage.class);

        switch (webSocketMessage.getType()) {
            case ENTER -> enterChatRoom(webSocketMessage.getChatDto(), session);
            case TALK -> sendMessage(userId, webSocketMessage.getChatDto());
            case EXIT -> exitChatRoom(userId, webSocketMessage.getChatDto());
        }

        chatService.saveMessage(webSocketMessage.getType(), webSocketMessage.getChatDto());
    }

    private void sendMessage(String username, ChatDto chatDto) {
        log.info("send chatDto : " + chatDto.toString());
        ChatRoomManager chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.sendMessage(username, chatDto);
    }

    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {

        log.info("enter chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 입장하셨습니다.");
        ChatRoomManager chatRoom = chatRoomMap
            .getOrDefault(chatDto.getChatRoomId(), new ChatRoomManager(objectMapper));
        chatRoom.enterChatRoom(chatDto, session);
        chatRoomMap.put(chatDto.getChatRoomId(), chatRoom);
    }

    private void exitChatRoom(String username, ChatDto chatDto) {
        log.info("exit chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 퇴장하셨습니다.");
        ChatRoomManager chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.exitChatRoom(username, chatDto);

        if(chatRoom.getActiveUserMap().isEmpty()){
            chatRoomMap.remove(chatDto.getChatRoomId());
        }
    }
}
