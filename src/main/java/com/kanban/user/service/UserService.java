package com.kanban.user.service;

import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import com.kanban.user.dto.UserSignupRequest;
import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public Response<Void> addUser(UserSignupRequest request) {
        User user = User.builder()
                .account(request.account())
                .password(passwordEncoder.encode(request.password()))
                .build();

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
        }

        return Response.httpStatusVoid(HttpStatus.CREATED);
    }
}
