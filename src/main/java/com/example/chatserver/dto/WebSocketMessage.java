package com.example.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {

    private WebSocketMessageType type;
    private ChatDto chatDto;
}
