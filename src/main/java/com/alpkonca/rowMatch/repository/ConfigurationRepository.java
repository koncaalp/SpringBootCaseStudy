package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.Configuration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ORM interface for Configuration table to implement CRUD operations and map Configuration object to database table
public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
    List<Configuration> findFirstByOrderByIdAsc(); // JPA Finder method to find first row of Configuration table to get configuration values
}
