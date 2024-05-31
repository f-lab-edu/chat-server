package com.example.chatserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpForm {
    private String email;
    private String password;
}
