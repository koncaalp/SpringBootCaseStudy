package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import lombok.Data;


@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
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

    public Team() {
    }

    public Team(int id, String name, int memberCount) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
    }

    public Team(String name) {
        this.name = name;
        memberCount = 1;
    } // memberCount is set to 1 by default since the creator is already the only member of the team

}
