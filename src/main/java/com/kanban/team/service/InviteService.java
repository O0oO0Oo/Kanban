package com.kanban.team.service;

import com.kanban.auth.service.AuthCacheService;
import com.kanban.common.dto.Response;
import com.kanban.common.exception.CustomException;
import com.kanban.common.exception.ErrorCode;
import com.kanban.team.dto.AddInviteRequest;
import com.kanban.team.dto.FindInviteResponse;
import com.kanban.team.entity.Invite;
import com.kanban.team.entity.Team;
import com.kanban.team.enums.Role;
import com.kanban.team.repository.InviteRepository;
import com.kanban.team.repository.TeamRepository;
import com.kanban.user.entity.User;
import com.kanban.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InviteService {

    private final InviteRepository inviteRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final AuthCacheService authCacheService;

    public Response<List<FindInviteResponse>> findAllInvite(String principal) {
        User user = findUserByAccountOrElseThrow(principal);
        List<Invite> invites = inviteRepository.findAllByUserAndAcceptIsFalse(user);

        List<FindInviteResponse> findInviteResponses = invites.stream()
                .map(FindInviteResponse::of)
                .toList();
        return Response.success(findInviteResponses);
    }

    @Transactional
    public Response<Void> addInvite(AddInviteRequest addInviteRequest) {
        User user = findUserByAccountOrElseThrow(addInviteRequest.inviteUserAccount());
        Team team = findTeamByIdOrElseThrow(addInviteRequest.teamId());

        isExistsByUserAndTeamOrElseThrow(user, team);

        Invite invite = Invite.builder()
                .team(team)
                .user(user)
                .role(Role.MEMBER)
                .accept(false)
                .build();
        inviteRepository.save(invite);

        return Response.successVoid();
    }

    private void isExistsByUserAndTeamOrElseThrow(User user, Team team) {
        if (inviteRepository.existsByUserAndTeam(user, team)) {
            throw new CustomException(ErrorCode.INVITE_ALREADY_EXIST);
        }
    }

    @Transactional
    public Response<Void> modifyInviteAccept(String principal, Long inviteId) {
        User user = findUserByAccountOrElseThrow(principal);
        Invite invite = findAllByUserAndAcceptIsFalseAndIdOrElseThrow(user, inviteId);

        invite.setAccept(true);
        inviteRepository.save(invite);
        authCacheService.updateAuthorities(principal, invite.getTeam().getId() + "_" + "MEMBER");

        return Response.successVoid();
    }

    private User findUserByAccountOrElseThrow(String account) {
        return userRepository.findUserByAccount(
                account
        ).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
        );
    }

    private Team findTeamByIdOrElseThrow(Long teamId) {
        return teamRepository.findById(
                teamId
        ).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND)
        );
    }

    private Invite findAllByUserAndAcceptIsFalseAndIdOrElseThrow(User user, Long inviteId) {
        return inviteRepository.findAllByUserAndAcceptIsFalseAndId(user, inviteId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.INVITE_NOT_FOUND)
                );
    }
}
