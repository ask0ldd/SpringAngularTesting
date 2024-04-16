package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    private final User user2 = User.builder().id(2L).admin(true).email("ced@ced.com").firstName("john").lastName("doe").password("aeazezeaeazeae").build();

    String user1Email = "yoga@studio.com";
    String user1Password = "$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq";
    String user1Fn = "Admin";
    String user1Ln = "Admin";
    Boolean user1IsAdmin = true;

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("when trying to find an existing user, an optional with said user should be returned")
    void testFindById_UserDoesExist_ReturnAnOptionalWithUser(){
        // Act
        User user1 = userRepository.findById(1L).orElse(null);
        // Assert
        assertThat(user1).isNotNull();
        assertThat(user1.getId()).isEqualTo(1L);
        assertThat(user1.getFirstName()).isEqualTo(user1Fn);
        assertThat(user1.getLastName()).isEqualTo(user1Ln);
        assertThat(user1.getEmail()).isEqualTo(user1Email);
        assertThat(user1.getPassword()).isEqualTo(user1Password);
        assertThat(user1.isAdmin()).isEqualTo(user1IsAdmin);
    }

    @Test
    @DisplayName("when trying to find a non existent user, an empty optional should be returned")
    void testFindById_UserDoesntExist_ReturnEmptyOptional(){
        // Act
        Optional<User> user1 = userRepository.findById(2L);
        // Assert
        assertThat(user1.isEmpty()).isTrue();
    }

    // -------
    // FindAll
    // -------

    @Test
    @DisplayName("when looking for all users in a populated table, an array of users should be returned")
    void testFindAll_UserExists_ReturnArrayOfUsers(){
        // Act
        userRepository.save(user2);
        List<User> userList = userRepository.findAll();
        // Assert
        assertThat(userList.get(0)).isNotNull();
        assertThat(userList.get(0).getId()).isEqualTo(1L);
        assertThat(userList.get(0).getFirstName()).isEqualTo(user1Fn);
        assertThat(userList.get(0).getLastName()).isEqualTo(user1Ln);
        assertThat(userList.get(1)).isNotNull();
        assertThat(userList.get(1).getId()).isEqualTo(2L);
        assertThat(userList.get(1).getFirstName()).isEqualTo(user2.getFirstName());
        assertThat(userList.get(1).getLastName()).isEqualTo(user2.getLastName());
    }

    @Test
    @DisplayName("when looking for all users in an empty table, an empty array should be returned")
    void testFindAll_UsersDontExist_ReturnAnEmptyArray(){
        // Act
        userRepository.deleteAll();
        List<User> userList = userRepository.findAll();
        // Assert
        assertThat(userList.size()).isEqualTo(0);
    }

    // -------
    // Delete
    // -------

    @Test
    @DisplayName("after deleting an existing user, said user shouldnt be in DB anymore")
    void testDeleteById_ExistingUser_UserCantBeFoundAfterDeletion(){
        // Act
        User user1 = userRepository.findById(1L).orElse(null);
        // Assert
        assertThat(user1).isNotNull();
        // Act
        userRepository.deleteById(1L);
        Optional<User> userOptional = userRepository.findById(1L);
        // Assert
        assertThat(userOptional.isEmpty()).isTrue();
    }

    // -------
    // FindByEmail
    // -------

    @Test
    @DisplayName("when trying to find by email an existing user, an optional with said user should be returned")
    void testFindByEmail_UserExists_ReturnTheTargetUser(){
        // Act
        User user1 = userRepository.findByEmail("yoga@studio.com").orElse(null);
        // Assert
        assertThat(user1).isNotNull();
        assertThat(user1.getFirstName()).isEqualTo(user1Fn);
        assertThat(user1.getLastName()).isEqualTo(user1Ln);
        assertThat(user1.getEmail()).isEqualTo(user1Email);
    }

    // -------
    // ExistsByEmail
    // -------

    @Test
    @DisplayName("when trying to look if a user owns a specific email and one does, true should be returned")
    void testExistsByEmail_UserExists_ReturnTrue(){
        // Act
        Boolean doesExist = userRepository.existsByEmail("yoga@studio.com");
        // Assert
        assertThat(doesExist).isTrue();
    }

    @Test
    @DisplayName("when trying to look if a user own a specific email and none does, false should be returned")
    void testExistsByEmail_UserDoesntExists_ReturnFalse(){
        // Act
        Boolean doesExist = userRepository.existsByEmail("yogaaaaaaaaa@studio.com");
        // Assert
        assertThat(doesExist).isFalse();
    }

    // -------
    // Save
    // -------

    @Test
    @DisplayName("when trying to save a valid user, said user should be returned")
    void testSave_ValidUser_ReturnAUser(){
        // Act
        User savedUser = userRepository.save(user2);
        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(2L);
        assertThat(savedUser.getFirstName()).isEqualTo(user2.getFirstName());
        assertThat(savedUser.getLastName()).isEqualTo(user2.getLastName());
        assertThat(savedUser.getEmail()).isEqualTo(user2.getEmail());
        assertThat(savedUser.getPassword()).isEqualTo(user2.getPassword());
        assertThat(savedUser.isAdmin()).isEqualTo(user2.isAdmin());
    }
}