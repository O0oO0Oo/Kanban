package com.kanban.team.dto;

import com.kanban.team.entity.Invite;
import com.kanban.team.enums.Role;

public record FindTeamResponse(
        Long teamId,
        String teamName,
        Role role
) {
    public static FindTeamResponse of(Invite invite) {
        return new FindTeamResponse(
                invite.getTeam().getId(),
                invite.getTeam().getName(),
                invite.getRole()
        );
    }
}
