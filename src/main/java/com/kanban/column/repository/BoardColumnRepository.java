package com.kanban.column.repository;

import com.kanban.column.entity.BoardColumn;
import com.kanban.team.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long> {

    int countByTeam(Team team);

    @EntityGraph(attributePaths = {"tickets"})
    List<BoardColumn> findByTeamOrderByOrderNumber(Team team);

    Optional<BoardColumn> findBoardColumnByTeamIdAndId(Long teamId, Long id);
}