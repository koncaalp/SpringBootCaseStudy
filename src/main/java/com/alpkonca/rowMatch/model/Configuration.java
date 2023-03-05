package com.alpkonca.rowMatch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Entity
@Component
@Table(name = "configuration")
public class Configuration {
    @Id
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
