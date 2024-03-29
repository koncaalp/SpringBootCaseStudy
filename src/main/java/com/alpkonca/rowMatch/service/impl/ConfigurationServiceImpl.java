package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.repository.ConfigurationRepository;
import com.alpkonca.rowMatch.service.ConfigurationService;

import org.springframework.stereotype.Service;

import java.util.List;


@Service // Service method for business logic of Configuration
public class ConfigurationServiceImpl implements ConfigurationService {

    // To provide the configuration repository to the service
    private final ConfigurationRepository configurationRepository;
    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    // Method to fetch the configuration from the database
    public Configuration fetchConfigurations() {
        List<Configuration> configurations = configurationRepository.findFirstByOrderByIdAsc();
        if (configurations.isEmpty()) { // If there is no configuration in the database, create a new one with default values
            Configuration configuration = new Configuration(1, 5000, 25, 20, 1000, 10);
            configurationRepository.save(configuration);
            return configuration;
        }
        else {
            return configurations.get(0);
        }
    }

}
