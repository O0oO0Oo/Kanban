package com.kanban.auth.filter;

import com.kanban.auth.jwt.JwtUtils;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token;
        String username;
        Collection<? extends GrantedAuthority> grantedAuthorities;

        if (isNotValidAuthHeader(authHeader)) {
            SecurityContextHolder.clearContext();
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        try {
            if (jwtUtils.isTokenValid(token)) {
                username = jwtUtils.extractUserName(token);
                grantedAuthorities = jwtUtils.extractAuthorities(token);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
                authenticationToken.setDetails(token);

                context.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(context);

                filterChain.doFilter(request, response);
            }
        } catch (ExpiredJwtException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new CustomException(ErrorCode.EXPIRED_TOKEN)
            );
        } catch (JwtException | IllegalArgumentException e) {
            handlerExceptionResolver.resolveException(
                    request, response, null, new CustomException(ErrorCode.INVALID_TOKEN)
            );
        }
    }

    private boolean isNotValidAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }
}
