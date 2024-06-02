package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.WebSocketMessageType;
import com.example.chatserver.model.ChatMessage;
import com.example.chatserver.model.ChatRoom;
import com.example.chatserver.repository.ChatMessageRepository;
import com.example.chatserver.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(ChatMessageRepository chatMessageRepository, ChatRoomRepository chatRoomRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public void saveMessage(WebSocketMessageType type,ChatDto chat) {

        ChatRoom chatRoom = chatRoomRepository.findById(chat.getChatRoomId())
            .orElseThrow(IllegalArgumentException::new);

        ChatMessage chatMessage = ChatMessage.builder()
            .chatRoom(chatRoom)
            .type(type)
            .sender(chat.getUsername())
            .message(chat.getMessage())
            .timestamp(LocalDateTime.now()).build();

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getMessages(Long chatRoomId) {
        return chatMessageRepository.findByChatRoomId(chatRoomId);
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
            .name(name).build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }
}