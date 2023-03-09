package com.alpkonca.rowMatch.payload;

import lombok.Data;

// This DTO is used to give proper response with only necessary information when a new user is created, teamId is excluded since it is not asked in the requirements
@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
public class NewUserDto {
    private int id;
    private int level;
    private int coinBalance;
}
