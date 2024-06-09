package com.example.chatserver.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class ChatDto {
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatRoomId;
    private String username;
    private String message;
}
