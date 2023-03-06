package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    @NotBlank(message = "name must be sent and cannot be empty")
    private String name;
    @Column(nullable = false)
    private int memberCount;
    @Column(nullable = false)
    @Min(value = 1, message = "creatorId must be sent and be greater than 0")
    private int creatorId;



    public Team() {
        memberCount = 1;
    }
}
