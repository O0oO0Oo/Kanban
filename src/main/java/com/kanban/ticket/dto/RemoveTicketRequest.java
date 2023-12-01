package com.kanban.ticket.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveTicketRequest(
        @NotNull(message = "는 필수 입니다.")
        Long teamId,
        @NotNull(message = "는 필수 입니다.")
        Long columnId,
        @NotNull(message = "는 필수 입니다.")
        Long ticketId
) {
}
