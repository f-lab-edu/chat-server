package com.example.chatserver.service;

import com.example.chatserver.dto.SecurityUser;
import com.example.chatserver.exception.CustomException;
import com.example.chatserver.exception.ErrorCode;
import com.example.chatserver.model.User;
import com.example.chatserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.EMAIL_NOT_FOUND));
        return new SecurityUser(user);
    }
}
