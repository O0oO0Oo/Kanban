package com.kanban.team.service;

import com.kanban.auth.service.AuthCacheService;
import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import com.kanban.team.dto.AddTeamRequest;
import com.kanban.team.dto.FindTeamResponse;
import com.kanban.team.entity.Invite;
import com.kanban.team.entity.Team;
import com.kanban.team.enums.Role;
import com.kanban.team.repository.InviteRepository;
import com.kanban.team.repository.TeamRepository;
import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamService {

    private final UserRepository userRepository;
    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;
    private final AuthCacheService authCacheService;

    public Response<List<FindTeamResponse>> findAllTeam(String principal) {
        User user = findUserByAccountOrElseThrow(principal);
        List<Invite> invites = inviteRepository.findAllByUserAndAcceptIsTrue(user);

        List<FindTeamResponse> findTeamResponses = invites.stream()
                .map(FindTeamResponse::of
                )
                .toList();
        return Response.success(findTeamResponses);
    }

    @Transactional
    public Response<Void> addTeam(String principal, AddTeamRequest addTeamRequest) {
        User user = findUserByAccountOrElseThrow(principal);

        Team team = Team.builder()
                .name(addTeamRequest.teamName())
                .build();
        Invite invite = Invite.builder()
                .team(team)
                .user(user)
                .role(Role.LEADER)
                .accept(true)
                .build();

        Team savedTeam = teamRepository.save(team);
        inviteRepository.save(invite);
        authCacheService.updateAuthorities(principal, savedTeam.getId() + "_" + "LEADER");

        return Response.successVoid();
    }

    private User findUserByAccountOrElseThrow(String account) {
        return userRepository.findUserByAccount(
                account
        ).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
        );
    }
}