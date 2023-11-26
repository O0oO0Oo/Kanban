package com.kanban.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

public record UserSignupRequest(
        @NotBlank(message = "계정명을 입력하세요")
        @Length(min = 6, message = "계정은 6글자 이상입니다.")
        String account,

        @NotBlank(message = "비밀번호를 입력하세요.")
        @Length(min = 8, message = "비밀번호는 8자 이상입니다.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z!@#$%^&*()-_=+]).+$", message = "비밀번호는 숫자, 문자, 특수문자 중 2가지 이상을 포함해야 합니다.")
        String password
) {
}