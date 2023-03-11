package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.exception.ResourceWithFieldNotFoundException;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.service.impl.TeamServiceImpl;
import com.alpkonca.rowMatch.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;


// Unit Test Class for UserController
@WebMvcTest
public class UserControllerTest {

    // To provide mock implementations of the user service and team service in order to achieve isolation
    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private TeamServiceImpl teamService;

    @Autowired
    private MockMvc mockMvc; // To provide the MockMvc object to send HTTP requests to the controller

    // Test to check if the HTTP POST request to create a new user is handled correctly when the input is valid
    @Test
    public void testCreateUser_whenSuccessful_thenReturnNewUserAsDto() throws Exception {
        // Arrange
        // NewUserDto is created since the user service returns a NewUserDto object
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setId(1);
        newUserDto.setCoinBalance(5000);
        newUserDto.setLevel(1);

        when(userService.createUser()).thenReturn(newUserDto); // Mock the user service to return the newUserDto when the createUser method is invoked
        // Act
        // Send the HTTP POST request to create a new team, map the team object to JSON
        mockMvc.perform(post("/users/create"))
                // Assert
                // Check if the HTTP status is CREATED, check if the returned JSON object has the correct values
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newUserDto.getId())))
                .andExpect(jsonPath("$.coinBalance", is(newUserDto.getCoinBalance())))
                .andExpect(jsonPath("$.level", is(newUserDto.getLevel())));
    }

    // Test to check if the HTTP PUT request to update the level of a user is handled correctly when the input is valid
    @Test
    public void testUpdateLevel_whenSuccessful_thenReturnNewProgressAsDto() throws Exception {
        // Arrange
        // ProgressDto is created since the user service returns a ProgressDto object
        int userId = 1;
        ProgressDto progressDto = new ProgressDto();
        progressDto.setLevel(1);
        progressDto.setCoinBalance(5000);
        when(userService.updateLevel(userId)).thenReturn(progressDto); // Mock the user service to return the progressDto when the updateLevel method is invoked
        // Act
        // Send the HTTP PUT request to update the level of a user, map the team object to JSON
        mockMvc.perform(put("/users/updateLevel/{id}", userId))
                // Assert
                // Check if the HTTP status is OK, check if the returned JSON object has the correct values
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", is(progressDto.getLevel())))
                .andExpect(jsonPath("$.coinBalance", is(progressDto.getCoinBalance())));
    }

    // Test to check if the HTTP PUT request to update the level of a user is handled correctly when the user with the given id does not exist
    @Test
    public void testUpdateLevel_whenUserDoesNotExist_thenReturnNotFound() throws Exception {
        // Arrange
        int userId = 1;
        when(userService.updateLevel(userId)).thenThrow(new ResourceWithFieldNotFoundException("User", "id", 0)); // Mock the user service to return null when the updateLevel method is invoked
        // Act
        // Send the HTTP PUT request to update the level of a user, map the team object to JSON
        mockMvc.perform(put("/users/updateLevel/{id}", userId))
                // Assert
                // Check if the HTTP status is NOT_FOUND
                .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", is("User not found with id : '0'")));
    }

}

