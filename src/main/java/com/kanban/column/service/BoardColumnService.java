package com.kanban.column.service;

import com.kanban.column.dto.*;
import com.kanban.column.entity.BoardColumn;
import com.kanban.column.repository.BoardColumnRepository;
import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import com.kanban.team.entity.Team;
import com.kanban.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final TeamRepository teamRepository;

    public Response<FindBoardColumnResponse> findAllBoardColumn(Long teamId) {
        Team team = findTeamByIdOrElseThrow(teamId);
        List<BoardColumn> boardColumns = boardColumnRepository.findByTeamOrderByOrderNumber(team);

        FindBoardColumnResponse findBoardColumnResponse = new FindBoardColumnResponse(team.getId(), boardColumns);
        return Response.success(findBoardColumnResponse);
    }

    @Transactional
    public Response<Void> addBoardColumn(AddBoardColumnRequest request) {
        Team team = findTeamByIdOrElseThrow(request.teamId());

        int order = boardColumnRepository.countByTeam(team) + 1;
        BoardColumn boardColumn = BoardColumn.builder()
                .team(team)
                .orderNumber(order)
                .name(request.columnName())
                .build();
        boardColumnRepository.save(boardColumn);

        return Response.successVoid();
    }

    @Transactional
    public Response<Void> modifyBoardColumnOrder(ModifyBoardColumnOrderRequest request) {
        BoardColumn boardColumnA = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.orderAColumnId());
        BoardColumn boardColumnB = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.orderBColumnId());

        int temp = boardColumnB.getOrderNumber();
        boardColumnB.setOrderNumber(boardColumnA.getOrderNumber());
        boardColumnA.setOrderNumber(temp);
        boardColumnRepository.saveAll(List.of(boardColumnA, boardColumnB));

        return Response.successVoid();
    }

    @Transactional
    public Response<Void> modifyBoardColumnName(ModifyBoardColumnNameRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());

        boardColumn.setName(request.columnName());
        boardColumnRepository.save(boardColumn);

        return Response.successVoid();
    }

    @Transactional
    public Response<FindBoardColumnResponse> removeBoardColumn(RemoveBoardColumnRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());
        boardColumnRepository.delete(boardColumn);

        Team team = findTeamByIdOrElseThrow(request.teamId());
        List<BoardColumn> boardColumns = boardColumnRepository.findByTeamOrderByOrderNumber(team);
        IntStream.range(0, boardColumns.size())
                .forEach(index -> boardColumns.get(index).setOrderNumber(index + 1));
        boardColumnRepository.saveAll(boardColumns);

        FindBoardColumnResponse findBoardColumnResponse = new FindBoardColumnResponse(team.getId(), boardColumns);
        return Response.success(findBoardColumnResponse);
    }

    private Team findTeamByIdOrElseThrow(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.TEAM_NOT_FOUND)
                );
    }

    private BoardColumn findBoardColumnByTeamIdAndIdOrElseThrow(Long teamId, Long columnId) {
        return boardColumnRepository.findBoardColumnByTeamIdAndId(teamId, columnId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.BOARD_COLUMN_NOT_FOUND)
                );
    }
}