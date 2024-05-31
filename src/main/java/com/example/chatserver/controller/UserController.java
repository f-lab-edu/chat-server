package com.example.chatserver.controller;

import com.example.chatserver.dto.SignInForm;
import com.example.chatserver.dto.SignUpForm;
import com.example.chatserver.dto.TokenDto;
import com.example.chatserver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Boolean> signUp(@RequestBody SignUpForm form){
        return ResponseEntity.ok(userService.signUpUser(form));
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<TokenDto> signIn(@RequestBody SignInForm form){
        return ResponseEntity.ok(userService.authenticate(form));
    }

    @DeleteMapping("/user")
    public ResponseEntity<Boolean> deleteUser(@RequestHeader(name = "X-AUTH-TOKEN") String token) {
        return ResponseEntity.ok(userService.deleteUser(token));
    }
}
