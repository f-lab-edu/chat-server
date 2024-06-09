package com.example.chatserver.interceptor;

import com.example.chatserver.security.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@RequiredArgsConstructor
public class WebSocketAuthhInterceptor implements HandshakeInterceptor {

    private final JwtTokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
            HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

            String token = servletRequest.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {

                String email = tokenProvider.getEmailFromToken(token.substring(7));

                attributes.put("email", email);

                return true;
            } else {
                servletResponse.setStatus(401);
                return false;
            }
        }
        return false;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {
    }
}
