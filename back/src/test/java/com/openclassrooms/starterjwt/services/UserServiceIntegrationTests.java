package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// 3 Tests

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class UserServiceIntegrationTests {
    @Autowired
    UserService userService;
    @SpyBean
    UserRepository userRepository;

    private final User user1 = User.builder().id(1L).admin(false).email("yoga@studio.com").firstName("Admin").lastName("Admin").password("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq").build();

    @BeforeEach
    void resetSpyCount(){ // reset SpyBean counts
        Mockito.reset(userRepository);
    }

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("When the user targeted by .findById() exists, said user should be returned")
    void testFindById_UserExists_ReturnAUser() {
        // Act
        User user = userService.findById(1L);
        // Assert
        assertThat(user).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        assertThat(user.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(user.getLastName()).isEqualTo(user1.getLastName());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
    }

    @Test
    @DisplayName("When the teacher targeted by .findById() doesnt exist, null should be returned")
    void testFindById_UserDoesntExist_ReturnNull() {
        // Act
        User user = userService.findById(2L);
        // Assert
        assertThat(user).isNull();
        verify(userRepository, times(1)).findById(2L);
    }

    // -------
    // Delete
    // -------

    @Test
    @DisplayName("When the user targeted by .delete() exists, repo.deleteById() should be called")
    void testDelete_TheDeleteByIdMethodOfTheRepoIsCalled() {
        // Arrange
        User user = userRepository.findById(1L).orElse(null);
        assertThat(user).isNotNull();
        // Act
        userService.delete(1L);
        // Assert
        verify(userRepository, times(1)).deleteById(1L);
        User userAfterDeletion = userRepository.findById(1L).orElse(null);
        assertThat(userAfterDeletion).isNull();
    }
}
