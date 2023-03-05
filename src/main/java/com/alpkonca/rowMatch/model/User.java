package com.alpkonca.rowMatch.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
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
    @Column(nullable = false)
    private int teamId;




    public User() {
        level = 1;
    }


}