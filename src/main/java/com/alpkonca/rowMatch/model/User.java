package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import lombok.Data;


@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
@Entity
@Table(name = "users") // This model represents the users table in the database
public class User {

    // The id field is the primary key of the table, and it is automatically incremented
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int level;
    @Column(nullable = false)
    private int coinBalance;
    @Column(nullable = false)
    private int teamId;

    public User(int id, int level, int coinBalance, int teamId) {
        this.id = id;
        this.level = level;
        this.coinBalance = coinBalance;
        this.teamId = teamId;
    }

    public User() {
        level = 1; // Since the logical starting value of the level field is 1, and it is not likely to change, set it to 1 in the constructor
    }


}
