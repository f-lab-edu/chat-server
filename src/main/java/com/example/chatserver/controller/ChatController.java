package com.example.chatserver.controller;

import com.example.chatserver.dto.ChatMessageDto;
import com.example.chatserver.dto.ChatRoomDto;
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
    public ChatRoomDto createChatRoom(@RequestParam String name) {
        return chatService.createChatRoom(name);
    }

    @GetMapping("/rooms")
    public List<ChatRoomDto> getAllChatRooms() {
        return chatService.getAllChatRooms();
    }

    @GetMapping("/messages")
    public List<ChatMessageDto> getMessages(@RequestParam Long chatRoomId) {
        return chatService.getMessages(chatRoomId);
    }
}
