package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.NoResourcesFoundException;
import com.alpkonca.rowMatch.exception.ResourceWithIdNotFoundException;
import com.alpkonca.rowMatch.exception.UniqueFieldException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.repository.TeamRepository;
import com.alpkonca.rowMatch.service.TeamService;
import com.alpkonca.rowMatch.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// Service method for business logic of Team
@Service
public class TeamServiceImpl implements  TeamService{
    // To provide the team repository, user service and configuration to the service
    private TeamRepository teamRepository;
    private UserService userService;
    private final Configuration configuration;
    public TeamServiceImpl(TeamRepository teamRepository, UserService userService, Configuration configuration) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.configuration = configuration;
    }

    // Method to create a new team,
    @Override
    @Transactional // It is annotated with @Transactional to enable transaction management. Since the method includes multiple database operations, it is necessary to make use of transactional annotation to ensure that all operations are completed successfully or all of them are rolled back.
    public Team createTeam(int userId, Team team) {
        Team teamWithSameName = teamRepository.findByName(team.getName()); // Check if there is a team with the same name
        if (teamWithSameName != null) { // If there is a team with the same name, throw a UniqueFieldException
            throw new UniqueFieldException("Team", "name", team.getName());
        } else {
            if (userService.isMemberOfTeam(userId)) { // Check if the user is already a member of a team
                throw new RuntimeException("User is already a member of a team"); // If the user is already a member of a team, throw a RuntimeException
            } else {
                if (userService.checkBalance(userId, configuration.getTeamCreationCost())) { // Check if the user has enough balance to create a team
                    // If all the checks are passed, create a new team, set the users team to the new team and deduct the team creation cost from the users balance
                    Team newTeam = teamRepository.save(team);
                    userService.setTeam(userId, newTeam.getId());
                    userService.deductFromBalance(userId, configuration.getTeamCreationCost());
                    return newTeam;
                } else {
                    throw new InsufficientBalanceException("create","team"); // If the user does not have enough balance to create a team, throw an InsufficientBalanceException
                }
            }
        }

    }

    // Method to join a team
    @Override
    @Transactional // It is annotated with @Transactional to enable transaction management. Since the method includes multiple database operations, it is necessary to make use of transactional annotation to ensure that all operations are completed successfully or all of them are rolled back.
    public Team joinTeam(int userId, int teamId) {
        Team team = teamRepository.findById(teamId) // Retrieve the team with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("Team", "id",teamId)); // If the team is not found, throw a ResourceWithIdNotFoundException, necessary since the findById method returns an Optional object
        if (userService.isMemberOfTeam(userId)) { // Check if the user is already a member of a team
            throw new RuntimeException("User is already a member of a team"); // If the user is already a member of a team, throw a RuntimeException
        }
        else if (team.getMemberCount() >= configuration.getMaxTeamMemberCount()) // Check if the team is full
        {
            throw new RuntimeException("Team is full"); // If the team is full, throw a RuntimeException
        }
        else { // If all the checks are passed, set the users team to the given team and increment the member count of the team
            userService.setTeam(userId, teamId);
            team.setMemberCount(team.getMemberCount() + 1);
            teamRepository.save(team);
            return team;
        }
    }

    // Method to get specified (in configurations) number of teams which have an empty spot randomly
    @Override
    public List<Team> getTeams() {
        Random rand = new Random();
        int index;
        int numberOfTeamsToGet = configuration.getNumberOfTeamsToGet(); // Get the number of teams to get from the configuration
        List<Team> allAvailableTeams = teamRepository.findByMemberCountLessThan(configuration.getMaxTeamMemberCount()); // Get all the teams which have an empty spot
        if (allAvailableTeams.size() > numberOfTeamsToGet) { // If the number of teams which have an empty spot is greater than the number of teams to get, get the specified number of teams randomly
            List<Team> reservoir = new ArrayList<>(numberOfTeamsToGet);
            // Randomly select indices from the list of teams which have an empty spot and add the teams with the selected indices to the reservoir
            // Then remove the selected teams from the list of teams which have an empty spot
            for (int i = 0; i < numberOfTeamsToGet; i++) {
                index = rand.nextInt(allAvailableTeams.size());
                reservoir.add(allAvailableTeams.get(index));
                allAvailableTeams.remove(index);
            }
            return reservoir;
        }
        else if (allAvailableTeams.size() == 0) { // If there are no teams which have an empty spot, throw a NoResourcesFoundException
            throw new NoResourcesFoundException("teams");
        }
        else { // If the number of teams which have an empty spot is less than the number of teams to get, return all the teams which have an empty spot in a random order
            Collections.shuffle(allAvailableTeams); // Shuffle the list of teams which have an empty spot
            return allAvailableTeams;
        }

    }
}
