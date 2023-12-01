package com.kanban.ticket.dto;

import com.kanban.ticket.enums.Tag;

import java.time.LocalDate;

public record ModifyTicketOrderRequest(
        Long teamId,
        Long columnId,
        Long orderATicketId,
        Long orderBTicketId
) {
}
