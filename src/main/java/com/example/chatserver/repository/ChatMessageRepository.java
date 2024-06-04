package com.example.chatserver.repository;

import com.example.chatserver.model.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop100ByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);
}
