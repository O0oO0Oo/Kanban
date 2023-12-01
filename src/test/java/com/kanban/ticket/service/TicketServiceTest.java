package com.kanban.ticket.service;

import com.kanban.column.entity.BoardColumn;
import com.kanban.column.repository.BoardColumnRepository;
import com.kanban.common.dto.Response;
import com.kanban.ticket.dto.AddTicketRequest;
import com.kanban.ticket.dto.ModifyTicketFiledRequest;
import com.kanban.ticket.dto.ModifyTicketOrderRequest;
import com.kanban.ticket.dto.RemoveTicketRequest;
import com.kanban.ticket.entity.Ticket;
import com.kanban.ticket.enums.Tag;
import com.kanban.ticket.repository.TicketRepository;
import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BoardColumnRepository boardColumnRepository;

    @InjectMocks
    private TicketService ticketService;

    @Test
    @DisplayName("addTicket - 성공 - 담당 있음")
    void should_successAddTicket_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        String title = "testTicket";
        Tag tag = Tag.PM;
        Double workTime = 3.5;
        LocalDate deadline = LocalDate.of(2023, 12, 30);
        String account = "testUser";
        AddTicketRequest request = new AddTicketRequest(
                teamId,
                columnId,
                title,
                tag,
                workTime,
                deadline,
                account
        );

        BoardColumn column = mock(BoardColumn.class);
        User user = mock(User.class);
        Ticket ticket = mock(Ticket.class);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(column));
        when(userRepository.findUserByAccount(request.account()))
                .thenReturn(Optional.of(user));
        when(ticketRepository.countByBoardColumnId(column.getId()))
                .thenReturn(5);
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // when
        Response<Void> response = ticketService.addTicket(request);

        // then
        assertThat(Response.successVoid()).isEqualTo(response);
    }

    @Test
    @DisplayName("addTicket - 성공 - 담당 없음")
    void should_successAddTicket_when_validRequestNullUser() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        String title = "testTicket";
        Tag tag = Tag.PM;
        Double workTime = 3.5;
        LocalDate deadline = LocalDate.of(2023, 12, 30);
        String account = null;
        AddTicketRequest request = new AddTicketRequest(
                teamId,
                columnId,
                title,
                tag,
                workTime,
                deadline,
                account
        );

        BoardColumn column = mock(BoardColumn.class);
        Ticket ticket = mock(Ticket.class);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(column));
        when(ticketRepository.countByBoardColumnId(column.getId()))
                .thenReturn(5);
        when(ticketRepository.save(any(Ticket.class)))
                .thenReturn(ticket);

        // when
        Response<Void> response = ticketService.addTicket(request);

        // then
        assertThat(Response.successVoid()).isEqualTo(response);
    }
    
    @Test
    @DisplayName("modifyTicketField - 성공 - 담담 있음")
    void should_successModifyTicketField_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        Long ticketId = 1L;
        String title = "변경된 타이틀";
        Tag tag = Tag.PM;
        Double workTime = 3.5;
        LocalDate deadline = LocalDate.of(2023, 12, 30);
        String account = "test";
        ModifyTicketFiledRequest request = new ModifyTicketFiledRequest(teamId, columnId, ticketId, title, tag, workTime, deadline, account);

        BoardColumn column = mock(BoardColumn.class);
        ReflectionTestUtils.setField(column, "id", columnId);
        Ticket ticket = mock(Ticket.class);
        User user = mock(User.class);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(column));
        when(ticketRepository.findTicketByBoardColumnIdAndId(column.getId(), request.ticketId()))
                .thenReturn(Optional.of(ticket));
        when(userRepository.findUserByAccount(request.account()))
                .thenReturn(Optional.of(user));
        when(ticketRepository.save(ticket))
                .thenReturn(ticket);

        // when
        Response<Void> response = ticketService.modifyTicketField(request);

        // then
        assertThat(Response.successVoid()).isEqualTo(response);
    }

    @Test
    @DisplayName("modifyTicketOrder - 성공")
    void should_successModifyTicketOrder_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        Long orderATicketId = 1L;
        Long orderBTicketId = 2L;
        ModifyTicketOrderRequest request = new ModifyTicketOrderRequest(teamId, columnId, orderATicketId, orderBTicketId);

        int ticketAOrder = 1;
        int ticketBOrder = 3;
        BoardColumn column = mock(BoardColumn.class);
        ReflectionTestUtils.setField(column, "id", columnId);

        Ticket ticketA = spy(Ticket.class);
        ticketA.setId(orderATicketId);
        ticketA.setOrderNumber(ticketAOrder);

        Ticket ticketB = spy(Ticket.class);
        ticketB.setId(orderBTicketId);
        ticketB.setOrderNumber(ticketBOrder);

        List<Ticket> tickets = List.of(ticketA, ticketB);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(column));
        when(ticketRepository.findTicketByBoardColumnIdAndId(column.getId(), request.orderATicketId()))
                .thenReturn(Optional.of(ticketA));
        when(ticketRepository.findTicketByBoardColumnIdAndId(column.getId(), request.orderBTicketId()))
                .thenReturn(Optional.of(ticketB));
        when(ticketRepository.saveAll(tickets))
                .thenReturn(tickets);

        // when
        Response<Void> response = ticketService.modifyTicketOrder(request);

        // then
        assertThat(3).isEqualTo(ticketA.getOrderNumber());
    }

    @Test
    @DisplayName("removeTicket - 성공")
    void should_successRemoveTicket_when_validRequest() {
        // given
        Long teamId = 1L;
        Long columnId = 1L;
        Long ticketId = 1L;

        RemoveTicketRequest request = new RemoveTicketRequest(teamId, columnId, ticketId);

        BoardColumn column = mock(BoardColumn.class);
        ReflectionTestUtils.setField(column, "id", columnId);

        Ticket ticket = spy(Ticket.class);
        ticket.setOrderNumber(3);

        Ticket ticket1 = spy(Ticket.class);
        ticket1.setOrderNumber(1);
        Ticket ticket2 = spy(Ticket.class);
        ticket1.setOrderNumber(2);
        Ticket ticket3 = spy(Ticket.class);
        ticket1.setOrderNumber(4);
        Ticket ticket4 = spy(Ticket.class);
        ticket1.setOrderNumber(5);
        Ticket ticket5 = spy(Ticket.class);
        ticket1.setOrderNumber(6);
        List<Ticket> tickets = List.of(ticket1, ticket2, ticket3, ticket4, ticket5);

        Ticket ticket6 = spy(Ticket.class);
        ticket1.setOrderNumber(1);
        Ticket ticket7 = spy(Ticket.class);
        ticket1.setOrderNumber(2);
        Ticket ticket8 = spy(Ticket.class);
        ticket1.setOrderNumber(3);
        Ticket ticket9 = spy(Ticket.class);
        ticket1.setOrderNumber(4);
        Ticket ticket10 = spy(Ticket.class);
        ticket1.setOrderNumber(5);
        List<Ticket> returnTickets = List.of(ticket6, ticket7, ticket8, ticket9, ticket10);

        when(boardColumnRepository.findBoardColumnByTeamIdAndId(request.teamId(), request.columnId()))
                .thenReturn(Optional.of(column));
        when(ticketRepository.findTicketByBoardColumnIdAndId(column.getId(), request.ticketId()))
                .thenReturn(Optional.of(ticket));
        doNothing().when(ticketRepository).delete(ticket);
        when(column.getTickets())
                .thenReturn(tickets);
        when(ticketRepository.saveAll(tickets))
                .thenReturn(returnTickets);

        // when
        Response<Void> response = ticketService.removeTicket(request);

        // then
        assertThat(3).isEqualTo(ticket3.getOrderNumber());
    }
}