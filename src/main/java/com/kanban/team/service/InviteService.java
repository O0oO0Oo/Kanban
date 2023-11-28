package com.kanban.team.service;

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

    public Response<List<FindInviteResponse>> findAllInvite(String principal) {
        User user = userRepository.findByAccount(
                principal
        ).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
        );

        List<Invite> invites = inviteRepository.findAllByUserAndAcceptIsFalse(user);

        List<FindInviteResponse> findInviteResponses = invites.stream()
                .map(FindInviteResponse::of)
                .toList();

        return Response.success(findInviteResponses);
    }

    @Transactional
    public Response<Void> addInvite(AddInviteRequest addInviteRequest) {
        User user = userRepository.findByAccount(
                addInviteRequest.inviteUserAccount()
        ).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
        );

        Team team = teamRepository.findById(
                addInviteRequest.teamId()
        ).orElseThrow(
                () -> new CustomException(ErrorCode.TEAM_NOT_FOUND)
        );

        if (inviteRepository.existsByUserAndTeam(user, team)) {
            throw new CustomException(ErrorCode.INVITE_ALREADY_EXIST);
        }

        Invite invite = Invite.builder()
                .team(team)
                .user(user)
                .role(Role.MEMBER)
                .accept(false)
                .build();

        inviteRepository.save(invite);

        return Response.successVoid();
    }

    @Transactional
    public Response<Void> updateInvite(String principal, Long inviteId) {
        User user = userRepository.findByAccount(
                principal
        ).orElseThrow(
                () -> new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND)
        );

        Invite invite = inviteRepository.findAllByUserAndAcceptIsFalseAndId(user, inviteId)
                .orElseThrow(
                        () -> new CustomException(ErrorCode.INVITE_NOT_FOUND)
                );

        invite.setAccept(true);

        inviteRepository.save(invite);

        return Response.successVoid();
    }
}