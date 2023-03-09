package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.model.Configuration;

// Configuration service interface to achieve abstraction and loose coupling
public interface ConfigurationService {
    Configuration fetchConfigurations();
}
