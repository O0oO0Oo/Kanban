package com.kanban.ticket.dto;

public record RemoveTicketRequest(
        Long teamId,
        Long columnId,
        Long ticketId
) {
}
