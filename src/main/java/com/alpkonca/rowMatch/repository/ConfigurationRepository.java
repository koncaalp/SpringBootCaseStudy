package com.alpkonca.rowMatch.repository;

import com.alpkonca.rowMatch.model.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {
}
