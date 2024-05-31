package com.example.chatserver.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    EMAIL_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "등록된 이메일입니다."),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    ACCOUNT_INFO_MISMATCH(HttpStatus.BAD_REQUEST, "계정정보가 일치하지 않습니다"),
    ACCOUNT_DELETION_IN_PROGRESS(HttpStatus.BAD_REQUEST, "탈퇴 진행중인 계정입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
