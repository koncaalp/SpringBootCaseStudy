package com.alpkonca.rowMatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

// Configuration model is used to store the configuration of the game
@Data
@Entity
@Component
@Table(name = "configuration")
public class Configuration {
    @Id //Since there will be only one configuration, id is set to 1 and auto increment is not implemented
    private int id = 1;
    @Column(nullable = false)
    private int startingCoinBalance;
    @Column(nullable = false)
    private int coinPerLevel;
    @Column(nullable = false)
    private int maxTeamMemberCount;
    @Column(nullable = false)
    private int teamCreationCost;
    @Column(nullable = false)
    private int numberOfTeamsToGet;


    public Configuration() {
    }

}
