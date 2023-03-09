package com.alpkonca.rowMatch.payload;

import lombok.Data;

// This DTO is used to receive only userId and teamId fields from the request and send to the joinTeam method in the TeamController class
@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
public class JoinTeamDto {
    private int userId;
    private int teamId;


}

