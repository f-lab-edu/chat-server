package com.example.chatserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.chatserver.dto.SignUpForm;
import com.example.chatserver.exception.CustomException;
import com.example.chatserver.exception.ErrorCode;
import com.example.chatserver.model.User;
import com.example.chatserver.repository.UserRepository;
import com.example.chatserver.security.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        ReflectionTestUtils.setField(authenticationManagerBuilder, "object", authenticationManager);
    }

    @Test
    @Transactional
    public void testSignUpUser_Success() {
        SignUpForm form = new SignUpForm("test@example.com", "password");

        when(userRepository.existsByEmail(form.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(form.getPassword())).thenReturn("encodedPassword");

        Boolean result = userService.signUpUser(form);

        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @Transactional
    public void testSignUpUser_EmailAlreadyRegistered() {
        SignUpForm form = new SignUpForm("test@example.com", "password");
        User existingUser = new User();

        when(userRepository.existsByEmail(form.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(form.getEmail())).thenReturn(Optional.of(existingUser));

        CustomException exception = assertThrows(CustomException.class, () -> userService.signUpUser(form));

        assertEquals(ErrorCode.EMAIL_ALREADY_REGISTERED, exception.getErrorCode());
    }

    @Test
    @Transactional
    public void testDeleteUser_Success() {
        String token = "token";
        String email = "test@example.com";
        User user = User.builder().email(email).build();

        when(jwtTokenProvider.getEmailFromToken(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Boolean result = userService.deleteUser(token);

        assertTrue(result);
        assertTrue(user.isDeleted());
        verify(userRepository, times(1)).save(user);
    }
}
