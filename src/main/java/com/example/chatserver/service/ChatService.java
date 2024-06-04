package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.ChatMessageDto;
import com.example.chatserver.dto.ChatRoomDto;
import com.example.chatserver.model.ChatMessage;
import com.example.chatserver.model.ChatRoom;
import com.example.chatserver.repository.ChatMessageRepository;
import com.example.chatserver.repository.ChatRoomRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void saveMessage(ChatDto chat) {

        ChatRoom chatRoom = chatRoomRepository.findById(chat.getChatRoomId())
            .orElseThrow(IllegalArgumentException::new);

        ChatMessage chatMessage = ChatMessage.builder()
            .chatRoom(chatRoom)
            .type(chat.getType())
            .sender(chat.getUsername())
            .message(chat.getMessage())
            .timestamp(LocalDateTime.now()).build();

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessageDto> getMessages(Long chatRoomId) {
        return chatMessageRepository.findTopMessagesByChatRoomId(chatRoomId)
            .stream().map(ChatMessageDto::from)
            .collect(Collectors.toList());
    }

    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
            .name(name).build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
            .map(ChatRoomDto::from)
            .collect(Collectors.toList());
    }
}