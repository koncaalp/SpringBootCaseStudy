package com.alpkonca.rowMatch.service;

import com.alpkonca.rowMatch.exception.ResourceWithIdNotFoundException;
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

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private Configuration configuration;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User(1, 1, 5000,0);
    }

    @Test
    public void testCreateUser_whenSuccessful_thenReturnNewUserAsDto() {
        // Arrange
        when(configuration.getStartingCoinBalance()).thenReturn(user.getCoinBalance());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        NewUserDto result = userService.createUser();

        // Assert
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getCoinBalance(), result.getCoinBalance());
        assertEquals(user.getLevel(), result.getLevel());
    }
    @Test
    public void testUpdateLevel_whenSuccessful_thenReturnUpdatedProgressOfUser() {
        // Arrange
        when(configuration.getCoinPerLevel()).thenReturn(25);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        ProgressDto result = userService.updateLevel(user.getId());

        // Assert
        assertEquals(2, result.getLevel());
        assertEquals(5025, result.getCoinBalance());
    }
    @Test
    public void testUpdateLevel_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        // Assert & Act
        assertThrows(ResourceWithIdNotFoundException.class, () -> userService.updateLevel(user.getId()));
    }
    @Test
    public void testCheckBalance_whenUserHasSufficientBalance_thenReturnTrue() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.checkBalance(user.getId(), 1000);

        // Assert
        assertEquals(true, result);
    } @Test
    public void testCheckBalance_whenUserHasInsufficientBalance_thenReturnFalse() {
        // Arrange
        user.setCoinBalance(500);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // Act
        boolean result = userService.checkBalance(user.getId(), 1000);

        // Assert
        assertEquals(false, result);
    }
    @Test
    public void testCheckBalance_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundExcepion() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        // Assert & Act
        assertThrows(ResourceWithIdNotFoundException.class, () -> userService.checkBalance(user.getId(), 1000));
    }
    @Test
    public void testIsMemberOfTeam_whenUserDoesNotHaveATeam_thenReturnFalse() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // Act
        boolean result = userService.isMemberOfTeam(user.getId());
        // Assert
        assertEquals(false, result);
    }
    @Test
    public void testIsMemberOfTeam_whenUserHasTeam_thenReturnTrue() {
        // Arrange
        user.setTeamId(1);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        // Act
        boolean result = userService.isMemberOfTeam(user.getId());
        // Assert
        assertEquals(true, result);
    }
    @Test
    public void testIsMemberOfTeam_whenUserDoesNotExist_thenThrowResourceWithIdNotFoundException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceWithIdNotFoundException.class, () -> userService.isMemberOfTeam(user.getId()));
    }


}
