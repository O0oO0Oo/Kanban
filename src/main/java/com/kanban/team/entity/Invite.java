package com.kanban.team.entity;


import com.kanban.team.enums.Role;
import com.kanban.user.entity.User;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    private boolean accept;

    @Builder
    public Invite(User user, Team team, Role role, boolean accept) {
        this.user = user;
        this.team = team;
        this.role = role;
        this.accept = accept;
    }
}