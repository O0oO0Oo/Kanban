package com.kanban.auth.controller;

import com.kanban.auth.dto.UserLoginRequest;
import com.kanban.auth.service.AuthService;
import com.kanban.common.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public Response<String> login(@Valid @RequestBody UserLoginRequest userLoginRequest) {
        return authService.login(userLoginRequest);
    }

    @PostMapping("/access")
    public Response<String> getAccessKey(Authentication authentication) {
        return authService.getAccessKey(authentication);
    }
}
