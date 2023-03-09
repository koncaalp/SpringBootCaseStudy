package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// ORM interface for Users table to implement CRUD operations and map User object to database table
public interface UserRepository extends JpaRepository<User, Integer> {}

