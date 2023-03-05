package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.model.Team;

import java.util.List;

public interface TeamService {
    Team createTeam(int userId, Team team);
    Team joinTeam(int userId, int teamId);
    List<Team> getTeams();

}