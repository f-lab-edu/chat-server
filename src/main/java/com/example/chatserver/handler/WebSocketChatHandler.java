package com.example.chatserver.handler;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.model.UserConnection;
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
        ChatDto chatDto = objectMapper.readValue(message.getPayload(), ChatDto.class);

        switch (chatDto.getType()) {
            case ENTER -> enterChatRoom(chatDto, session);
            case TALK -> sendMessage(userId, chatDto);
            case EXIT -> exitChatRoom(userId, chatDto);
        }

        chatService.saveMessage(chatDto);
    }

    private void sendMessage(String userId, ChatDto chatDto) {
        log.info("send chatDto : " + chatDto.toString());
        ChatRoomManager chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.sendMessage(userId, chatDto);
    }

    private void enterChatRoom(ChatDto chatDto, WebSocketSession session) {
        log.info("enter chatDto : " + chatDto.toString());
        UserConnection userConnection = new UserConnection(chatDto.getUsername(), session);
        chatDto.setMessage(chatDto.getUsername() + "님이 입장하셨습니다.");
        ChatRoomManager chatRoom = chatRoomMap.getOrDefault(chatDto.getChatRoomId(), new ChatRoomManager(objectMapper));
        chatRoom.enterChatRoom(chatDto, userConnection);
        chatRoomMap.put(chatDto.getChatRoomId(), chatRoom);
    }

    private void exitChatRoom(String userId, ChatDto chatDto) {
        log.info("exit chatDto : " + chatDto.toString());
        chatDto.setMessage(chatDto.getUsername() + "님이 퇴장하셨습니다.");
        ChatRoomManager chatRoom = chatRoomMap.get(chatDto.getChatRoomId());
        chatRoom.exitChatRoom(userId, chatDto);

        if(chatRoom.getActiveUserMap().isEmpty()){
            chatRoomMap.remove(chatDto.getChatRoomId());
        }
    }
}
