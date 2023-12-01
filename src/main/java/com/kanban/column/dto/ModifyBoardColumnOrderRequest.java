package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;

public record ModifyBoardColumnOrderRequest(
        @NotNull
        Long teamId,
        @NotNull
        Long orderAColumnId,
        @NotNull
        Long orderBColumnId
) {
}
