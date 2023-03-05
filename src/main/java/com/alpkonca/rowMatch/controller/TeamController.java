package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.exception.ErrorResponse;
import com.alpkonca.rowMatch.exception.InsufficientBalanceException;
import com.alpkonca.rowMatch.exception.MissingFieldException;
import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.JoinTeamDto;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.service.TeamService;
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

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ErrorResponse errorResponse = new ErrorResponse("insufficient_balance", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
    @ExceptionHandler(MissingFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMissingFieldException(MissingFieldException ex) {
        ErrorResponse errorResponse = new ErrorResponse("missing_field", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }


    @PostMapping("/create")
    public ResponseEntity<Team> createUser(@RequestBody Team team){
        if (team.getName() == null || team.getName().isEmpty()) {
           throw new MissingFieldException("name");
        }
        else {
            return new ResponseEntity<Team>(teamService.createTeam(team.getCreatorId(), team), HttpStatus.CREATED);
        }
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
