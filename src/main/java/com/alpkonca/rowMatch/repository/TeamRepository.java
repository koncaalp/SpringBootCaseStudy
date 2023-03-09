package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ORM interface for Teams table to implement CRUD operations and map Team object to database table
public interface TeamRepository extends JpaRepository<Team, Integer>
{
    List<Team> findByMemberCountLessThan(int memberCount); // JPA finder method to find teams with less than memberCount members
    Team findByName(String name); // JPA finder method to find team with name
}
