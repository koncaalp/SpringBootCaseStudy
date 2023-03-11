package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import lombok.Data;


@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int coinBalance;
    @ManyToOne(fetch = FetchType.LAZY) // Many users can be in one team but one user can be in only one team, fetch type is lazy because we don't need to fetch the team data everytime we fetch the user data
    @JoinColumn(name = "teamId", nullable = true) // The teamId column is the foreign key column in the users table
    private Team team;

    public User(int id, int level, int coinBalance, Team team) {
        this.id = id;
        this.level = level;
        this.coinBalance = coinBalance;
        this.team = team;
    }

    public User() {
        level = 1; // Since the logical starting value of the level field is 1, and it is not likely to change, set it to 1 in the constructor
    }


}
