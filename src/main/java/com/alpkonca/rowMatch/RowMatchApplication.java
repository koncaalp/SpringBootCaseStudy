package com.alpkonca.rowMatch;

import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.service.ConfigurationService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class RowMatchApplication {



	public static void main(String[] args) {


		SpringApplication.run(RowMatchApplication.class, args);


	}

}
