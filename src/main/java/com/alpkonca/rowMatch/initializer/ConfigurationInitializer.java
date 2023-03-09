package com.alpkonca.rowMatch.initializer;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.service.ConfigurationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationInitializer implements CommandLineRunner { // Implementation of CommandLineRunner interface to be executed at startup

    //To provide the Configuration model and ConfigurationService to the initializer
    private final ConfigurationService configurationService;
    private final Configuration configuration;
    public ConfigurationInitializer(ConfigurationService configurationService, Configuration configuration) {
        this.configurationService = configurationService;
        this.configuration = configuration;
    }

    // Implementation of CommandLineRunner interface's run method overridden to fetch the configuration from the database and set the configuration values to the Configuration object
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
