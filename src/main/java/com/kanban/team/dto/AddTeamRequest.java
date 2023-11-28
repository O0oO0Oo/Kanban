package com.kanban.team.dto;

import org.hibernate.validator.constraints.Length;

public record AddTeamRequest(
        @Length(min = 6, message = "팀 이름은 최소 6글자 이상입니다.")
        String teamName
) {
}
