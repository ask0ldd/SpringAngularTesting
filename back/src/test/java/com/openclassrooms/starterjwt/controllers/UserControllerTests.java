package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTests {
    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Autowired
    private UserController userController;

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();
    private final UserDto userDto = new UserDto();

    private UserControllerTests() {
        userDto.setId(user1.getId());
        userDto.setAdmin(user1.isAdmin());
        userDto.setLastName(user1.getLastName());
        userDto.setFirstName(user1.getFirstName());
        userDto.setEmail(user1.getEmail());
        userDto.setPassword(user1.getPassword());
    }

    // FIND BY ID
    @Test
    void testFindById_ValidUser() {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(user1);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);
        // Act
        ResponseEntity<?> response = userController.findById(user1.getId().toString());
        // Assert
        verify(userService, times(1)).findById(user1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);
    }

    @Test
    void testFindById_UserNotFound() {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(null);
        // Act
        ResponseEntity<?> response = userController.findById(user1.getId().toString());
        // Assert
        verify(userService, times(1)).findById(user1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindById_InvalidUserId() {
        // Arrange
        String invalidUserId = "abc";
        // Act
        ResponseEntity<?> response = userController.findById(invalidUserId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // DELETE
    @Test
    void testDelete_ExistingUser(){
        // Arrange
        String userId = "1";
        UserDetails userDetails = UserDetailsImpl.builder()
                .admin(false)
                .username(user1.getEmail())
                .password(user1.getPassword())
                .lastName(user1.getLastName())
                .firstName(user1.getFirstName())
                .id(1L)
                .build();
        when(userService.findById(anyLong())).thenReturn(user1);
        doNothing().when(userService).delete(anyLong());
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Act
        // !!! error method name in original code, save instead of delete
        ResponseEntity<?> response = userController.save(userId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService, times(1)).delete(Long.valueOf(userId));
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        // Arrange
        String userId = "1";
        when(userService.findById(anyLong())).thenReturn(null);
        // Act
        ResponseEntity<?> response = userController.save(userId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteUser_InvalidId() {
        // Arrange
        String userId = "invalid";
        // Act
        ResponseEntity<?> response = userController.save(userId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // !!! TODO : Unauthorized line
}
