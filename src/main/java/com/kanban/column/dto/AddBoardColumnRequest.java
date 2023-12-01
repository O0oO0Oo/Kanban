package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AddBoardColumnRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "은 필수 입니다.")
        @Length(min = 4, message = "은 4글자 이상이어야 합니다.")
        String columnName
) {
}
