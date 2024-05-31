package com.example.chatserver.security;

import com.example.chatserver.dto.TokenDto;
import com.example.chatserver.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    @Value("${spring.jwt.secret}")
    private String SECRETE_KEY;
    private static final String KEY_ROLE = "role";
    private static final long ACCESS_TOKEN_DURATION_IN_SECOND = 1000*60*60;
    private static final long REFRESH_TOKEN_DURATION_IN_SECOND = 1000*60*60*24;
    private final UserDetailsServiceImpl userDetailsService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.SECRETE_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public TokenDto generateToken(String email, Authentication authentication) {
        String role = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
            .toString();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put(KEY_ROLE, role);

        long now = (new Date()).getTime();
        String accessToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(new Date(now + ACCESS_TOKEN_DURATION_IN_SECOND))
            .signWith(this.getSigningKey())
            .compact();

        String refreshToken = Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(new Date(now + REFRESH_TOKEN_DURATION_IN_SECOND))
            .signWith(this.getSigningKey())
            .compact();

        return TokenDto.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public Authentication getAuthentication(String accessToken) {
        String subject = parseClaims(accessToken).getSubject();
        UserDetails username = userDetailsService.loadUserByUsername(subject);
        return new UsernamePasswordAuthenticationToken(username, accessToken, username.getAuthorities());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(this.getSigningKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.getSubject(); // assuming the subject is the email
    }
}
