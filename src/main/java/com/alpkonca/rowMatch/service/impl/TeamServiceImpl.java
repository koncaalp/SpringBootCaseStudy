package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.NoResourcesFoundException;
import com.alpkonca.rowMatch.exception.ResourceWithIdNotFoundException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.repository.TeamRepository;
import com.alpkonca.rowMatch.service.TeamService;
import com.alpkonca.rowMatch.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class TeamServiceImpl implements  TeamService{
    private TeamRepository teamRepository;
    private UserService userService;
    private final Configuration configuration;



    public TeamServiceImpl(TeamRepository teamRepository, UserService userService, Configuration configuration) {
        this.teamRepository = teamRepository;
        this.userService = userService;
        this.configuration = configuration;
    }

    @Override
    @Transactional
    public Team createTeam(int userId, Team team) {
        if (userService.isMemberOfTeam(userId)) {
            throw new RuntimeException("User is already a member of a team");
        } else {
            if (userService.checkBalance(userId, configuration.getTeamCreationCost())) {
                Team newTeam = teamRepository.save(team);
                userService.setTeam(userId, newTeam.getId());
                userService.deductFromBalance(userId, configuration.getTeamCreationCost());
                return newTeam;
            } else {
                throw new InsufficientBalanceException("Insufficient balance to create a team");
            }
        }


    }

    @Override
    public Team joinTeam(int userId, int teamId) {
        if (userService.isMemberOfTeam(userId)) {
            throw new RuntimeException("User is already a member of a team");
        } else {
            Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceWithIdNotFoundException("Team", "id",teamId));
            userService.setTeam(userId, teamId);
            return team;
        }
    }

    @Override
    public List<Team> getTeams() {
        Random rand = new Random();
        int index;
        List<Team> allAvailableTeams = teamRepository.findByMemberCountLessThan(configuration.getMaxTeamMemberCount());
        if (allAvailableTeams.size() > configuration.getNumberOfTeamsToGet()) {
            List<Team> reservoir = new ArrayList<>(configuration.getNumberOfTeamsToGet());
            for (int i = 0; i < configuration.getNumberOfTeamsToGet(); i++) {
                index = rand.nextInt(allAvailableTeams.size());
                reservoir.add(allAvailableTeams.get(index));
                allAvailableTeams.remove(index);
            }
            return reservoir;
        }
        else if (allAvailableTeams.size() == 0) {
            throw new NoResourcesFoundException("teams");
        }
        else {
            return allAvailableTeams;
        }

    }
}