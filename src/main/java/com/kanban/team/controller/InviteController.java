package com.kanban.team.controller;

import com.kanban.common.dto.Response;
import com.kanban.team.dto.AddInviteRequest;
import com.kanban.team.dto.FindInviteResponse;
import com.kanban.team.service.InviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team/invite")
@RequiredArgsConstructor
public class InviteController {

    private final InviteService inviteService;

    @GetMapping
    public Response<List<FindInviteResponse>> findAllInvite(@AuthenticationPrincipal String principal) {
        return inviteService.findAllInvite(principal);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority(#addInviteRequest.teamId() + '_LEADER')")
    public Response<Void> addInvite(@Valid @RequestBody AddInviteRequest addInviteRequest) {
        return inviteService.addInvite(addInviteRequest);
    }

    @PutMapping("/{inviteId}")
    public Response<Void> modifyInviteAccept(@AuthenticationPrincipal String principal,
                                             @PathVariable Long inviteId) {
        return inviteService.modifyInviteAccept(principal, inviteId);
    }
}
