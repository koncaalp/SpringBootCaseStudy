package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.ResourceWithFieldNotFoundException;
import com.alpkonca.rowMatch.exception.UniqueFieldException;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.payload.CreateTeamDto;
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

import static org.mockito.ArgumentMatchers.*;
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
        Team team = new Team(1,"First team", 1);
        CreateTeamDto createTeamDto = new CreateTeamDto();
        createTeamDto.setUserId(1);
        createTeamDto.setName("First team");
        when(teamService.createTeam(anyInt(), anyString())).thenReturn(team); // Mock the team service to return the team when the createTeam method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the team object to JSON
        this.mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTeamDto)))
                // Assert
                // Check if the HTTP status is CREATED, check if the returned JSON object has the correct values
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(team.getId())))
                .andExpect(jsonPath("$.name", is(team.getName())))
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when no userId is sent
    @Test
    public void testCreateTeam_whenNoUserIdSent_thenReturnBadRequestWithMessage() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "First team", 1);
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userId", is("userId must be sent and cannot be empty")));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when the name is not sent, sent as an empty string or sent as whitespace
    @Test
    public void testCreateTeam_whenNameSentBlank_thenReturnBadRequestWithMessage() throws Exception {
        // Arrange
        Team invalidTeam = new Team(1, "", 1);
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

    // Test to check if the HTTP POST request to create a new team is handled correctly when the user's balance is insufficient
    @Test
    public void testCreateTeam_whenUserBalanceInsufficient_thenReturnForbiddenWithMessage() throws Exception {
        // Arrange
        CreateTeamDto invalidTeam = new CreateTeamDto();
        invalidTeam.setUserId(1);
        invalidTeam.setName("First team");
        when(teamService.createTeam(anyInt(), anyString())).thenThrow(new InsufficientBalanceException("create", "team")); // Mock the team service to throw an exception when the createTeam method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message", is("Insufficient balance to create team")));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when the user is already in a team
    @Test
    public void testCreateTeam_whenUserAlreadyInTeam_thenReturnInternalServerErrorWithMessage() throws Exception {
        // Arrange
        CreateTeamDto invalidTeam = new CreateTeamDto();
        invalidTeam.setUserId(1);
        invalidTeam.setName("First team");
        when(teamService.createTeam(anyInt(), anyString())).thenThrow(new RuntimeException("User is already a member of a team")); // Mock the team service to throw an exception when the createTeam method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("User is already a member of a team")));
    }

    // Test to check if the HTTP POST request to create a new team is handled correctly when the team name is already taken
    @Test
    public void testCreateTeam_whenTeamNameAlreadyTaken_thenReturnBadRequestWithMessage() throws Exception {
        // Arrange
        CreateTeamDto invalidTeam = new CreateTeamDto();
        invalidTeam.setUserId(1);
        invalidTeam.setName("Team");
        when(teamService.createTeam(anyInt(), anyString())).thenThrow(new UniqueFieldException("Team", "name", "Team")); // Mock the team service to throw an exception when the createTeam method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the invalid team object to JSON
        mockMvc.perform(post("/teams/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeam)))
                // Assert
                // Check if the HTTP status is BAD_REQUEST and check if the returned JSON object has the correct message
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Team with same name already exists : 'Team'")));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when the input is valid
    @Test
    public void testJoinTeam_whenInputIsValid_thenReturnJoinedTeamWithOkStatus() throws Exception {
        // Arrange
        // JointTeamDto is created since joinTeam method takes userId and teamId as parameters
        JoinTeamDto teamDto = new JoinTeamDto();
        teamDto.setTeamId(1);
        teamDto.setUserId(1);
        Team team = new Team(1, "First team", 1);
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
                .andExpect(jsonPath("$.memberCount", is(team.getMemberCount())));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when no userId is sent
    @Test
    public void testJoinTeam_whenNoUserIdSent_thenReturnNotFoundWithMessage() throws Exception {
        // Arrange
        JoinTeamDto invalidTeamDto = new JoinTeamDto();
        invalidTeamDto.setTeamId(1);
        when(teamService.joinTeam(eq(0), anyInt())).thenThrow(new ResourceWithFieldNotFoundException("User", "id", 0)); // Mock the team service to throw exception when the joinTeam method is invoked
        // Act
        // Send the HTTP PUT request to join a team, map the invalid teamDto object to JSON
        mockMvc.perform(put("/teams/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeamDto)))
                // Assert
                // Check if the HTTP status is Not Found and check if the returned JSON object has the correct message
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("User not found with id : '0'")));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when no teamId is sent
    @Test
    public void testJoinTeam_whenNoTeamIdSent_thenReturnNotFoundWithMessage() throws Exception {
        // Arrange
        JoinTeamDto invalidTeamDto = new JoinTeamDto();
        invalidTeamDto.setUserId(1);
        when(teamService.joinTeam(anyInt(), eq(0))).thenThrow(new ResourceWithFieldNotFoundException("Team", "id", 0)); // Mock the team service to throw exception when the joinTeam method is invoked
        // Act
        // Send the HTTP PUT request to join a team, map the invalid teamDto object to JSON
        mockMvc.perform(put("/teams/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeamDto)))
                // Assert
                // Check if the HTTP status is Not Found and check if the returned JSON object has the correct message
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Team not found with id : '0'")));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when the team is full
    @Test
    public void testJoinTeam_whenTeamIsFull_thenReturnInternalServerErrorWithMessage() throws Exception {
        // Arrange
        JoinTeamDto invalidTeamDto = new JoinTeamDto();
        invalidTeamDto.setUserId(1);
        invalidTeamDto.setTeamId(1);
        when(teamService.joinTeam(anyInt(), anyInt())).thenThrow(new RuntimeException("Team is full")); // Mock the team service to throw exception when the joinTeam method is invoked
        // Act
        // Send the HTTP PUT request to join a team, map the invalid teamDto object to JSON
        mockMvc.perform(put("/teams/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeamDto)))
                // Assert
                // Check if the HTTP status is Bad Request and check if the returned JSON object has the correct message
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("Team is full")));
    }

    // Test to check if the HTTP PUT request to join a team is handled correctly when the user already has a team
    @Test
    public void testJoinTeam_whenUserAlreadyHasATeam_thenReturnInternalServerErrorWithMessage() throws Exception {
        // Arrange
        JoinTeamDto invalidTeamDto = new JoinTeamDto();
        invalidTeamDto.setUserId(1);
        invalidTeamDto.setTeamId(1);
        when(teamService.joinTeam(anyInt(), anyInt())).thenThrow(new RuntimeException("User is already a member of a team")); // Mock the team service to throw exception when the joinTeam method is invoked
        // Act
        // Send the HTTP PUT request to join a team, map the invalid teamDto object to JSON
        mockMvc.perform(put("/teams/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidTeamDto)))
                // Assert
                // Check if the HTTP status is Bad Request and check if the returned JSON object has the correct message
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message", is("User is already a member of a team")));
    }

    @Test
    public void testGetTeams_whenSuccessful_thenReturnTeamsWithOkStatus() throws Exception {
        // Arrange
        List<Team> teams = Arrays.asList(
                new Team(1, "Team 1", 1),
                new Team(2, "Team 2", 2)
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
