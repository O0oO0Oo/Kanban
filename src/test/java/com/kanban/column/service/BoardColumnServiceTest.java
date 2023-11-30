package com.kanban.column.service;

import com.kanban.column.dto.*;
import com.kanban.column.entity.BoardColumn;
import com.kanban.column.repository.BoardColumnRepository;
import com.kanban.common.dto.Response;
import com.kanban.team.entity.Team;
import com.kanban.team.repository.TeamRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardColumnServiceTest {

    @Mock
    private BoardColumnRepository boardColumnRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private BoardColumnService boardColumnService;

    @Test
    @DisplayName("findAllBoardColumn - 성공")
    void should_successFindAllBoardColumn_when_validRequest() {
        // given
        Long teamId = 1L;
        Team team = mock(Team.class);
        ReflectionTestUtils.setField(team,"id",1L);

        BoardColumn boardColumn1 = mock(BoardColumn.class);
        BoardColumn boardColumn2 = mock(BoardColumn.class);
        BoardColumn boardColumn3 = mock(BoardColumn.class);
        List<BoardColumn> boardColumns = List.of(boardColumn1, boardColumn2, boardColumn3);

        FindBoardColumnResponse findBoardColumnResponse = new FindBoardColumnResponse(team.getId(), boardColumns);

        when(teamRepository.findById(teamId))
                .thenReturn(Optional.of(team));
        when(boardColumnRepository.findByTeamOrderByOrderNumber(team))
                .thenReturn(boardColumns);

        // when
        Response<FindBoardColumnResponse> result = boardColumnService.findAllBoardColumn(teamId);

        // then
        assertThat(boardColumns).isEqualTo(result.data().boardColumns());
    }
    
    @Test
    @DisplayName("addBoardColumn - 성공")
    void should_successAddBoardColumn_when_validRequest() {
        // given
        AddBoardColumnRequest request = new AddBoardColumnRequest(1L, "testColumn");

        Team team = mock(Team.class);
        int teamSize = 10;

        BoardColumn boardColumn = mock(BoardColumn.class);

        when(teamRepository.findById(request.teamId()))
                .thenReturn(Optional.of(team));
        when(boardColumnRepository.countByTeam(team))
                .thenReturn(teamSize);
        when(boardColumnRepository.save(any(BoardColumn.class)))
                .thenReturn(boardColumn);

        Response<Void> actualResponse = Response.successVoid();

        // when
        Response<Void> expectResponse = boardColumnService.addBoardColumn(request);

        // then
        assertThat(actualResponse).isEqualTo(expectResponse);
    }
    
    @Test
    @DisplayName("removeBoardColumn - 성공")
    void should_successRemoveBoardColumn_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 2L;
        RemoveBoardColumnRequest request = new RemoveBoardColumnRequest(teamId, columnId);

        BoardColumn boardColumn = mock(BoardColumn.class);
        Team team = mock(Team.class);
        ReflectionTestUtils.setField(team, "id", teamId);

        BoardColumn reorderColumn1 = mock(BoardColumn.class);
        BoardColumn reorderColumn2 = mock(BoardColumn.class);
        BoardColumn reorderColumn3 = mock(BoardColumn.class);
        List<BoardColumn> reorderColumnList = List.of(reorderColumn1, reorderColumn2, reorderColumn3);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(boardColumn));
        doNothing().when(boardColumnRepository).delete(boardColumn);
        when(teamRepository.findById(request.teamId()))
                .thenReturn(Optional.of(team));
        when(boardColumnRepository.findByTeamOrderByOrderNumber(team))
                .thenReturn(reorderColumnList);
        when(boardColumnRepository.saveAll(reorderColumnList))
                .thenReturn(reorderColumnList);

        FindBoardColumnResponse actualResponse = new FindBoardColumnResponse(team.getId(), reorderColumnList);

        // when
        Response<FindBoardColumnResponse> expectResponse = boardColumnService.removeBoardColumn(request);

        // then
        assertThat(actualResponse).isEqualTo(expectResponse.data());
    }

    @Test
    @DisplayName("modifyBoardColumnOrder - 성공")
    void should_successModifyBoardColumnOrder_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnAId = 1L;
        Long columnBId = 2L;
        ModifyBoardColumnOrderRequest request = new ModifyBoardColumnOrderRequest(teamId, columnAId, columnBId);

        int orderA = 1;
        int orderB = 2;
        BoardColumn columnA = mock(BoardColumn.class);
        BoardColumn columnB = mock(BoardColumn.class);
        columnA.setOrderNumber(orderA);
        columnB.setOrderNumber(orderB);
        List<BoardColumn> modifiedColumns = List.of(columnA, columnB);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.orderAColumnId()))
                .thenReturn(Optional.of(columnA));
        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.orderBColumnId()))
                .thenReturn(Optional.of(columnB));
        when(boardColumnRepository.saveAll(modifiedColumns))
                .thenReturn(modifiedColumns);

        Response<Void> actualResponse = Response.successVoid();

        // when
        Response<Void> expectResponse = boardColumnService.modifyBoardColumnOrder(request);

        // then
        assertThat(actualResponse).isEqualTo(expectResponse);
    }
    
    @Test
    @DisplayName("modifyBoardColumnName - 성공")
    void should_successModifyBoardColumnName_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        String columnName = "modifiedName";
        ModifyBoardColumnNameRequest request = new ModifyBoardColumnNameRequest(teamId, columnId, columnName);

        BoardColumn boardColumn = mock(BoardColumn.class);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(boardColumn));
        when(boardColumnRepository.save(boardColumn))
                .thenReturn(boardColumn);

        Response<Void> actualResponse = Response.successVoid();

        // when
        Response<Void> expectResponse = boardColumnService.modifyBoardColumnName(request);

        // then
        assertThat(actualResponse).isEqualTo(expectResponse);
    }
}