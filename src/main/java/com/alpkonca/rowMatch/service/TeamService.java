package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.model.Team;

import java.util.List;

// Team service interface to achieve abstraction and loose coupling
public interface TeamService {
    Team createTeam(int userId, Team team);
    Team joinTeam(int userId, int teamId);
    List<Team> getTeams();

}
