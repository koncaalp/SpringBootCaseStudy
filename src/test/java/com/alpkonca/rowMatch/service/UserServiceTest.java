package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.exception.ResourceWithFieldNotFoundException;
import com.alpkonca.rowMatch.model.Configuration;
import com.alpkonca.rowMatch.model.User;
import com.alpkonca.rowMatch.payload.NewUserDto;
import com.alpkonca.rowMatch.payload.ProgressDto;
import com.alpkonca.rowMatch.repository.UserRepository;
import com.alpkonca.rowMatch.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Unit Test Class for UserService
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    // To provide mock implementations of the configuration and user repository in order to achieve isolation
    @Mock
    private Configuration configuration;
    @Mock
    private UserRepository userRepository;

    // To inject the mock implementations of the configuration and user repository into the user service
    @InjectMocks
    private UserServiceImpl userService;

    // So that the test methods can reach the initialized user object in the setUp method
    private User user;

    // To initialize the user object before each test method
    @BeforeEach
    public void setUp() {
        user = new User(1, 1, 5000,0);
    }

    // Test to check if the createUser method is handled correctly when the input is valid
    @Test
    public void testCreateUser_whenSuccessful_thenReturnNewUserAsDto() {
        // Arrange
        // To provide mock implementations of the configuration and user repository methods
        when(configuration.getStartingCoinBalance()).thenReturn(user.getCoinBalance());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        NewUserDto result = userService.createUser(); // Call the createUser method of the user service

        // Assert
        // Check if the returned DTO object has the same values except the teamId as the user object that was initialized in the setUp method
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getCoinBalance(), result.getCoinBalance());
        assertEquals(user.getLevel(), result.getLevel());
    }

    // Test to check if the UpdateLevel method is handled correctly when the input is valid
    @Test
    public void testUpdateLevel_whenSuccessful_thenReturnUpdatedProgressOfUser() {
        // Arrange
        // To provide mock implementations of the configuration and user repository methods
        when(configuration.getCoinPerLevel()).thenReturn(25);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        ProgressDto result = userService.updateLevel(user.getId()); // Call the updateLevel method of the user service with the userId

        // Assert
        // Check if the returned DTO object has the incremented level and the updated coin balance according to the configuration
        assertEquals(2, result.getLevel());
        assertEquals(5025, result.getCoinBalance());
    }

    // Test to check if the UpdateLevel method is handled correctly when the user does not exist
    @Test
    public void testUpdateLevel_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty()); // To provide mock implementation of the user repository method to return an empty object when the findById method is called with the userId

        // Assert & Act
        assertThrows(ResourceWithFieldNotFoundException.class, () -> userService.updateLevel(user.getId())); // Check if the ResourceWithFieldNotFoundException is thrown when the updateLevel method is called with the userId
    }

    // Test to check if the CheckBalance method is handled correctly when the user has sufficient balance for performing the action
    @Test
    public void testCheckBalance_whenUserHasSufficientBalance_thenReturnTrue() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // To provide mock implementation of the user repository method to return the user object when the findById method is called with the userId

        // Act
        boolean result = userService.checkBalance(user.getId(), 1000); // Call the checkBalance method of the user service with the userId and the cost of the action

        // Assert
        assertEquals(true, result); // Check if the returned boolean value is true
    }

    // Test to check if the CheckBalance method is handled correctly when the user does not have sufficient balance for performing the action
    @Test
    public void testCheckBalance_whenUserHasInsufficientBalance_thenReturnFalse() {
        // Arrange
        user.setCoinBalance(500);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // To provide mock implementation of the user repository method to return the user object when the findById method is called with the userId

        // Act
        boolean result = userService.checkBalance(user.getId(), 1000); // Call the checkBalance method of the user service with the userId and the cost of the action which is greater than the user's coin balance

        // Assert
        assertEquals(false, result); // Check if the returned boolean value is false
    }

    // Test to check if the CheckBalance method is handled correctly when the user does not exist
    @Test
    public void testCheckBalance_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty()); // To provide mock implementation of the user repository method to return an empty object when the findById method is called with the userId

        // Assert & Act
        assertThrows(ResourceWithFieldNotFoundException.class, () -> userService.checkBalance(user.getId(), 1000)); // Check if the ResourceWithFieldNotFoundException is thrown when the checkBalance method is called with a non-existent userId
    }

    // Test to check if the IsMemberOfTeam method is handled correctly when the user does not have a team
    @Test
    public void testIsMemberOfTeam_whenUserDoesNotHaveATeam_thenReturnFalse() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // To provide mock implementation of the user repository method to return the user object when the findById method is called with the userId

        // Act
        boolean result = userService.isMemberOfTeam(user.getId()); // Call the isMemberOfTeam method of the user service with the userId

        // Assert
        assertEquals(false, result); // Check if the returned boolean value is false
    }

    // Test to check if the IsMemberOfTeam method is handled correctly when the user has a team
    @Test
    public void testIsMemberOfTeam_whenUserHasTeam_thenReturnTrue() {
        // Arrange
        user.setTeamId(1);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user)); // To provide mock implementation of the user repository method to return the user object when the findById method is called with the userId

        // Act
        boolean result = userService.isMemberOfTeam(user.getId()); // Call the isMemberOfTeam method of the user service with the userId

        // Assert
        assertEquals(true, result); // Check if the returned boolean value is true
    }

    // Test to check if the IsMemberOfTeam method is handled correctly when the user does not exist
    @Test
    public void testIsMemberOfTeam_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty()); // To provide mock implementation of the user repository method to return an empty object when the findById method is called with a non-existent userId

        // Act & Assert
        assertThrows(ResourceWithFieldNotFoundException.class, () -> userService.isMemberOfTeam(user.getId())); // Check if the ResourceWithFieldNotFoundException is thrown when the isMemberOfTeam method is called with a non-existent userId
    }


}
