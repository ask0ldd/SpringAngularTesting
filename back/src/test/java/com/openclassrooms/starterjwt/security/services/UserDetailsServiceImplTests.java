package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// 2 Tests

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTests {

    private UserDetailsService userDetailsService;
    @Mock
    private UserRepository userRepository;

    String user1Email = "yoga@studio.com";
    String user1Password = "$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq";
    private final User user1 = User.builder().id(1L).admin(true).email("yoga@studio.com").firstName("john").lastName("doe").password("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq").build();

    // -------
    // LoadByUserName
    // -------

    @Test
    @DisplayName("When a user can be linked to the target email, his userDetails should be returned")
    void testLoadUserByUsername_EmailExists_ReturnSomeUserDetails(){
        // Arrange
        userDetailsService = new UserDetailsServiceImpl(userRepository);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user1));
        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(user1Email);
        // Assert
        assertThat(userDetails.getUsername()).isEqualTo(user1Email);
        assertThat(userDetails.getPassword()).isEqualTo(user1Password);
    }

    @Test
    @DisplayName("When no user can be found using the target email, a UsernameNotFoundException should be thrown")
    void testLoadUserByUsername_EmailDoesntExist_ThrowAUsernameNotFoundException(){
        // Arrange
        userDetailsService = new UserDetailsServiceImpl(userRepository);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("thisEmailDoesntExist@oc.com");
        });
    }
}