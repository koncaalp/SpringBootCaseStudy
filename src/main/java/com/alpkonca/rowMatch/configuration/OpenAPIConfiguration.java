package com.alpkonca.rowMatch.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// This class is used to configure the OpenAPI documentation
@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI rowMatchAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Row Match Game API")
                        .description("This is a sample API for the demonstration of the Row Match game")
                        .version("v1"));
    }
}
