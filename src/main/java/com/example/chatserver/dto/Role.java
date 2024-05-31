package com.example.chatserver.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("관리자"), ROLE_USER("사용자");
    private final String description;
}
