package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.ResourceWithFieldNotFoundException;
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

    // Method to level up the user; check if the user exists; increment the level and add the coin per level to the user's coin balance
    @Override
    public ProgressDto updateLevel(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("User", "id",userId)); // If the user is not found, throw a ResourceWithFieldNotFoundException, necessary since the findById method returns an Optional object
        user.setLevel(user.getLevel()+1);
        user.setCoinBalance(user.getCoinBalance()+configuration.getCoinPerLevel());
        userRepository.save(user);

        // Map the user object to the response DTO in order to exclude the userId and teamId fields
        ProgressDto responseDto = new ProgressDto();
        responseDto.setLevel(user.getLevel());
        responseDto.setCoinBalance(user.getCoinBalance());

        return responseDto;
    }

    // Method to check if the user has enough coin balance to perform an action; check if the user exists;
    // return true if the user has enough coin balance, false otherwise
    @Override
    public boolean checkBalance(int userId, int requiredCoinBalance) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("User", "id",userId)); // Throw a ResourceWithFieldNotFoundException if the user is not found, necessary since the findById method returns an Optional object
        if(user.getCoinBalance() >= requiredCoinBalance){
            return true;
        }
        else {
            return false;
        }
    }

    // Method to check if the user is already in a team; check if the user exists; return false if the user is not in a team, true otherwise
    @Override
    public boolean isMemberOfTeam(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("User", "id",userId)); // Throw a ResourceWithFieldNotFoundException if the user is not found, necessary since the findById method returns an Optional object
        if(user.getTeamId() == 0){ // Return false if the user is not in a team -> teamId is 0, true otherwise
            return false;
        }
        else {
            return true;
        }
    }

    // Method to set the teamId of the user; check if the user exists; set the teamId of the user
    @Override
    public void setTeam(int userId, int teamId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("User", "id",userId)); // Throw a ResourceWithFieldNotFoundException if the user is not found, necessary since the findById method returns an Optional object

        user.setTeamId(teamId);
        userRepository.save(user);
    }

    // Method to deduct coins from the balance of the user; check if the user exists; deduct the amount set in configurations from the user's coin balance
    @Override
    public void deductFromBalance(int userId, int balanceToDeduct) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceWithFieldNotFoundException("User", "id",userId)); // Throw a ResourceWithFieldNotFoundException if the user is not found, necessary since the findById method returns an Optional object
        user.setCoinBalance(user.getCoinBalance()-balanceToDeduct);
        userRepository.save(user);
    }


}
