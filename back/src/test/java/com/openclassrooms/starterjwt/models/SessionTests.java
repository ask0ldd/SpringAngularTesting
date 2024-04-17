package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class SessionTests {

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session1copy = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();

    @Test
    void testSetters() {
        // Arrange
        Session session = Session.builder()
                .name("")
                .date(new Date())
                .description("")
                .teacher(new Teacher())
                .users(new ArrayList<>())
                .build();
        // Act
        session.setId(session1.getId());
        session.setName(session1.getName());
        session.setDate(session1.getDate());
        session.setDescription(session1.getDescription());
        session.setTeacher(session1.getTeacher());
        session.setUsers(session1.getUsers());
        session.setCreatedAt(session1.getCreatedAt());
        session.setUpdatedAt(session1.getUpdatedAt());
        // Assert
        assertThat(session.getId()).isEqualTo(session1.getId());
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDate()).isEqualTo(session1.getDate());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
        assertThat(session.getTeacher()).isEqualTo(session1.getTeacher());
        assertThat(session.getUsers()).isEqualTo(session1.getUsers());
        assertThat(session.getCreatedAt()).isEqualTo(session1.getCreatedAt());
        assertThat(session.getUpdatedAt()).isEqualTo(session1.getUpdatedAt());
    }

    @Test
    void testGetters(){
        assertThat(session1.getId()).isEqualTo(1L);
        assertThat(session1.getName()).isEqualTo("session1Name");
        assertThat(session1.getDate()).isNotNull();
        assertThat(session1.getDescription()).isEqualTo("session1Description");
        assertThat(session1.getTeacher()).isEqualTo(teacher1);
        assertThat(session1.getUsers()).isEqualTo(new ArrayList<>((Arrays.asList(user1, user2))));
        assertThat(session1.getCreatedAt()).isNull();
        assertThat(session1.getUpdatedAt()).isNull();
    }

    @Test
    void testToString(){
        assertThat(session1.toString().contains("session1Name")).isTrue();
    }

    @Test
    void testEqual(){
        assertThat(session1.equals(session1copy)).isTrue();
        assertThat(session1.equals(session2)).isFalse();
    }

    @Test
    void testConstructors(){
        Session session = new Session();
        assertThat(session.getClass()).isEqualTo(Session.class);
    }

    @Test
    void testBuilder(){
        String sessionAsString = Session.builder().name("sessionName").description("sessionDescription").date(new Date()).users(new ArrayList<>((Arrays.asList(user1, user2)))).teacher(teacher1).toString();
        assertThat(sessionAsString.contains("sessionDescription")).isTrue();
    }
}
