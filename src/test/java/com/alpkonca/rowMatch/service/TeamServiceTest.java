package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.UniqueFieldException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.repository.TeamRepository;
import com.alpkonca.rowMatch.service.TeamService;
import com.alpkonca.rowMatch.service.UserService;
import com.alpkonca.rowMatch.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private Configuration configuration;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    public void testCreateTeam_withValidInput() {
        // Arrange
        int userId = 1;
        Team team = new Team(1, "Team A", 1, userId);

        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(false);
        Mockito.when(userService.checkBalance(userId, configuration.getTeamCreationCost())).thenReturn(true);
        Mockito.when(teamRepository.save(team)).thenReturn(team);

        // Act
        Team result = teamService.createTeam(userId, team);

        // Assert
        Mockito.verify(teamRepository, Mockito.times(1)).save(team);
        Mockito.verify(userService, Mockito.times(1)).setTeam(userId, team.getId());
        Mockito.verify(userService, Mockito.times(1)).deductFromBalance(userId, configuration.getTeamCreationCost());
        assert (result.equals(team));
    }

    @Test
    public void testCreateTeam_withDuplicateTeamName() {
        // Arrange
        int userId = 1;
        Team team = new Team();
        team.setName("Team A");
        team.setMemberCount(1);
        team.setCreatorId(userId);
        Team existingTeam = new Team();
        existingTeam.setId(2);
        existingTeam.setName("Team A");
        existingTeam.setMemberCount(3);
        existingTeam.setCreatorId(3);

        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(existingTeam);

        // Act and Assert
        assertThrows(UniqueFieldException.class, () -> {
            teamService.createTeam(userId, team);
        });
    }

    @Test
    public void testCreateTeam_withUserAlreadyMemberOfTeam() {
        // Arrange
        int userId = 1;
        Team team = new Team();
        team.setName("Team A");
        team.setMemberCount(1);
        team.setCreatorId(userId);

        Mockito.when(teamRepository.findByName(team.getName())).thenReturn(null);
        Mockito.when(userService.isMemberOfTeam(userId)).thenReturn(true);

        // Act and Assert
        assertThrows(RuntimeException.class, () -> {
            teamService.createTeam(userId, team);
        });
    }

}