package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.NoResourcesFoundException;
import com.alpkonca.rowMatch.exception.ResourceWithFieldNotFoundException;
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

    // Method to create a new team; check if there is a team with the same name, check if the user is already a member
    // of a team, check if the user has enough balance to create a team; if all the checks are passed, create a new team,
    // set the users team to the new team and deduct the team creation cost from the users balance
    @Override
    @Transactional // It is annotated with @Transactional to enable transaction management. Since the method includes
                   // multiple database operations, it is necessary to make use of transactional annotation to ensure
                   // that all operations are completed successfully or all of them are rolled back.
    public Team createTeam(int userId, String name) {
        Team teamWithSameName = teamRepository.findByName(name); // Check if there is a team with the same name
        if (teamWithSameName != null) {
            throw new UniqueFieldException("Team", "name", name);
        } else {
            if (userService.isMemberOfTeam(userId)) {
                throw new RuntimeException("User is already a member of a team");
            } else { // All checks are passed
                if (userService.checkBalance(userId, configuration.getTeamCreationCost())) {
                    Team newTeam = teamRepository.save(new Team(name));
                    userService.setTeam(userId, newTeam);
                    userService.deductFromBalance(userId, configuration.getTeamCreationCost());
                    return newTeam;
                }
                else {
                    throw new InsufficientBalanceException("create","team");
                }
            }
        }

    }

    // Method to join a team with the given user id and team id; check if the team exists,
    // check if the user is already a member, check if the team is full; if all the checks are passed,
    // set the users team to the given team and increment the member count of the team
    @Override
    @Transactional // It is annotated with @Transactional to enable transaction management. Since the method includes
                   // multiple database operations, it is necessary to make use of transactional annotation to ensure that
                   // all operations are completed successfully or all of them are rolled back.
    public Team joinTeam(int userId, int teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("Team", "id",teamId)); // If the team is not found, throw a ResourceWithFieldNotFoundException, necessary since the findById method returns an Optional object
        if (userService.isMemberOfTeam(userId)) {
            throw new RuntimeException("User is already a member of a team");
        }
        else if (team.getMemberCount() >= configuration.getMaxTeamMemberCount())
        {
            throw new RuntimeException("Team is full");
        }
        else { // All checks are passed
            userService.setTeam(userId, team);
            team.setMemberCount(team.getMemberCount() + 1);
            teamRepository.save(team);
            return team;
        }
    }

    // Method to get specified number of teams which have an empty spot randomly
    @Override
    public List<Team> getTeams() {
        Random rand = new Random();
        int index;
        int numberOfTeamsToGet = configuration.getNumberOfTeamsToGet();
        List<Team> allAvailableTeams = teamRepository.findByMemberCountLessThan(configuration.getMaxTeamMemberCount());
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
        else if (allAvailableTeams.size() == 0) { // If there are no teams which have an empty spot
            throw new NoResourcesFoundException("teams with empty spots");
        }
        else { // If the number of teams which have an empty spot is less than the number of teams to get, return all the teams which have an empty spot in a random order
            Collections.shuffle(allAvailableTeams); // Shuffle the list of teams which have an empty spot
            return allAvailableTeams;
        }

    }
}
