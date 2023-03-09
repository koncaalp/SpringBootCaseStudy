package com.alpkonca.rowMatch.payload;

import lombok.Data;

// This DTO is used to give proper response with only necessary information when a new user is created, userId and teamId are excluded since only the progress of the user is wanted
@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
public class ProgressDto {
    private int level;
    private int coinBalance;


}
