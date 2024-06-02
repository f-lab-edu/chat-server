package com.example.chatserver.controller;

import com.example.chatserver.model.ChatMessage;
import com.example.chatserver.model.ChatRoom;
import com.example.chatserver.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public ChatRoom createChatRoom(@RequestParam String name) {
        return chatService.createChatRoom(name);
    }

    @GetMapping("/rooms")
    public List<ChatRoom> getAllChatRooms() {
        return chatService.getAllChatRooms();
    }

    @GetMapping("/messages")
    public List<ChatMessage> getMessages(@RequestParam Long chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }
}
