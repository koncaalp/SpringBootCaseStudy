package com.alpkonca.rowMatch.service.impl;

import com.alpkonca.rowMatch.exception.ResourceWithIdNotFoundException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.repository.UserRepository;
import com.alpkonca.rowMatch.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private final Configuration configuration;

    public UserServiceImpl(UserRepository userRepository, Configuration configuration) {
        this.userRepository = userRepository;
        this.configuration = configuration;
    }

    @Override
    public NewUserDto createUser() {
        User user = new User();
        user.setCoinBalance(configuration.getStartingCoinBalance());
        User newUser = userRepository.save(user);
        NewUserDto responseDto = new NewUserDto();
        responseDto.setId(newUser.getId());
        responseDto.setCoinBalance(newUser.getCoinBalance());
        responseDto.setLevel(newUser.getLevel());

        return responseDto;
    }

    @Override
    public ProgressDto updateLevel(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId));
        user.setLevel(user.getLevel()+1);
        user.setCoinBalance(user.getCoinBalance()+25);
        userRepository.save(user);
        ProgressDto responseDto = new ProgressDto();
        responseDto.setLevel(user.getLevel());
        responseDto.setCoinBalance(user.getCoinBalance());
        return responseDto;
    }

    @Override
    public boolean checkBalance(int userId, int requiredCoinBalance) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId));
        if(user.getCoinBalance() >= requiredCoinBalance){
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean isMemberOfTeam(int userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId));
        if(user.getTeamId() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void setTeam(int userId, int teamId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId));
        user.setTeamId(teamId);
        userRepository.save(user);
    }

    @Override
    public void deductFromBalance(int userId, int balanceToDeduct) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceWithIdNotFoundException("User", "id",userId));
        user.setCoinBalance(user.getCoinBalance()-balanceToDeduct);
        userRepository.save(user);
    }


}
