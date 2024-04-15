package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// 4 Tests

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {
    @InjectMocks
    AuthController authController;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    UserRepository userRepository;
    @Mock
    Authentication authentication;
    @Mock
    AuthenticationManager authenticationManager;

    private final User user1 = User.builder().id(1L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();

    // -------
    // Login
    // -------

    @Test
    @DisplayName("When a user log in with valid credentials, ctrlr.authenticateUser should return a 200 success response with a jwt")
    void testLogin_ValidUser() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user1.getEmail());
        loginRequest.setPassword(user1.getPassword());
        UserDetailsImpl userDetails = new UserDetailsImpl(user1.getId(), user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateJwtToken(any(Authentication.class))).thenReturn("test_jwt_token");
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        // Act
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        // Assert
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(any(Authentication.class));
        verify(authentication, times(1)).getPrincipal();
        verify(userRepository, times(1)).findByEmail(anyString());
        assertThat(response).isNotNull();
        assertThat(response.getBody() instanceof JwtResponse).isNotNull();
        JwtResponse responseBody = (JwtResponse) response.getBody();
        assert responseBody != null;
        assertThat(responseBody.getToken()).isEqualTo("test_jwt_token");
        assertThat(responseBody.getId().longValue()).isEqualTo(user1.getId());
        assertThat(responseBody.getUsername()).isEqualTo(user1.getEmail());
        assertThat(responseBody.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(responseBody.getLastName()).isEqualTo(user1.getLastName());
        assertThat(responseBody.getAdmin()).isEqualTo(user1.isAdmin());
    }

    @Test
    @DisplayName("When a user tries to log in with invalid credentials, a BadCredentialsException should be thrown")
    void testLogin_InvalidCredentials() throws BadCredentialsException {
        // Arrange
        // loginRequest contains an invalid password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user1.getEmail());
        loginRequest.setPassword("invalidPassword");
        // userDetails contains all the user1 infos
        UserDetailsImpl userDetails = new UserDetailsImpl(user1.getId(), user1.getEmail(),  user1.getFirstName(), user1.getLastName(), user1.isAdmin(), user1.getPassword());
        doThrow(new BadCredentialsException("Invalid username or password")).when(authenticationManager).authenticate(any());
        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            ResponseEntity<?> response = authController.authenticateUser(loginRequest);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        });
        verify(authenticationManager, times(1)).authenticate(any());
        verify(userRepository, never()).findByEmail(anyString());
    }

    // -------
    // Register
    // -------

    @Test
    @DisplayName("When a user registers with an email already in DB, ctrlr.registerUser should return a 400 Bad Request response with the expected error message")
    void testRegisterUser_AlreadyExistingUser() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(true);
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("ezaezaezeza@casaze.com");
        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        // Assert
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(userRepository, never()).save(any(User.class));
        assertThat(response).isNotNull();
        assertThat(response.getBody() instanceof MessageResponse).isTrue();
        assert response.getBody() != null;
        assertThat(((MessageResponse) response.getBody()).getMessage()).isEqualTo("Error: Email is already taken!");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("When a user registers with valid datas, ctrlr.registerUser should return a 200 success response with the expected message")
    void testRegisterUser_ValidUser() {
        // Arrange
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(user1.getEmail());
        signupRequest.setFirstName(user1.getFirstName());
        signupRequest.setLastName(user1.getLastName());
        signupRequest.setPassword(user1.getPassword());
        // Act
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        // Assert
        verify(userRepository, times(1)).existsByEmail(anyString());
        verify(passwordEncoder, times(1)).encode(user1.getPassword());
        assertThat(response).isNotNull();
        assertThat(response.getBody() instanceof MessageResponse).isTrue();
        assert response.getBody() != null;
        assertThat(((MessageResponse) response.getBody()).getMessage()).isEqualTo("User registered successfully!");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}