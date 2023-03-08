package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.payload.JoinTeamDto;
import com.alpkonca.rowMatch.service.impl.TeamServiceImpl;
import com.alpkonca.rowMatch.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
public class TeamControllerTest {
    @MockBean
    private TeamServiceImpl teamService;
    @MockBean
    private UserServiceImpl userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateTeam_withValidInput() throws Exception {
        // Arrange
        Team team = new Team(1, "First team", 1, 1);
        when(teamService.createTeam(anyInt(), any(Team.class))).thenReturn(team);
        // Act
        this.mockMvc.perform(post("/teams/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(team)))
                .andExpect(status().isCreated())
                // Assert
                .andExpect(jsonPath("$.id", is(team.getId())))
                .andExpect(jsonPath("$.name", is(team.getName())))
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())))
                .andExpect(jsonPath("$.creatorId", is(team.getCreatorId())));
    }
    @Test
    public void testCreateTeamWithNoCreatorId() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "First team", 1, 0);


        String requestBody = objectMapper.writeValueAsString(invalidTeam);

        // Act
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Assert
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.creatorId", is("creatorId must be sent and be greater than 0")));
    }
    @Test
    public void testCreateTeamWithBlankTeamName() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "", 1, 1);


        String requestBody = objectMapper.writeValueAsString(invalidTeam);

        // Act
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                // Assert
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name", is("name must be sent and cannot be empty")));
    }
    @Test
    public void testJoinTeam_withValidInput() throws Exception {
        // Arrange
        JoinTeamDto teamDto = new JoinTeamDto();
        teamDto.setTeamId(1);
        teamDto.setUserId(1);
        Team team = new Team(1, "First team", 1, 1);
        when(teamService.joinTeam(teamDto.getUserId(), teamDto.getTeamId())).thenReturn(team);
        // Act
        this.mockMvc.perform(put("/teams/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamDto)))
                .andExpect(status().isOk())
                // Assert
                .andExpect(jsonPath("$.id", is(team.getId())))
                .andExpect(jsonPath("$.name", is(team.getName())))
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())))
                .andExpect(jsonPath("$.creatorId", is(team.getCreatorId())));
    }
    @Test
    public void testGetTeams() throws Exception {
        // Setup
        List<Team> teams = Arrays.asList(
                new Team(1, "Team 1", 1, 1),
                new Team(2, "Team 2", 2, 2)
        );
        when(teamService.getTeams()).thenReturn(teams);

        // Exercise
        mockMvc.perform(get("/teams/getTeams"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teams)));

    }

}
