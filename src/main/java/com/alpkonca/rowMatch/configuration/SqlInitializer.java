package com.alpkonca.rowMatch.configuration;

import com.alpkonca.rowMatch.repository.TeamRepository;
import com.alpkonca.rowMatch.repository.UserRepository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.io.IOException;

// This class is used to insert data to database when the application starts if there is no data in the user and team tables.

@Component
public class SqlInitializer implements ApplicationRunner {

        JdbcTemplate jdbcTemplate;
        TeamRepository teamRepository;
        UserRepository userRepository;

    public SqlInitializer(JdbcTemplate jdbcTemplate, TeamRepository teamRepository, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    @Override
        public void run(ApplicationArguments args) throws Exception {
            String line;
            if (teamRepository.findAll().size() == 0 && userRepository.findAll().size() == 0) { // if there is no data in the user and team tables, insert data to database.

                ClassPathResource resource = new ClassPathResource("insert.sql");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                    while ((line = reader.readLine()) != null) { // read each sql insert statement from the insert.sql file and execute it
                        // process each line here
                        jdbcTemplate.execute(line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            }



        }


}

