package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Integer>
{
    List<Team> findByMemberCountLessThan(int memberCount);
}
