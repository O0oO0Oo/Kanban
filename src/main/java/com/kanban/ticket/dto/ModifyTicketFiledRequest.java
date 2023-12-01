package com.kanban.ticket.dto;

import com.kanban.ticket.enums.Tag;

import java.time.LocalDate;

public record ModifyTicketFiledRequest(
        Long teamId,
        Long columnId,
        Long ticketId,
        String title,
        Tag tag,
        Double workTime,
        LocalDate deadline,
        String account
) {
}
