package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.payload.JoinTeamDto;
import com.alpkonca.rowMatch.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }




    @PostMapping("/create")
    public ResponseEntity<Team> createUser(@Valid @RequestBody Team team){
        return new ResponseEntity<Team>(teamService.createTeam(team.getCreatorId(), team), HttpStatus.CREATED);

    }
    @PutMapping("/join")
    public ResponseEntity<Team> joinTeam(@RequestBody JoinTeamDto joinTeamDto){
        int userId = joinTeamDto.getUserId();
        int teamId = joinTeamDto.getTeamId();
        return new ResponseEntity<Team>(teamService.joinTeam(userId, teamId), HttpStatus.OK);
    }

    @GetMapping("/getTeams")
    public ResponseEntity<List<Team>> getTeams(){
        return new ResponseEntity<List<Team>>(teamService.getTeams(), HttpStatus.OK);
    }


}
