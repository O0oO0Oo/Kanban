package com.kanban.column.dto;

import com.kanban.column.entity.BoardColumn;

import java.util.List;

public record FindBoardColumnResponse(
        Long teamId,
        List<BoardColumn> boardColumns
) {
}
