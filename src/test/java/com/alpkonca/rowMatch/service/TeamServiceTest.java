package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.NoResourcesFoundException;
import com.alpkonca.rowMatch.exception.UniqueFieldException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.repository.TeamRepository;


import com.alpkonca.rowMatch.service.impl.TeamServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Unit Test Class for TeamService
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    // So that the test methods can reach the initialized objects in the setUp method
    private Team team;
    private int userId;

    // To provide mock implementations of the team repository, configuration, team service and user service in order to achieve isolation
    @Mock
    private Configuration configuration;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserService userService;

    // In order to inject the mock implementations of the team repository, configuration, team service and user service into the team service
    @InjectMocks
    private TeamServiceImpl teamService;

    // To initialize a team object before each test method
    @BeforeEach
    public void setUp() {
        team = new Team(0,"First team", 1);
        userId = 1;
    }

    // Test to check if the createTeam method is handled correctly when the input is valid
    @Test
    public void testCreateTeam_whenInputIsValid_thenReturnCreatedTeam() {
        // Arrange
        // Mock the necessary methods of the team repository, user service and configuration to return the desired values for this scenario
        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(userService.checkBalance(userId, configuration.getTeamCreationCost())).thenReturn(true);
        Mockito.when(teamRepository.save(team)).thenReturn(team);

        // Act
        Team result = teamService.createTeam(userId, team.getName()); // Call the createTeam method of the team service with userId and team name

        // Assert
        // Check if the returned team object is the same as the team object that was created in the setUp method
        assertEquals (result.getId(),team.getId());
        assertEquals (result.getName(),team.getName());
        assertEquals (result.getMemberCount(),team.getMemberCount());
    }

    // Test to check if the createTeam method is handled correctly when a team with the same name already exists
    @Test
    public void testCreateTeam_whenNameIsDuplicate_thenThrowUniqueFieldException() {
        // Arrange
        Team existingTeam = new Team(2,"First team", 3); // Create an existing team object with the same name as the team object that was created in the setUp method
        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(existingTeam); // Mock the findByName method of the team repository to return the existing team object

        // Act and Assert
        assertThrows(UniqueFieldException.class, () -> {teamService.createTeam(userId, team.getName());}); // Check if the createTeam method of the team service throws a UniqueFieldException when the team to be created has the same name as the existing team
    }

    // Test to check if the createTeam method is handled correctly when the user already has a team
    @Test
    public void testCreateTeam_whenUserAlreadyHasATeam_thenThrowRuntimeException() {
        // Arrange
        // Mock the necessary methods of the team repository and user service to return the desired values for this scenario
        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(true);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {teamService.createTeam(userId, team.getName());}); // Check if the createTeam method of the team service throws a RuntimeException when the user already has a team
    }

    // Test to check if the createTeam method is handled correctly when the user's balance is insufficient
    @Test
    public void testCreateTeam_whenUsersBalanceIsInsufficient_thenThrowInsufficientBalanceException() {
        // Arrange
        // Mock the necessary methods of the team repository and user service to return the desired values for this scenario
        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(userService.checkBalance(userId, configuration.getTeamCreationCost())).thenReturn(false);

        // Act and Assert
        assertThrows(InsufficientBalanceException.class, () -> {teamService.createTeam(userId, team.getName());}); // Check if the createTeam method of the team service throws an InsufficientBalanceException when the user's balance is insufficient

    }

    // Test to check if the joinTeam method is handled correctly when the input is valid
    @Test
    public void testJoinTeam_whenInputIsValid_thenReturnJoinedTeam() {
        // Arrange
        // Mock the necessary methods of the team repository, user service and configuration to return the desired values for this scenario
        Mockito.when(teamRepository.findById(team.getId())).thenReturn(Optional.ofNullable(team));
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);

        int initialMemberCount = team.getMemberCount(); // Get the initial member count of the team to test if it is incremented by 1 after the user joins the team

        // Act
        Team result = teamService.joinTeam(userId, team.getId()); // Call the joinTeam method of the team service with userId and teamId

        // Assert
        // Check if the returned team object is the same as the team object that was created in the setUp method and if the member count is incremented by 1
        assertEquals(initialMemberCount + 1, result.getMemberCount());
        assertEquals (result.getId(),team.getId());
        assertEquals (result.getName(),team.getName());
        assertEquals (result.getMemberCount(),team.getMemberCount());
    }

    // Test to check if the joinTeam method is handled correctly when the user already has a team
    @Test
    public void testJoinTeam_whenUserAlreadyHasATeam_thenThrowException() {
        // Arrange
        // Mock the necessary methods of the team repository and user service to return the desired values for this scenario
        Mockito.when(teamRepository.findById(team.getId())).thenReturn(Optional.ofNullable(team));
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> teamService.joinTeam(userId, team.getId())); // Check if the joinTeam method of the team service throws a RuntimeException when the user already has a team and tries to join another team
    }

    // Test to check if the joinTeam method is handled correctly when the team is full
    @Test
    public void testJoinTeam_whenTeamIsFull_thenThrowException() {
        // Arrange
        int maxTeamMemberCount = 5;
        team.setMemberCount(maxTeamMemberCount);

        // Mock the necessary methods of the team repository and configuration to return the desired values for this scenario
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(maxTeamMemberCount);
        Mockito.when(teamRepository.findById(team.getId())).thenReturn(java.util.Optional.of(team));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> teamService.joinTeam(userId, team.getId())); // Check if the joinTeam method of the team service throws a RuntimeException when the team is full and the user tries to join it
    }

    // Test to check if the getTeams method is handled correctly when there are more than 10 teams
    @Test
    public void testGetTeams_whenThereAreMoreThan10Teams_thenFetch10RandomTeams() {
        // Arrange
        // Create a list of 11 teams
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        teams.add(new Team(2, "Second team", 7));
        teams.add(new Team(3, "Third team", 3));
        teams.add(new Team(4, "Fourth team", 4));
        teams.add(new Team(5, "Fifth team", 8));
        teams.add(new Team(6, "Sixth team", 6));
        teams.add(new Team(7, "Seventh team", 5));
        teams.add(new Team(8, "Eighth team", 2));
        teams.add(new Team(9, "Ninth team", 5));
        teams.add(new Team(10, "Tenth team", 6));
        teams.add(new Team(11, "Eleventh team", 7));

        // Mock the necessary methods of the team repository and configuration to return the desired values for this scenario
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);

        // Act
        List<Team> result = teamService.getTeams(); // Call the getTeams method of the team service

        // Assert
        // Check if the returned list contains 10 unique teams by adding them to a HashSet
        Set<Team> uniqueTeams = new HashSet<>(result);
        assertEquals(10, uniqueTeams.size());
    }

    // Test to check if the getTeams method is handled correctly when there are less than 10 teams
    @Test
    public void testGetTeams_whenThereAreLessThan10_thenFetchAllTeamsRandomly() {
        // Arrange
        // Create a list of 7 teams
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        teams.add(new Team(2, "Second team", 7));
        teams.add(new Team(3, "Third team", 3));
        teams.add(new Team(4, "Fourth team", 4));
        teams.add(new Team(5, "Fifth team", 8));
        teams.add(new Team(6, "Sixth team", 6));
        teams.add(new Team(7, "Seventh team", 5));

        // Mock the necessary methods of the team repository and configuration to return the desired values for this scenario
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);

        // Act
        List<Team> result = teamService.getTeams(); // Call the getTeams method of the team service

        // Assert
        // Check if the returned list contains 7 unique teams by adding them to a HashSet
        Set<Team> uniqueTeams = new HashSet<>(result);
        assertEquals(7, uniqueTeams.size());
    }

    // Test to check if the getTeams method is handled correctly when there are no teams
    @Test
    public void testGetTeams_whenThereAreNoTeams_thenThrowNoResourcesFoundException() {
        // Arrange
        List<Team> teams = new ArrayList<>(); // Create an empty list of teams

        // Mock the necessary methods of the team repository and configuration to return the desired values for this scenario
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);

        // Act & Assert
        assertThrows(NoResourcesFoundException.class, () -> teamService.getTeams()); // Check if the getTeams method of the team service throws a NoResourcesFoundException when there are no teams
    }

}