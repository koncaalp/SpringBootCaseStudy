package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private int memberCount;
    @Column(nullable = false)
    private int creatorId;



    public Team() {
        memberCount = 1;
    }
}
