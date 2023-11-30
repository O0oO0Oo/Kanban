package com.kanban.column.controller;

import com.kanban.column.dto.*;
import com.kanban.column.service.BoardColumnService;
import com.kanban.common.dto.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/column")
@RequiredArgsConstructor
public class BoardColumnController {

    private final BoardColumnService boardColumnService;

    @GetMapping("/{teamId}")
    @PreAuthorize("hasAnyAuthority(#teamId + '_LEADER', #teamId + '_MEMBER')")
    public Response<FindBoardColumnResponse> findAllBoardColumn(@PathVariable Long teamId) {
        return boardColumnService.findAllBoardColumn(teamId);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority(#addBoardColumnRequest.teamId() + '_LEADER')")
    public Response<Void> addBoardColumn(@Valid @RequestBody AddBoardColumnRequest addBoardColumnRequest) {
        return boardColumnService.addBoardColumn(addBoardColumnRequest);
    }

    @PutMapping("/order")
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> modifyBoardColumnOrder(@Valid @RequestBody ModifyBoardColumnOrderRequest request) {
        return boardColumnService.modifyBoardColumnOrder(request);
    }

    @PutMapping("/name")
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> modifyBoardColumnName(@Valid @RequestBody ModifyBoardColumnNameRequest request) {
        return boardColumnService.modifyBoardColumnName(request);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER')")
    public Response<FindBoardColumnResponse> removeBoardColumn(@Valid @RequestBody RemoveBoardColumnRequest request) {
        return boardColumnService.removeBoardColumn(request);
    }
}
