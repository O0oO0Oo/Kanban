package com.kanban.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequest(
        @NotBlank(message = "계정 이름은 필수 입니다.")
        String account,
        @NotBlank(message = "비밀번호는 필수 입니다.")
        String password
) {
}
