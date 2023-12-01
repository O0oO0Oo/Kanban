package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyBoardColumnOrderRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long orderAColumnId,
        @NotNull(message = "는 필수 입니다.")
        Long orderBColumnId
) {
}
