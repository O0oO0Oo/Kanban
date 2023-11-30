package com.kanban.column.dto;

public record ModifyBoardColumnNameRequest(
        Long teamId,
        Long columnId,
        String columnName
) {
}
