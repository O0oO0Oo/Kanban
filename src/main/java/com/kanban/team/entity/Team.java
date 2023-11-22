package com.kanban.team.entity;

import com.kanban.column.entity.BoardColumn;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "team_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Invite> invites;

    @OneToMany(mappedBy = "team",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<BoardColumn> boardColumns;

    @Builder
    public Team(String name) {
        this.name = name;
    }
}
