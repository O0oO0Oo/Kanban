package com.kanban.team.dto;

import com.kanban.team.entity.Invite;
import jakarta.validation.constraints.NotNull;

public record AddInviteRequest(
        @NotNull
        String inviteUserAccount,
        @NotNull
        Long teamId
) {
}
