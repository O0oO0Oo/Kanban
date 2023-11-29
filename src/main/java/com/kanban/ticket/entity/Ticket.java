package com.kanban.ticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanban.column.entity.BoardColumn;
import com.kanban.ticket.enums.Tag;
import com.kanban.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private int orderNumber;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tag tag;

    @Column(nullable = false)
    private Long workTime;

    @Column(nullable = false)
    private LocalDate deadline;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "board_column_id")
    private BoardColumn boardColumn;

    @Builder
    public Ticket(String title, int orderNumber, Tag tag, Long workTime, LocalDate deadline, User user, BoardColumn boardColumn) {
        this.title = title;
        this.orderNumber = orderNumber;
        this.tag = tag;
        this.workTime = workTime;
        this.deadline = deadline;
        this.user = user;
        this.boardColumn = boardColumn;
    }
}
