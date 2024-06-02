package com.example.chatserver.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignInForm {
    private String email;
    private String password;
}
