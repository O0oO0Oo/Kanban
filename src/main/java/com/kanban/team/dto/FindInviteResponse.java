package com.kanban.team.dto;

import com.kanban.team.entity.Invite;

public record FindInviteResponse(
        Long inviteId,
        String teamName
) {
    public static FindInviteResponse of(Invite invite) {
        return new FindInviteResponse(
                invite.getId(),
                invite.getTeam().getName()
        );
    }
}
