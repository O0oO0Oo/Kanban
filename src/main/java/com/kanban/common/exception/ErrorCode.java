package com.kanban.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //User
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "U001", "이미 계정이 존재합니다."),
    USER_BAD_CREDENTIALS(HttpStatus.NOT_FOUND, "U002", "아이디 또는 패스워드가 틀립니다."),
    USER_ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "U003", "존재하지 않는 유저 입니다."),

    //Team
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "T001", "존재하지 않는 팀 입니다."),

    //Invite
    INVITE_NOT_FOUND(HttpStatus.NOT_FOUND, "I001", "존재하지 않는 초대 입니다."),
    INVITE_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "I002", "이미 초대를 하였거나 팀의 멤버입니다."),

    //auth
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A001", "잘못된 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A002", "만료된 토큰입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
