package com.kanban.column.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddBoardColumnRequest(
        @NotNull
        Long teamId,
        @NotEmpty
        String columnName
) {
}
