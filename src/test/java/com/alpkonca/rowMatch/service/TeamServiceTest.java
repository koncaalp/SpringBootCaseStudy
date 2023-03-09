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

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    private Team team;
    private int userId;
    @Mock
    private Configuration configuration;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private TeamServiceImpl teamService;

    @BeforeEach
    public void setUp() {
        team = new Team(1, "First team", 1, 1);
        userId = team.getCreatorId();
    }

    @Test
    public void testCreateTeam_whenInputIsValid_thenReturnCreatedTeam() {
        // Arrange
        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(userService.checkBalance(userId, configuration.getTeamCreationCost())).thenReturn(true);
        Mockito.when(teamRepository.save(team)).thenReturn(team);

        // Act
        Team result = teamService.createTeam(userId, team);

        // Assert

        assertEquals (result.getId(),team.getId());
        assertEquals (result.getName(),team.getName());
        assertEquals (result.getMemberCount(),team.getMemberCount());
        assertEquals (result.getCreatorId(),team.getCreatorId());
    }

    @Test
    public void testCreateTeam_whenNameIsDuplicate_thenThrowUniqueFieldException() {
        // Arrange

        Team existingTeam = new Team(2, "First team", 3, 3);


        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(existingTeam);

        // Act and Assert
        assertThrows(UniqueFieldException.class, () -> {
            teamService.createTeam(userId, team);
        });
    }

    @Test
    public void testCreateTeam_whenUserAlreadyHasATeam_thenThrowRuntimeException() {
        // Arrange


        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(true);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            teamService.createTeam(userId, team);
        });
    }
    @Test
    public void testCreateTeam_whenUsersBalanceIsInsufficient_thenThrowInsufficientBalanceException() {

        Team team = new Team(1, "Team A", 1, userId);

        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(userService.checkBalance(userId, configuration.getTeamCreationCost())).thenReturn(false);



        // Act and Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            teamService.createTeam(userId, team);
        });
    }
    @Test
    public void testJoinTeam_whenInputIsValid_thenReturnJoinedTeam() {
        // Arrange
        Mockito.when(teamRepository.findById(team.getId())).thenReturn(Optional.ofNullable(team));
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        int initialMemberCount = team.getMemberCount();

        // Act
        Team result = teamService.joinTeam(userId, team.getId());

        // Assert
        assertEquals(initialMemberCount + 1, result.getMemberCount());
        assertEquals (result.getId(),team.getId());
        assertEquals (result.getName(),team.getName());
        assertEquals (result.getMemberCount(),team.getMemberCount());
        assertEquals (result.getCreatorId(),team.getCreatorId());
    }

    @Test
    public void testJoinTeam_whenUserAlreadyHasATeam_thenThrowException() {
        // Arrange
        Mockito.when(teamRepository.findById(team.getId())).thenReturn(Optional.ofNullable(team));
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(true);
        // Act & Assert
        assertThrows(RuntimeException.class, () -> teamService.joinTeam(userId, team.getId()));


    }

    @Test
    public void testJoinTeam_whenTeamIsFull_thenThrowException() {
        // Arrange
        int userId = 1;
        int teamId = 2;
        int maxTeamMemberCount = 5;
        Team team = new Team();
        team.setId(teamId);
        team.setMemberCount(maxTeamMemberCount);
        Configuration configuration = new Configuration();
        configuration.setMaxTeamMemberCount(maxTeamMemberCount);

        Mockito.when(teamRepository.findById(teamId)).thenReturn(java.util.Optional.of(team));

        teamService = new TeamServiceImpl(teamRepository, userService, configuration);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> teamService.joinTeam(userId, teamId));

    }
    @Test
    public void testGetTeams_whenThereAreMoreThan10Teams_thenFetch10RandomTeams() {
        // Arrange
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        teams.add(new Team(2, "Second team", 7, 2));
        teams.add(new Team(3, "Third team", 3, 1));
        teams.add(new Team(4, "Fourth team", 4, 4));
        teams.add(new Team(5, "Fifth team", 8, 5));
        teams.add(new Team(6, "Sixth team", 6, 2));
        teams.add(new Team(7, "Seventh team", 5, 1));
        teams.add(new Team(8, "Eighth team", 2, 3));
        teams.add(new Team(9, "Ninth team", 5, 2));
        teams.add(new Team(10, "Tenth team", 6, 6));
        teams.add(new Team(11, "Eleventh team", 7, 1));
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);
        // Act
        List<Team> result = teamService.getTeams();
        // Assert
        Set<Team> uniqueTeams = new HashSet<>(result);
        assertEquals(10, uniqueTeams.size());
    }
    @Test
    public void testGetTeams_whenThereAreLessThan10_thenFetchAllTeamsRandomly() {
        // Arrange
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        teams.add(new Team(2, "Second team", 7, 2));
        teams.add(new Team(3, "Third team", 3, 1));
        teams.add(new Team(4, "Fourth team", 4, 4));
        teams.add(new Team(5, "Fifth team", 8, 5));
        teams.add(new Team(6, "Sixth team", 6, 2));
        teams.add(new Team(7, "Seventh team", 5, 1));
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);
        // Act
        List<Team> result = teamService.getTeams();
        // Assert
        Set<Team> uniqueTeams = new HashSet<>(teams);
        assertEquals(7, uniqueTeams.size());
    }
    @Test
    public void testGetTeams_whenThereAreNoTeams_thenThrowNoResourcesFoundException() {
        // Arrange
        List<Team> teams = new ArrayList<>();
        Mockito.when(teamRepository.findByMemberCountLessThan(20)).thenReturn(teams);
        Mockito.when(configuration.getMaxTeamMemberCount()).thenReturn(20);
        Mockito.when(configuration.getNumberOfTeamsToGet()).thenReturn(10);

        // Act & Assert
        assertThrows(NoResourcesFoundException.class, () -> teamService.getTeams());
    }

}