package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    // TODO : should bcrypt the password

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();

    @Test
    void whenCallingFindById_AndReceivingAnOptionalContainingAUser_ShouldReturnAUser() {
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
    void whenCallingFindById_AndReceivingAnEmptyOptional_ShouldReturnNull() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        User user = userService.findById(1L);
        // Assert
        assertNull(user);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void whenCallingDelete_ShouldCallTheDeleteIdMethodOfTheUserRepo() {
        // Arrange
        // needs to use this syntax cause when(userRepository.deleteById(anyLong())).doNothing(); generates an IDE alert
        // when trying to mock methods returning void
        doNothing().when(userRepository).deleteById(anyLong());
        // Act
        userService.delete(1L);
        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }

    // TODO : IllegalArgumentException for both
}
