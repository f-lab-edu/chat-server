package com.example.chatserver.dto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WebSocketMessageType {
    ENTER("ENTER"),
    TALK("TALK"),
    EXIT("EXIT");

    private final String type;
}
