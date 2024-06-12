package com.example.chatserver.handler;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.processor.ChatMessageBatchProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@RequiredArgsConstructor
public class RedisMessageHandler implements MessageListener {
    private final WebSocketSession session;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageBatchProcessor chatMessageBatchProcessor;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatDto chatDto = objectMapper.readValue(message.getBody(), ChatDto.class);
            if(session.isOpen() && !chatDto.getUsername().equals(session.getAttributes().get("username"))){
                session.sendMessage(new TextMessage(new String(message.getBody())));
                chatMessageBatchProcessor.addMessage(chatDto);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
