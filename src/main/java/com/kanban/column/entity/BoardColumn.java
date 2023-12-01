package com.kanban.column.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kanban.team.entity.Team;
import com.kanban.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_column_id", nullable = false)
    private Long id;

    @Setter
    @Column(nullable = false)
    private int orderNumber;

    @Setter
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "boardColumn",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @org.hibernate.annotations.OrderBy(clause = "orderNumber ASC")
    private List<Ticket> tickets;

    @Builder
    public BoardColumn(int orderNumber, String name, Team team) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.team = team;
    }
}
