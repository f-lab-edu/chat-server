package com.example.chatserver.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.sql.Timestamp;
import java.util.TimeZone;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@Setter
@ToString
public class ChatDto {
    @Enumerated(EnumType.STRING)
    private MessageType type;
    private Long chatRoomId;
    private String username;
    private String message;
    private Timestamp timestamp;

    public ChatDto() {
        long currentTimeMillis = System.currentTimeMillis();

        TimeZone timeZone = TimeZone.getDefault();

        this.timestamp = new Timestamp(currentTimeMillis + timeZone.getRawOffset() + TimeZone.getTimeZone("GMT+09:00").getRawOffset());
    }
}
