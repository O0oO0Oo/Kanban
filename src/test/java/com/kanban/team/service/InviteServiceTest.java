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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class InviteServiceTest {

    @InjectMocks
    private InviteService inviteService;

    @Mock
    private InviteRepository inviteRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("findAllInvite - 성공")
    void should_successFindAllInvites_when_validRequest() {
        // given
        String principal = "test";

        User user = User.builder()
                .account("test")
                .build();

        Team team1 = Team.builder()
                .name("team1")
                .build();
        Team team2 = Team.builder()
                .name("team2")
                .build();
        Invite invite1 = Invite.builder()
                .team(team1)
                .build();
        Invite invite2 = Invite.builder()
                .team(team2)
                .build();
        ReflectionTestUtils.setField(invite1, "id", 1L);
        ReflectionTestUtils.setField(invite2, "id", 2L);
        List<Invite> inviteList = List.of(invite1, invite2);

        FindInviteResponse response1 = FindInviteResponse.of(invite1);
        FindInviteResponse response2 = FindInviteResponse.of(invite2);
        List<FindInviteResponse> findInviteResponses = List.of(response1, response2);

        when(userRepository.findByAccount(principal))
                .thenReturn(Optional.of(user));
        when(inviteRepository.findAllByUserAndAcceptIsFalse(user))
                .thenReturn(inviteList);

        // when
        Response<List<FindInviteResponse>> response = inviteService.findAllInvite(principal);

        // then
        assertThat(findInviteResponses).isEqualTo(response.data());
    }
    
    @Test
    @DisplayName("findAllInvite - 실패 - 유저 없음")
    void should_throwException_when_userNotFound() {
        // given
        String principal = "test";

        CustomException exception = new CustomException(ErrorCode.USER_ACCOUNT_NOT_FOUND);

        when(userRepository.findByAccount(principal))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(
                () -> inviteService.findAllInvite(principal), exception.getMessage(), exception
        )
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("addInvite - 성공")
    void should_successAddInvite_when_validRequest() {
        // given
        AddInviteRequest addInviteRequest
                = new AddInviteRequest("inviteUserAccount", 1L);

        User user = User.builder()
                .account("test")
                .build();
        Team team = mock(Team.class);
        Invite invite = Invite.builder()
                .user(user)
                .team(team)
                .role(Role.MEMBER)
                .accept(false)
                .build();

        when(userRepository.findByAccount(addInviteRequest.inviteUserAccount()))
                .thenReturn(Optional.of(user));
        when(teamRepository.findById(addInviteRequest.teamId()))
                .thenReturn(Optional.of(team));
        when(inviteRepository.save(any(Invite.class)))
                .thenReturn(invite);

        // when
        Response<Void> response = inviteService.addInvite(addInviteRequest);

        // then
        assertThat(HttpStatus.OK.value()).isEqualTo(response.status());
    }
    
    @Test
    @DisplayName("updateInvite - 성공")
    void should_successUpdateInvite_when_validRequest() {
        // given
        String principal = "test";

        Long inviteId = 1L;

        User user = User.builder()
                .account("test")
                .build();
        Team team = mock(Team.class);
        Invite invite = Invite.builder()
                .user(user)
                .team(team)
                .role(Role.MEMBER)
                .accept(false)
                .build();

        when(userRepository.findByAccount(
                principal
        ))
                .thenReturn(Optional.of(user));
        when(inviteRepository.findAllByUserAndAcceptIsFalseAndId(
                user, inviteId
        ))
                .thenReturn(Optional.of(invite));
        when(inviteRepository.save(invite))
                .thenReturn(invite);

        // when
        Response<Void> response = inviteService.updateInvite(principal, inviteId);

        // then
        assertThat(HttpStatus.OK.value()).isEqualTo(response.status());
    }
}