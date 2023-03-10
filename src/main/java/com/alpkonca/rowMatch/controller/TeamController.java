package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.payload.JoinTeamDto;
import com.alpkonca.rowMatch.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This class is a REST controller that handles incoming HTTP requests and sends HTTP responses for team related requests.
@RestController
@RequestMapping("/teams")
public class TeamController {

    // To provide the team service to the controller
    private TeamService teamService;
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    // HTTP POST request to create a new team
    @Operation(summary = "User creates a new team")
    @PostMapping("/create")
    public ResponseEntity<Team> createUser(@Valid @RequestBody Team team){
        return new ResponseEntity<>(teamService.createTeam(team.getCreatorId(), team), HttpStatus.CREATED);
    }

    // HTTP PUT request to join a team
    @Operation(summary = "User joins an existent team")
    @PutMapping("/join")
    public ResponseEntity<Team> joinTeam(@RequestBody JoinTeamDto joinTeamDto){
        //Map the joinTeamDto to userId and teamId
        int userId = joinTeamDto.getUserId();
        int teamId = joinTeamDto.getTeamId();
        return new ResponseEntity<>(teamService.joinTeam(userId, teamId), HttpStatus.OK);
    }

    // HTTP GET request to get specified number of teams randomly
    @GetMapping("/getTeams")
    @Operation(summary = "Gets specified number of teams with open spots randomly")
    public ResponseEntity<List<Team>> getTeams(){
        return new ResponseEntity<>(teamService.getTeams(), HttpStatus.OK);
    }


}
