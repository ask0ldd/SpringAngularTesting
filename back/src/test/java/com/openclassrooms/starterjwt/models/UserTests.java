package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserTests {

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user1copy = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();

    @Test
    void testAllConstructor(){ // User(String, String, String, String, boolean)
        // Arrange & Act
        User user = new User(user1.getId(), user1.getEmail(), user1.getLastName(), user1.getFirstName(), user1.getPassword(), user1.isAdmin(), LocalDateTime.now(), LocalDateTime.now());
        // Assert
        assertThat(user.getId()).isEqualTo(user1.getId());
        assertThat(user.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(user.getLastName()).isEqualTo(user1.getLastName());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
        assertThat(user.getPassword()).isEqualTo(user1.getPassword());
    }

    @Test
    void testRequiredAllConstructor(){ // User(String, String, String, String, boolean)
        // Arrange & Act
        User user = new User(user1.getEmail(), user1.getLastName(), user1.getFirstName(), user1.getPassword(), user1.isAdmin());
        // Assert
        assertThat(user.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(user.getLastName()).isEqualTo(user1.getLastName());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
        assertThat(user.getPassword()).isEqualTo(user1.getPassword());
    }

    @Test
    void testSetters() {
        // Arrange
        User user = User.builder()
                .id(2L)
                .firstName("")
                .lastName("")
                .email("")
                .admin(false)
                .password("")
                .build();
        // Act
        user.setId(user1.getId());
        user.setFirstName(user1.getFirstName());
        user.setLastName(user1.getLastName());
        user.setAdmin(user1.isAdmin());
        user.setPassword(user1.getPassword());
        user.setEmail(user1.getEmail());
        user.setCreatedAt(user1.getCreatedAt());
        user.setUpdatedAt(user1.getUpdatedAt());
        // Assert
        assertThat(user.getId()).isEqualTo(user1.getId());
        assertThat(user.getFirstName()).isEqualTo(user1.getFirstName());
        assertThat(user.getLastName()).isEqualTo(user1.getLastName());
        assertThat(user.getPassword()).isEqualTo(user1.getPassword());
        assertThat(user.getEmail()).isEqualTo(user1.getEmail());
        assertThat(user.isAdmin()).isEqualTo(user1.isAdmin());
        assertThat(user.getCreatedAt()).isEqualTo(user1.getCreatedAt());
        assertThat(user.getUpdatedAt()).isEqualTo(user1.getUpdatedAt());
    }

    @Test
    void testConstraints() {
        // Arrange
        User user = new User();
        String aaaas = String.valueOf(new char[125]).replace("\0", "a");
        // Act & Assert
        assertThrows(NullPointerException.class, () -> user.setFirstName(null));
        assertThrows(NullPointerException.class, () -> user.setLastName(null));
        assertThrows(NullPointerException.class, () -> user.setEmail(null));
        assertThrows(NullPointerException.class, () -> user.setPassword(null));
    }

    @Test
    void testGetters(){
        assertThat(user1.getId()).isEqualTo(1L);
        assertThat(user1.getFirstName()).isEqualTo("user1Fn");
        assertThat(user1.getLastName()).isEqualTo("user1Ln");
        assertThat(user1.getPassword()).isEqualTo("aeazezeaeazeae");
        assertThat(user1.getEmail()).isEqualTo("user1@ced.com");
        assertThat(user1.isAdmin()).isFalse();
        assertThat(user1.getCreatedAt()).isNull();
        assertThat(user1.getUpdatedAt()).isNull();
    }

    @Test
    void testToString(){
        assertThat(user1.toString().contains("user1Fn")).isTrue();
    }

    @Test
    void testEqual(){
        assertThat(user1.equals(user1copy)).isTrue();
        assertThat(user1.equals(user2)).isFalse();
    }

    @Test
    void testConstructors(){
        User userEmptyArgs = new User();
        User userFullArgs = new User(1L, "ced@ced.com", "lastname", "firstname", "password", false, LocalDateTime.now(), LocalDateTime.now());
        User userRequiredArgs = new User("ced@ced.com", "lastname", "firstname", "password", false);
        assertThat(userEmptyArgs.getClass()).isEqualTo(User.class);
        assertThat(userFullArgs.getClass()).isEqualTo(User.class);
        assertThat(userRequiredArgs.getClass()).isEqualTo(User.class);
    }

    @Test
    void testBuilder(){
        String userAsString = User.builder().password("password").email("email@email.com").firstName("firstname").lastName("lastname").toString();
        assertThat(userAsString.contains("email@email.com")).isTrue();
    }
}
