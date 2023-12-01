package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyBoardColumnNameRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long columnId,
        @NotNull(message = "은 필수 입니다.")
        String columnName
) {
}
