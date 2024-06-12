package com.example.chatserver.service;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.ChatMessageDto;
import com.example.chatserver.dto.ChatRoomDto;
import com.example.chatserver.model.ChatMessage;
import com.example.chatserver.model.ChatRoom;
import com.example.chatserver.repository.ChatMessageRepository;
import com.example.chatserver.repository.ChatRoomRepository;
import java.util.ArrayList;
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
            .timestamp(chat.getTimestamp()).build();

        chatMessageRepository.save(chatMessage);
    }
    public void saveMessages(List<ChatDto> chatDtos) {

        List<ChatMessage> chatMessages = new ArrayList<>();
        for (ChatDto chatDto : chatDtos) {
            ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getChatRoomId())
                .orElseThrow(IllegalArgumentException::new);

            ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .type(chatDto.getType())
                .sender(chatDto.getUsername())
                .message(chatDto.getMessage())
                .timestamp(chatDto.getTimestamp())
                .build();

            chatMessages.add(chatMessage);
        }
        chatMessageRepository.saveAll(chatMessages);
    }

    public List<ChatMessageDto> getMessages(Long chatRoomId) {
        return chatMessageRepository.findTopMessagesByChatRoomId(chatRoomId)
            .stream().map(ChatMessageDto::from)
            .collect(Collectors.toList());
    }

    public ChatRoomDto createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
            .name(name).build();
        chatRoomRepository.save(chatRoom);
        return ChatRoomDto.from(chatRoom);
    }

    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
            .map(ChatRoomDto::from)
            .collect(Collectors.toList());
    }
}