package com.alpkonca.rowMatch.configuration;

import com.alpkonca.rowMatch.repository.TeamRepository;
import com.alpkonca.rowMatch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;

import java.io.*;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;



import java.io.IOException;

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
            if (teamRepository.findAll().size() == 0 && userRepository.findAll().size() == 0) {

                ClassPathResource resource = new ClassPathResource("insert.sql");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {

                    while ((line = reader.readLine()) != null) {
                        // process each line here
                        jdbcTemplate.execute(line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            }



        }


}

