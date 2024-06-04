package com.example.chatserver.dto;

import com.example.chatserver.model.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;

    private String name;

    public static ChatRoomDto from(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
            .id(chatRoom.getId())
            .name(chatRoom.getName())
            .build();
    }
}
