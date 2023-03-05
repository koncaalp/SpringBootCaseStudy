package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {}

