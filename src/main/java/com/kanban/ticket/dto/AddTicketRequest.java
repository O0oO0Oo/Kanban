package com.kanban.ticket.dto;

import com.kanban.ticket.enums.Tag;

import java.time.LocalDate;

public record AddTicketRequest(
        Long teamId,
        Long columnId,
        String title,
        Tag tag,
        Double workTime,
        LocalDate deadline,
        String account
) {
}
