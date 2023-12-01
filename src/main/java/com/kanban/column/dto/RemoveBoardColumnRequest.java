package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveBoardColumnRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long columnId
) {
}
