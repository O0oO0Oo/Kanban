package com.kanban.user.controller;

import com.kanban.common.dto.Response;
import com.kanban.user.dto.UserSignupRequest;
import com.kanban.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Response<Void> signup(@Valid @RequestBody UserSignupRequest request) {
        return userService.addUser(request);
    }
}
