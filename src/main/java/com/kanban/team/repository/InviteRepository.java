package com.kanban.team.repository;

import com.kanban.team.entity.Invite;
import com.kanban.team.entity.Team;
import com.kanban.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, Long> {

    @EntityGraph(attributePaths = {"team"})
    List<Invite> findAllByUserAndAcceptIsFalse(User user);

    @EntityGraph(attributePaths = {"team"})
    Optional<Invite> findAllByUserAndAcceptIsFalseAndId(User user, Long id);

    @EntityGraph(attributePaths = {"team"})
    List<Invite> findAllByUserAndAcceptIsTrue(User user);

    boolean existsByUserAndTeam(User user, Team team);
}
