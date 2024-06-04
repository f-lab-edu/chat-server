package com.example.chatserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class UserConnection {

    private String userId;
    private WebSocketSession session;

}
