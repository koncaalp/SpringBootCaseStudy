package com.alpkonca.rowMatch.controller;

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

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    private TeamServiceImpl teamService;

    @Test
    public void testCreateUser_whenSuccessful_thenReturnNewUserAsDto() throws Exception {
        // Setup
        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setId(1);
        newUserDto.setCoinBalance(5000);
        newUserDto.setLevel(1);
        when(userService.createUser()).thenReturn(newUserDto);

        // Exercise and Verify
        mockMvc.perform(post("/users/create"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(newUserDto.getId())))
                .andExpect(jsonPath("$.coinBalance", is(newUserDto.getCoinBalance())))
                .andExpect(jsonPath("$.level", is(newUserDto.getLevel())));
    }

    @Test
    public void testUpdateLevel_whenSuccessful_thenReturnNewProgressAsDto() throws Exception {
        // Setup
        int userId = 1;
        ProgressDto progressDto = new ProgressDto();
        progressDto.setLevel(1);
        progressDto.setCoinBalance(5000);
        when(userService.updateLevel(userId)).thenReturn(progressDto);

        // Exercise and Verify
        mockMvc.perform(put("/users/updateLevel/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.level", is(progressDto.getLevel())))
                .andExpect(jsonPath("$.coinBalance", is(progressDto.getCoinBalance())));
    }

}

