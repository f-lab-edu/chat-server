package com.example.chatserver.dto;

import com.example.chatserver.model.ChatMessage;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private String sender;

    private String message;

    private Timestamp timestamp;

    public static ChatMessageDto from(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
            .sender(chatMessage.getSender())
            .message(chatMessage.getMessage())
            .timestamp(chatMessage.getTimestamp())
            .build();
    }
}
