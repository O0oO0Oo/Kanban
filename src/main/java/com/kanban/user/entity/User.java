package com.kanban.user.entity;

import com.kanban.team.entity.Invite;
import com.kanban.ticket.entity.Ticket;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Invite> invites;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    @Builder
    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    /**
     * ROLE_{Team id}_{LEADER/MEMBER} Team 에 팀장, 멤버 권한인지
     * @return List ROLE_{Team id}_{LEADER/MEMBER} Team 에 팀장, 멤버 권한인지
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getInvites().stream()
                .map(invite -> {
                            if (invite.isAccept()) {
                                return new SimpleGrantedAuthority(
                                        invite.getTeam().getId() + "_" + invite.getRole().toString()
                                );
                            }
                            return null;
                        }
                )
                .filter(Objects::nonNull)
                .toList();
    }


    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


