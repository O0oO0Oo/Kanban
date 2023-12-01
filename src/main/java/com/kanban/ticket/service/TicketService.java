package com.kanban.ticket.service;

import com.kanban.column.entity.BoardColumn;
import com.kanban.column.repository.BoardColumnRepository;
import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import com.kanban.ticket.dto.AddTicketRequest;
import com.kanban.ticket.dto.ModifyTicketFiledRequest;
import com.kanban.ticket.dto.ModifyTicketOrderRequest;
import com.kanban.ticket.dto.RemoveTicketRequest;
import com.kanban.ticket.entity.Ticket;
import com.kanban.ticket.repository.TicketRepository;
import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Transactional
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BoardColumnRepository boardColumnRepository;

    public Response<Void> addTicket(AddTicketRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());
        User user = findUserByAccountOrElseThrow(request.account());

        int order = ticketRepository.countByBoardColumnId(boardColumn.getId()) + 1;
        Ticket ticket = Ticket.builder()
                .orderNumber(order)
                .title(request.title())
                .tag(request.tag())
                .workTime(request.workTime())
                .deadline(request.deadline())
                .boardColumn(boardColumn)
                .user(user)
                .build();
        ticketRepository.save(ticket);

        return Response.successVoid();
    }

    public Response<Void> modifyTicketField(ModifyTicketFiledRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());
        Ticket ticket = findTicketByBoardColumnIdAndTicketIdOrElseThrow(boardColumn.getId(), request.ticketId());

        User user = findUserByAccountOrElseThrow(request.account());

        ticket.setTitle(request.title());
        ticket.setTag(request.tag());
        ticket.setWorkTime(request.workTime());
        ticket.setDeadline(request.deadline());
        ticket.setUser(user);

        ticketRepository.save(ticket);
        return Response.successVoid();
    }

    public Response<Void> modifyTicketOrder(ModifyTicketOrderRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());
        Ticket ticketA = findTicketByBoardColumnIdAndTicketIdOrElseThrow(boardColumn.getId(), request.orderATicketId());
        Ticket ticketB = findTicketByBoardColumnIdAndTicketIdOrElseThrow(boardColumn.getId(), request.orderBTicketId());

        int temp = ticketA.getOrderNumber();
        ticketA.setOrderNumber(ticketB.getOrderNumber());
        ticketB.setOrderNumber(temp);

        ticketRepository.saveAll(List.of(ticketA, ticketB));
        return Response.successVoid();
    }

    public Response<Void> removeTicket(RemoveTicketRequest request) {
        BoardColumn boardColumn = findBoardColumnByTeamIdAndIdOrElseThrow(request.teamId(), request.columnId());
        Ticket ticket = findTicketByBoardColumnIdAndTicketIdOrElseThrow(boardColumn.getId(), request.ticketId());
        ticketRepository.delete(ticket);

        List<Ticket> tickets = boardColumn.getTickets();
        IntStream.range(0, tickets.size())
                .forEach(index -> tickets.get(index).setOrderNumber(index + 1));

        ticketRepository.saveAll(tickets);
        return Response.successVoid();
    }

    private User findUserByAccountOrElseThrow(String account) {
        if (account != null) {
            return userRepository.findUserByAccount(account)
                    .orElseThrow(
                            () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
                    );
        }
        return null;
    }

    private BoardColumn findBoardColumnByTeamIdAndIdOrElseThrow(Long teamId, Long columnId) {
        return boardColumnRepository.findBoardColumnByTeamIdAndId(teamId, columnId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.BOARD_COLUMN_NOT_FOUND)
                );
    }

    private Ticket findTicketByBoardColumnIdAndTicketIdOrElseThrow(Long boardColumnId, Long ticketId) {
        return ticketRepository.findTicketByBoardColumnIdAndId(boardColumnId, ticketId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.TICKET_NOT_FOUND)
                );
    }
}
