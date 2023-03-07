package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Objects;

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

    public Team(int id, String name, int memberCount, int creatorId) {
        this.id = id;
        this.name = name;
        this.memberCount = memberCount;
        this.creatorId = creatorId;
    }

    public Team() {
        memberCount = 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return id == team.id && memberCount == team.memberCount && creatorId == team.creatorId && Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, memberCount, creatorId);
    }
}
