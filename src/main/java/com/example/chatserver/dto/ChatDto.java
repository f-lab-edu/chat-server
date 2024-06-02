package com.example.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class ChatDto {
    private Long chatRoomId;
    private String username;
    private String message;

    public void setMessage(String message){
        this.message = message;
    }
}
