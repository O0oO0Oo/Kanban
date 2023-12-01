package com.kanban.column.dto;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ModifyBoardColumnNameRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long columnId,
        @NotNull(message = "는 필수 입니다.")
        @Length(min = 4, message = "4글자 이상 입력하세요.")
        String columnName
) {
}
