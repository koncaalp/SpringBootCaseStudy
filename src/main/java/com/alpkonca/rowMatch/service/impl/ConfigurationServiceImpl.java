package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.repository.ConfigurationRepository;
import com.alpkonca.rowMatch.service.ConfigurationService;
import org.springframework.stereotype.Service;


@Service // Service method for business logic of Configuration
public class ConfigurationServiceImpl implements ConfigurationService {

    // To provide the configuration repository to the service
    private final ConfigurationRepository configurationRepository;
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    // Method to fetch the configuration from the database
    public Configuration fetchConfigurations() {
        Configuration configurations = configurationRepository.findById(1). // Retrieve the Configuration object with id 1 from the database, hard coded since there will be only one configuration record in the database
                orElseThrow(()->new RuntimeException("Configuration not found")); // Throw an exception if the configuration is not found, necessary since the findById method returns an Optional object
        return configurations;
    }

}
