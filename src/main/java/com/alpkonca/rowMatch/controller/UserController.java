package com.alpkonca.rowMatch.controller;

import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<NewUserDto> createUser(){
        return new ResponseEntity<NewUserDto>(userService.createUser(), HttpStatus.CREATED);
    }

    @PutMapping("/updateLevel/{id}")
    public ResponseEntity<ProgressDto> updateLevel(@PathVariable("id") int userId){
        return new ResponseEntity<>(userService.updateLevel(userId), HttpStatus.OK);
    }

}
