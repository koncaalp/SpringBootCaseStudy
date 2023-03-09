package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;

import java.util.List;

// User service interface to achieve abstraction and loose coupling
public interface UserService {

    NewUserDto createUser();
    ProgressDto updateLevel(int userId);
    boolean checkBalance(int userId, int requiredCoinBalance);
    boolean isMemberOfTeam(int userId);
    void setTeam(int userId, int teamId);
    void deductFromBalance(int userId, int balanceToDeduct);

}
