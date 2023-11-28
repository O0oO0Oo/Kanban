package com.kanban.team.controller;

import com.kanban.common.dto.Response;
import com.kanban.team.dto.AddTeamRequest;
import com.kanban.team.dto.FindTeamResponse;
import com.kanban.team.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping
    public Response<List<FindTeamResponse>> findAllTeam(@AuthenticationPrincipal String principal) {
        return teamService.findAllTeam(principal);
    }


    @PostMapping
    public Response<Void> addTeam(@AuthenticationPrincipal String principal,
                                  @Valid @RequestBody AddTeamRequest addTeamRequest) {
        return teamService.addTeam(principal, addTeamRequest);
    }
}
