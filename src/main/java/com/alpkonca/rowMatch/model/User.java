package com.alpkonca.rowMatch.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamId", nullable = true)
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
