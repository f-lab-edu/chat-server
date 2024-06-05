package com.example.chatserver.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

@Getter
public class MessageEvent extends ApplicationEvent {

    private final String userId;
    private final ChatDto chatDto;
    private final WebSocketSession session;

    public MessageEvent(Object source, String userId, ChatDto chatDto, WebSocketSession session) {
        super(source);
        this.userId = userId;
        this.chatDto = chatDto;
        this.session = session;
    }
}
