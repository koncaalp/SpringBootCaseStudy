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

// Unit Test Class for TeamController
@WebMvcTest
public class TeamControllerTest {
    // To provide mock implementations of the team service and user service in order to achieve isolation
    @MockBean
    private TeamServiceImpl teamService;
    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private MockMvc mockMvc; // To provide the MockMvc object to send HTTP requests to the controller
    @Autowired
    private ObjectMapper objectMapper; // To provide the ObjectMapper object to convert Java objects to JSON and vice versa

    // Test to check if the HTTP POST request to create a new team is handled correctly when the input is valid
    @Test
    public void testCreateTeam_whenInputIsValid_thenReturnCreatedTeamWithCreatedStatus() throws Exception {
        // Arrange
        Team team = new Team(1, "First team", 1, 1);
        when(teamService.createTeam(anyInt(), any(Team.class))).thenReturn(team); // Mock the team service to return the team when the createTeam method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the team object to JSON
        this.mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(team)))
                // Assert
                // Check if the HTTP status is CREATED, check if the returned JSON object has the correct values
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(team.getId())))
                .andExpect(jsonPath("$.name", is(team.getName())))
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())))
                .andExpect(jsonPath("$.creatorId", is(team.getCreatorId())));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when no creatorId is sent
    @Test
    public void testCreateTeam_whenNoCreatorIdSent_thenReturnBadRequestWithMessage() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "First team", 1, 0);
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.creatorId", is("creatorId must be sent and be greater than 0")));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when the name is not sent, sent as an empty string or sent as whitespace
    @Test
    public void testCreateTeam_whenNameSentBlank_thenReturnBadRequestWithMessage() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "", 1, 1);
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.name", is("name must be sent and cannot be empty")));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when the input is valid
    @Test
    public void testJoinTeam_whenInputIsValid_thenReturnJoinedTeamWithOkStatus() throws Exception {
        // Arrange
        // JointTeamDto is created since joinTeam method takes userId and teamId as parameters
        JoinTeamDto teamDto = new JoinTeamDto();
        teamDto.setTeamId(1);
        teamDto.setUserId(1);
        Team team = new Team(1, "First team", 1, 1);
        when(teamService.joinTeam(teamDto.getUserId(), teamDto.getTeamId())).thenReturn(team); // Mock the team service to return the team when the joinTeam method is invoked
        // Act
        // Send the HTTP PUT request to join a team, map the teamDto object to JSON
        this.mockMvc.perform(put("/teams/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamDto)))
                // Assert
                // Check if the HTTP status is OK, check if the returned JSON object has the correct values
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(team.getId())))
                .andExpect(jsonPath("$.name", is(team.getName())))
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())))
                .andExpect(jsonPath("$.creatorId", is(team.getCreatorId())));
    }
    @Test
    public void testGetTeams_whenSuccessful_thenReturnTeamsWithOkStatus() throws Exception {
        // Arrange
        List<Team> teams = Arrays.asList(
                new Team(1, "Team 1", 1, 1),
                new Team(2, "Team 2", 2, 2)
        );
        when(teamService.getTeams()).thenReturn(teams); // Mock the team service to return the teams when the getTeams method is invoked
        // Act
        // Send the HTTP GET request to get all teams
        mockMvc.perform(get("/teams/getTeams"))
                // Assert
                // Check if the HTTP status is OK, check if the returned JSON object has the correct values
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(teams)));

    }

}
