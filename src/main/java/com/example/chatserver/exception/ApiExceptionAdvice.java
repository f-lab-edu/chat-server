package com.example.chatserver.exception;

import com.example.chatserver.exception.CustomException.CustomExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionAdvice {

    @ExceptionHandler({CustomException.class})

    public ResponseEntity<CustomExceptionResponse> exceptionHandler(final CustomException c){
        return ResponseEntity
            .status(c.getStatus())
            .body(CustomExceptionResponse.builder()
                .message(c.getMessage())
                .code(c.getErrorCode().name())
                .status(c.getStatus()).build());
    }
}
