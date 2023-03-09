package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.ResourceWithIdNotFoundException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.repository.UserRepository;
import com.alpkonca.rowMatch.service.UserService;
import org.springframework.stereotype.Service;


// Service method for business logic of User
@Service
public class UserServiceImpl implements UserService {
    // To provide the user repository and configuration to the service
    private UserRepository userRepository;
    private final Configuration configuration;
    public UserServiceImpl(UserRepository userRepository, Configuration configuration) {
        this.userRepository = userRepository;
        this.configuration = configuration;
    }

    // Method to create a new user
    @Override
    public NewUserDto createUser() {
        // Create a new user with the starting coin balance from the configuration and save it to the database
        User user = new User();
        user.setCoinBalance(configuration.getStartingCoinBalance());
        User newUser = userRepository.save(user);

        // Map the user object to the response DTO in order to exclude the teamId field
        NewUserDto responseDto = new NewUserDto();
        responseDto.setId(newUser.getId());
        responseDto.setCoinBalance(newUser.getCoinBalance());
        responseDto.setLevel(newUser.getLevel());

        return responseDto;
    }

    // Method to level up the user
    @Override
    public ProgressDto updateLevel(int userId) {
        User user = userRepository.findById(userId) // Retrieve the user with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId)); // Throw a ResourceWithIdNotFoundException if the user is not found
        user.setLevel(user.getLevel()+1); // Increment the level of the user
        user.setCoinBalance(user.getCoinBalance()+configuration.getCoinPerLevel()); // Add the coin per level fetched from the configurations to the user's coin balance
        userRepository.save(user); // Save the updated user to the database

        // Map the user object to the response DTO in order to exclude the userId and teamId fields
        ProgressDto responseDto = new ProgressDto();
        responseDto.setLevel(user.getLevel());
        responseDto.setCoinBalance(user.getCoinBalance());

        return responseDto;
    }

    // Method to check if the user has enough coin balance to perform an action
    @Override
    public boolean checkBalance(int userId, int requiredCoinBalance) {
        User user = userRepository.findById(userId) // Retrieve the user with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId)); // Throw a ResourceWithIdNotFoundException if the user is not found
        if(user.getCoinBalance() >= requiredCoinBalance){ // Return true if the user has enough coin balance, false otherwise
            return true;
        }
        else {
            return false;
        }
    }

    // Method to check if the user is already in a team
    @Override
    public boolean isMemberOfTeam(int userId) {
        User user = userRepository.findById(userId) // Retrieve the user with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId)); // Throw a ResourceWithIdNotFoundException if the user is not found
        if(user.getTeamId() == 0){ // Return false if the user is not in a team -> teamId is 0, true otherwise
            return false;
        }
        else {
            return true;
        }
    }

    // Method to set the teamId of the user
    @Override
    public void setTeam(int userId, int teamId) {
        User user = userRepository.findById(userId) // Retrieve the user with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId)); // Throw a ResourceWithIdNotFoundException if the user is not found
        // Set the teamId of the user and save it to the database
        user.setTeamId(teamId);
        userRepository.save(user);
    }

    // Method to deduct coins from the balance of the user
    @Override
    public void deductFromBalance(int userId, int balanceToDeduct) {
        User user = userRepository.findById(userId) // Retrieve the user with the given id from the database
                .orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId)); // Throw a ResourceWithIdNotFoundException if the user is not found
        // Deduct the amount set in configurations from the user's coin balance and save it to the database
        user.setCoinBalance(user.getCoinBalance()-balanceToDeduct);
        userRepository.save(user);
    }


}
