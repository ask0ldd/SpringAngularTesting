package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 2 Tests

@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class UserDetailsServiceImplIntegrationTests {

    @Autowired
    private UserRepository userRepository;
    private UserDetailsService userDetailsService;

    String user1Email = "yoga@studio.com";
    String user1Password = "$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq";

    // -------
    // LoadByUserName
    // -------

    @Test
    @DisplayName("When a user can be linked to the target email, his userDetails should be returned")
    void testLoadUserByUsername_EmailExists_ReturnSomeUserDetails(){
        // Arrange
        userDetailsService = new UserDetailsServiceImpl(userRepository);
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
        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("thisEmailDoesntExist@oc.com");
        });
    }
}