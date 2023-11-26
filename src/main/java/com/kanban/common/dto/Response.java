package com.kanban.common.dto;

import org.springframework.http.HttpStatus;

public record Response<T>(
        int status,
        String message,
        T data
) {
    public static <T> Response<T> success(T data) {
        return new Response<>(HttpStatus.OK.value(), "Success", data);
    }

    public static Response<Void> successVoid() {
        return new Response<>(HttpStatus.OK.value(), "Success", null);
    }

    public static Response<Void> httpStatusVoid(HttpStatus httpStatus) {
        return new Response<>(httpStatus.value(), httpStatus.getReasonPhrase(), null);
    }
}
