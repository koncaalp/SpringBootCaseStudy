package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;


@Data // Generates boilerplate code for getters, setters, equals, hashCode, and toString
@Entity
@Table(name = "teams") // This model represents the teams table in the database
public class Team {
    // The id field is the primary key of the table, and it is automatically incremented
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true) // The name field is unique
    @NotBlank(message = "name must be sent and cannot be empty") // Validation for the name field to ensure that it is not null, empty, or whitespace
    private String name;
    @Column(nullable = false)
    private int memberCount;
    @Column(nullable = false)
    @Min(value = 1, message = "creatorId must be sent and be greater than 0") // Validation for the creatorId field to ensure that it is greater than 0
    private int creatorId;

    public Team(int id, String name, int memberCount, int creatorId) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.creatorId = creatorId;
    }

    public Team() {
        memberCount = 1;
    } // memberCount is set to 1 by default since the creator is already the only member of the team

}
