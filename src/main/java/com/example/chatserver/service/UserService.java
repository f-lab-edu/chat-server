package com.example.chatserver.service;


import com.example.chatserver.dto.Role;
import com.example.chatserver.dto.SignInForm;
import com.example.chatserver.dto.SignUpForm;
import com.example.chatserver.dto.TokenDto;
import com.example.chatserver.exception.CustomException;
import com.example.chatserver.exception.ErrorCode;
import com.example.chatserver.model.User;
import com.example.chatserver.repository.UserRepository;
import com.example.chatserver.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public Boolean signUpUser(SignUpForm form){
        if(userRepository.existsByEmail(form.getEmail())){
            User user = userRepository.findByEmail(form.getEmail())
                .orElseThrow(IllegalArgumentException::new);
            if(user.isDeleted()){
                throw new CustomException(ErrorCode.ACCOUNT_DELETION_IN_PROGRESS);
            }
            throw new CustomException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }else{
            User build = User.builder()
                .email(form.getEmail())
                .encryptedPassword(passwordEncoder.encode(form.getPassword()))
                .role(Role.ROLE_USER)
                .build();
            userRepository.save(build);
            return true;
        }
    }

    public TokenDto authenticate(SignInForm form){
        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(
            form.getEmail(), form.getPassword());
        try{
            Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(authenticationToken);
            return jwtTokenProvider.generateToken(form.getEmail(), authentication);
        }catch (RuntimeException e){
            throw new CustomException(ErrorCode.ACCOUNT_INFO_MISMATCH);
        }
    }

    @Transactional
    public Boolean deleteUser(String token){
        String email = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
        user.deleted();
        userRepository.save(user);
        return true;
    }
}
