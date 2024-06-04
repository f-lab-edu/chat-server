package com.example.chatserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.chatserver.dto.ChatDto;
import com.example.chatserver.dto.ChatMessageDto;
import com.example.chatserver.dto.ChatRoomDto;
import com.example.chatserver.dto.MessageType;
import com.example.chatserver.model.ChatMessage;
import com.example.chatserver.model.ChatRoom;
import com.example.chatserver.repository.ChatMessageRepository;
import com.example.chatserver.repository.ChatRoomRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ChatServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveMessage() {
        // Arrange
        Long chatRoomId = 1L;
        String username = "user1";
        String message = "Hello, world!";
        ChatDto chatDto = new ChatDto(MessageType.TALK,chatRoomId, username, message);

        ChatRoom chatRoom = new ChatRoom();
        ChatRoom.builder().id(chatRoomId).build();

        when(chatRoomRepository.findById(chatRoomId)).thenReturn(Optional.of(chatRoom));

        // Act
        chatService.saveMessage(chatDto);

        // Assert
        verify(chatMessageRepository, times(1)).save(any());
    }

    @Test
    public void testGetMessages() {
        // Arrange
        Long chatRoomId = 1L;
        ChatMessage chatMessage1 = ChatMessage.builder().message("Message 1").build();
        ChatMessage chatMessage2 = ChatMessage.builder().message("Message 2").build();
        List<ChatMessage> expectedMessages = List.of(chatMessage1, chatMessage2);

        when(chatMessageRepository.findTopMessagesByChatRoomId(chatRoomId))
            .thenReturn(expectedMessages);

        // Act
        List<ChatMessageDto> actualMessages = chatService.getMessages(chatRoomId);

        // Assert
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void testCreateChatRoom() {
        // Arrange
        String chatRoomName = "Test Room";
        ChatRoom expectedChatRoom = ChatRoom.builder().name(chatRoomName).build();

        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(expectedChatRoom);

        // Act
        ChatRoom createdChatRoom = chatService.createChatRoom(chatRoomName);

        // Assert
        assertEquals(expectedChatRoom, createdChatRoom);
    }

    @Test
    public void testGetAllChatRooms() {
        // Arrange
        ChatRoom chatRoom1 = ChatRoom.builder().name("Room 1").build();
        ChatRoom chatRoom2 = ChatRoom.builder().name("Room 2").build();
        List<ChatRoom> expectedChatRooms = new ArrayList<>();
        expectedChatRooms.add(chatRoom1);
        expectedChatRooms.add(chatRoom2);

        when(chatRoomRepository.findAll()).thenReturn(expectedChatRooms);

        // Act
        List<ChatRoomDto> actualChatRooms = chatService.getAllChatRooms();

        // Assert
        assertEquals(expectedChatRooms, actualChatRooms);
    }
}