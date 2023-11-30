package com.kanban.column.dto;

public record RemoveBoardColumnRequest(
        Long teamId,
        Long columnId
) {
}
