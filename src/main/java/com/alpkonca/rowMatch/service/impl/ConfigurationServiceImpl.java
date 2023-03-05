package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.repository.ConfigurationRepository;
import com.alpkonca.rowMatch.service.ConfigurationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationServiceImpl implements ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public Configuration fetchConfigurations() {
        // Fetch the configuration from the database
        Configuration configurations = configurationRepository.findById(1).orElseThrow(()->new RuntimeException("Configuration not found"));
        return configurations;
    }

}
