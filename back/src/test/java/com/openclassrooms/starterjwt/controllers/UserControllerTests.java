package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import org.mockito.Mock;

// 7 Tests

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserController userController;

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(true).email("ced2@ced.com").firstName("jane").lastName("doe").password("aeazezeaeazeae2").build();
    private final UserDto userDto = new UserDto();

    private UserControllerTests() {
        userDto.setId(user1.getId());
        userDto.setAdmin(user1.isAdmin());
        userDto.setLastName(user1.getLastName());
        userDto.setFirstName(user1.getFirstName());
        userDto.setEmail(user1.getEmail());
        userDto.setPassword(user1.getPassword());
    }

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("when service.findById returns a user, ctrlr.findById should return a 200 Success response with the user")
    void testFindById_ValidUser_200() {
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
    @DisplayName("when service.findById hasn't been able to retrieve the target user, ctrlr.findById should return a 404 Not Found response")
    void testFindById_UserNotFound_404() {
        // Arrange
        when(userService.findById(anyLong())).thenReturn(null);
        // Act
        ResponseEntity<?> response = userController.findById(user1.getId().toString());
        // Assert
        verify(userService, times(1)).findById(user1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("when the userId passed to the controller is malformed, ctrlr.findById should return a 400 Bad Request response")
    void testFindById_InvalidUserId_400() {
        // Arrange
        String invalidUserId = "abc";
        // Act
        ResponseEntity<?> response = userController.findById(invalidUserId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // -------
    // Delete // !!! error method name in original code, save instead of delete !!!!
    // -------

    @Test
    @DisplayName("when service.delete returns no value, ctrlr.delete(save) should return a 200 Success response")
    void testDelete_ExistingUser_200(){
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
    @DisplayName("when the userId passed to the ctrlr doesnt exist, ctrlr.delete(save) should return a 404 Not Found response")
    public void testDeleteUser_UserNotFound_400() {
        // Arrange
        String userId = "1";
        when(userService.findById(anyLong())).thenReturn(null);
        // Act
        ResponseEntity<?> response = userController.save(userId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("when the userId passed is malformed, ctrlr.delete(save) should return a 400 Bad Request response")
    public void testDeleteUser_InvalidId() {
        // Arrange
        String userId = "invalid";
        // Act
        ResponseEntity<?> response = userController.save(userId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("when the id of the user trying to delete doesnt match with the id of the owern of the account, ctrlr.delete(save) should return a 401 Unauthorized response")
    public void testDeleteUser_TheLoggedUserIsTryingToDeleteAnotherUserProfile() {
        // Arrange
        // user1 as Principal
        UserDetails userDetails = UserDetailsImpl.builder()
                .admin(false)
                .username(user1.getEmail())
                .password(user1.getPassword())
                .lastName(user1.getLastName())
                .firstName(user1.getFirstName())
                .id(1L)
                .build();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // asking to delete the user2 profile
        when(userService.findById(anyLong())).thenReturn(user2);
        // Act
        ResponseEntity<?> response = userController.save(user2.getId().toString());
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

}
