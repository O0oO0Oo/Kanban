package com.kanban.ticket.repository;

import com.kanban.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    int countByBoardColumnId(Long boardColumnId);

    Optional<Ticket> findTicketByBoardColumnIdAndId(Long boardColumnId, Long ticketId);
}
