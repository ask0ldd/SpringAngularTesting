package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TeacherTests {

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Teacher teacher1copy = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Teacher teacher2 = Teacher.builder().id(2L).firstName("teacher2Fn").lastName("teacher2Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();

    @Test
    void testSetters() {
        // Arrange
        Teacher teacher = Teacher.builder()
                .id(2L)
                .firstName("")
                .lastName("")
                .build();
        // Act
        teacher.setId(teacher1.getId());
        teacher.setFirstName(teacher1.getFirstName());
        teacher.setLastName(teacher1.getLastName());
        teacher.setCreatedAt(teacher1.getCreatedAt());
        teacher.setUpdatedAt(teacher1.getUpdatedAt());
        // Assert
        assertThat(teacher.getId()).isEqualTo(teacher1.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacher1.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacher1.getLastName());
        assertThat(teacher.getCreatedAt()).isEqualTo(teacher1.getCreatedAt());
        assertThat(teacher.getUpdatedAt()).isEqualTo(teacher1.getUpdatedAt());
    }

    @Test
    void testGetters(){
        assertThat(teacher1.getId()).isEqualTo(1L);
        assertThat(teacher1.getFirstName()).isEqualTo("teacher1Fn");
        assertThat(teacher1.getLastName()).isEqualTo("teacher1Ln");
        assertThat(teacher1.getCreatedAt()).isNull();
        assertThat(teacher1.getUpdatedAt()).isNull();
    }

    @Test
    void testToString(){
        assertThat(teacher1.toString().contains("teacher1Fn")).isTrue();
    }

    @Test
    void testEqual(){
        assertThat(teacher1.equals(teacher1copy)).isTrue();
        assertThat(teacher1.equals(teacher2)).isFalse();
    }

    @Test
    void testConstructors(){
        Teacher teacherEmptyArgs = new Teacher();
        Teacher teacherFullArgs = new Teacher(1L, "lastname", "firstname", LocalDateTime.now(), LocalDateTime.now());
        assertThat(teacherEmptyArgs.getClass()).isEqualTo(Teacher.class);
        assertThat(teacherFullArgs.getClass()).isEqualTo(Teacher.class);
    }

    @Test
    void testBuilder(){
        String teacherAsString = Teacher.builder().firstName("firstname").lastName("lastname").toString();
        assertThat(teacherAsString.contains("firstname")).isTrue();
    }
}
