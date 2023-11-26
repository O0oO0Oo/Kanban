package com.kanban.auth.jwt;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface JwtUtils {
    String extractUserName(String token);
    Collection<? extends GrantedAuthority> extractAuthorities(String token);
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    boolean isTokenValid(String token);
}
