package com.kanban.team.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kanban.auth.config.TestSecurityConfig;
import com.kanban.auth.filter.JwtAuthFilter;
import com.kanban.common.dto.Response;
import com.kanban.team.dto.AddInviteRequest;
import com.kanban.team.service.InviteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@WebMvcTest(controllers = InviteController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
        })
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class InviteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InviteService inviteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("addInvite - 인가 성공")
    @WithMockUser(username = "test", authorities = {"1_LEADER"})
    void should_successResponse_when_hasAuthorityUser() throws Exception {

        // given
        AddInviteRequest addInviteRequest = new AddInviteRequest("test1", 1L);
        String requestJson = objectMapper.writeValueAsString(addInviteRequest);

        when(inviteService.addInvite(addInviteRequest))
                .thenReturn(Response.successVoid());

        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/team/invite")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(
                        MockMvcResultMatchers.status().isOk()
                );
    }

    @Test
    @DisplayName("addInvite - 인가 실패 - 권한 없음")
    @WithMockUser(username = "test", authorities = {"1_LEADER"})
    void should_accessDenied_when_invalidAuthorityUser() throws Exception {
        AddInviteRequest addInviteRequest = new AddInviteRequest("test1", 999L);
        String requestJson = objectMapper.writeValueAsString(addInviteRequest);


        // when & then
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/team/invite")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(
                        MockMvcResultMatchers
                                .status().isForbidden()
                );
    }
}