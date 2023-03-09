package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

// ORM interface for Configuration table to implement CRUD operations and map Configuration object to database table
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
}
