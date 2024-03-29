package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.model.Team;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;


// User service interface to achieve abstraction and loose coupling
public interface UserService {

    NewUserDto createUser();
    ProgressDto updateLevel(int userId);
    boolean checkBalance(int userId, int requiredCoinBalance);
    boolean isMemberOfTeam(int userId);
    void setTeam(int userId, Team team);
    void deductFromBalance(int userId, int balanceToDeduct);

}
