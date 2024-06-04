package com.example.chatserver.repository;

import com.example.chatserver.model.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "SELECT * FROM chat_message WHERE chat_room_id = :chatRoomId ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<ChatMessage> findMessagesByChatRoomId(@Param("chatRoomId") Long chatRoomId, @Param("limit") int limit);

    default List<ChatMessage> findTopMessagesByChatRoomId(Long chatRoomId) {
        final int DEFAULT_MESSAGE_FETCH_LIMIT = 100;
        return findMessagesByChatRoomId(chatRoomId, DEFAULT_MESSAGE_FETCH_LIMIT);
    }
}
