package com.kanban.auth.service;

import com.kanban.auth.dto.UserLoginRequest;
import com.kanban.auth.jwt.JwtUtils;
import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final AuthCacheService authCacheService;

    public Response<String> login(UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                userLoginRequest.account(),
                userLoginRequest.password()
        );
        Authentication authenticate = authenticateOrElseThrow(authenticationToken);

        String refreshToken = authCacheService.addRefreshKey(authenticate);
        authCacheService.addAuthorities(authenticate);

        return Response.success(refreshToken);
    }

    public Response<String> getAccessKey(Authentication authentication) {
        String refreshKeyFromCache = authCacheService.findRefreshKey(authentication);
        String refreshKeyFromUser = (String) authentication.getDetails();

        if (refreshKeyFromCache.equals(refreshKeyFromUser)) {
            String authorities = authCacheService.findAuthorities(authentication.getName());
            return Response.success(jwtUtils.generateAccessToken(authentication, authorities));
        }

        throw new JwtException("Invalid Refresh Key.");
    }

    private Authentication authenticateOrElseThrow(UsernamePasswordAuthenticationToken authenticationToken) {
        try {
            return authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new CustomException(ErrorCode.USER_BAD_CREDENTIALS);
        }
    }
}
