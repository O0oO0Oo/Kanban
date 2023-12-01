package com.kanban.ticket.dto;

import com.kanban.ticket.enums.Tag;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AddTicketRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long columnId,
        @NotNull(message = "은 필수 입니다.")
        String title,
        @NotNull(message = "는 필수 입니다. [FRONTEND, BACKEND, DESIGN, QA, PM, DOCUMENT]")
        Tag tag,
        @NotNull(message = "은 필수 입니다.")
        Double workTime,
        @NotNull(message = "은 필수 입니다.")
        LocalDate deadline,
        String account
) {
}
