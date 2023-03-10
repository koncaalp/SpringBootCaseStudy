package com.alpkonca.rowMatch.controller;


import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController //This class is a REST controller that handles incoming HTTP requests and sends HTTP responses for user related requests.
@RequestMapping("/users")
public class UserController {

    //To provide the team service to the controller
    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // HTTP POST request to create a new user
    @PostMapping("/create")
    @Operation(summary = "Creates a new user")
    public ResponseEntity<NewUserDto> createUser(){
        return new ResponseEntity<>(userService.createUser(), HttpStatus.CREATED); // Invoke the user service to create the user and return the user mapped to NewUserDto, set the HTTP status to CREATED
    }

    // HTTP PUT request to level up the user
    @PutMapping("/updateLevel/{id}")
    @Operation(summary = "Levels up the user")
    public ResponseEntity<ProgressDto> updateLevel(@PathVariable("id") int userId){
        return new ResponseEntity<>(userService.updateLevel(userId), HttpStatus.OK); // Invoke the user service to level up the user and return the user mapped to ProgressDto, set the HTTP status to OK
    }

}
