package com.kanban.ticket.controller;

import com.kanban.common.dto.Response;
import com.kanban.ticket.dto.AddTicketRequest;
import com.kanban.ticket.dto.ModifyTicketFiledRequest;
import com.kanban.ticket.dto.ModifyTicketOrderRequest;
import com.kanban.ticket.dto.RemoveTicketRequest;
import com.kanban.ticket.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/board/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> addTicket(@Valid @RequestBody AddTicketRequest request) {
        return ticketService.addTicket(request);
    }

    @PutMapping("/field")
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> modifyTicketField(@Valid @RequestBody ModifyTicketFiledRequest request) {
        return ticketService.modifyTicketField(request);
    }

    @PutMapping("/order")
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> modifyTicketOrder(@Valid @RequestBody ModifyTicketOrderRequest request) {
        return ticketService.modifyTicketOrder(request);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority(#request.teamId() + '_LEADER', #request.teamId() + '_MEMBER')")
    public Response<Void> removeTicket(@Valid @RequestBody RemoveTicketRequest request) {
        return ticketService.removeTicket(request);
    }
}
