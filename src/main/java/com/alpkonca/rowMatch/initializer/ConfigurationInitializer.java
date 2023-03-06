package com.alpkonca.rowMatch.initializer;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.service.ConfigurationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component // Marks this class as a Spring bean to be managed by Spring framework
public class ConfigurationInitializer implements CommandLineRunner {

    private final ConfigurationService configurationService; // Dependency injection for ConfigurationService bean
    private final Configuration configuration; // Dependency injection for Configuration bean

    // Constructor for dependency injection
    public ConfigurationInitializer(ConfigurationService configurationService, Configuration configuration) {
        this.configurationService = configurationService;
        this.configuration = configuration;
    }

    // Implementation of CommandLineRunner interface's run method, to be executed at startup
    @Override
    public void run(String... args) throws Exception {
        // Fetch the configuration from the database using the ConfigurationService
        Configuration config = configurationService.fetchConfigurations();
        // Set the configuration values fetched from the database to the Configuration object
        configuration.setCoinPerLevel(config.getCoinPerLevel());
        configuration.setMaxTeamMemberCount(config.getMaxTeamMemberCount());
        configuration.setTeamCreationCost(config.getTeamCreationCost());
        configuration.setNumberOfTeamsToGet(config.getNumberOfTeamsToGet());
        configuration.setStartingCoinBalance(config.getStartingCoinBalance());
    }
}
