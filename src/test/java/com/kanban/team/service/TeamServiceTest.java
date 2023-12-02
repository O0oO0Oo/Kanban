package com.kanban.team.service;

import com.kanban.auth.service.AuthCacheService;
import com.kanban.common.dto.Response;
import com.kanban.team.dto.AddTeamRequest;
import com.kanban.team.dto.FindTeamResponse;
import com.kanban.team.entity.Invite;
import com.kanban.team.entity.Team;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private InviteRepository inviteRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthCacheService authCacheService;

    @Test
    @DisplayName("findAllTeam - 성공")
    void should_successFindAllTeam_when_validRequest() {
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

        FindTeamResponse findTeamResponse1 = FindTeamResponse.of(invite1);
        FindTeamResponse findTeamResponse2 = FindTeamResponse.of(invite2);
        List<FindTeamResponse> findTeamResponses = List.of(findTeamResponse1, findTeamResponse2);

        when(userRepository.findUserByAccount(principal))
                .thenReturn(Optional.of(user));
        when(inviteRepository.findAllByUserAndAcceptIsTrue(user))
                .thenReturn(inviteList);

        // when
        Response<List<FindTeamResponse>> response = teamService.findAllTeam(principal);

        // then
        assertThat(findTeamResponses).isEqualTo(response.data());
    }
    
    @Test
    @DisplayName("addTeam - 성공")
    void should_successAddTeam_when_validRequest() {
        // given
        String principal = "test";

        AddTeamRequest request = new AddTeamRequest("testTeam");

        User user = User.builder()
                .account("test")
                .build();

        Team team = Team.builder()
                .name(request.teamName())
                .build();
        Invite invite = Invite.builder()
                .team(team)
                .build();

        when(userRepository.findUserByAccount(principal))
                .thenReturn(Optional.of(user));
        when(teamRepository.save(any(Team.class)))
                .thenReturn(team);
        when(inviteRepository.save(any(Invite.class)))
                .thenReturn(invite);
        when(authCacheService.updateAuthorities(principal, invite.getTeam().getId() + "_" + "LEADER"))
                .thenReturn(anyString());

        // when
        Response<Void> response = teamService.addTeam(principal, request);

        // then
        assertThat(HttpStatus.OK.value()).isEqualTo(response.status());
    }
}