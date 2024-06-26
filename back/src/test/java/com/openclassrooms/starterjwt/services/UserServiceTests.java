package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// 3 Tests

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("When the user targeted by .findById() exists, said user should be returned")
    void testFindbyId_TargetUserExists_ShouldReturnAUser() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        // Act
        User user = userService.findById(1L);
        // Assert
        assertNotNull(user);
        verify(userRepository, times(1)).findById(1L);
        assertThat(user.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(user.getLastName()).isEqualTo(user1.getLastName());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    @DisplayName("When the user targeted by .findById() doesnt exist, null should be returned")
    void testFindbyId_TargetUserDoesntExist_ShouldReturnNull() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        User user = userService.findById(1L);
        // Assert
        assertNull(user);
        verify(userRepository, times(1)).findById(1L);
    }

    // -------
    // Delete
    // -------

    @Test
    @DisplayName("When .delete() is called with a userId, repo.deleteById() should be called")
    void testDelete_TheDeleteIdMethodOfTheUserRepoShouldBeCalled() {
        // Arrange
        // needs to use this syntax cause when(userRepository.deleteById(anyLong())).doNothing(); generates an IDE alert
        // when trying to mock methods returning void
        doNothing().when(userRepository).deleteById(anyLong());
        // Act
        userService.delete(1L);
        // Assert : repo.deletebyid should have been called with service.delete passed parameter
        verify(userRepository, times(1)).deleteById(1L);
    }
}
