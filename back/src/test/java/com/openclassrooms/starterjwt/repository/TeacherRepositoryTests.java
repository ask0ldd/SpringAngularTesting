package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// 4 Tests

// @DataJpaTest
@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class TeacherRepositoryTests {

    @Autowired
    private TeacherRepository teacherRepository;

    private String teacher1Fn = "Margot";
    private String teacher1Ln = "DELAHAYE";
    private String teacher2Fn = "Helene";
    private String teacher2Ln = "THIERCELIN";

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("when trying to find an existing teacher, an optional with said teacher should be returned")
    void testFindById_ReturnOptionWithTeacher(){
        // Act
        Teacher teacher1 = teacherRepository.findById(1L).orElse(null);
        // Assert
        assertThat(teacher1).isNotNull();
        assertThat(teacher1.getId()).isEqualTo(1L);
        assertThat(teacher1.getFirstName()).isEqualTo(teacher1Fn);
        assertThat(teacher1.getLastName()).isEqualTo(teacher1Ln);
    }

    @Test
    @DisplayName("when trying to find a non existent user, an empty optional should be returned")
    void testFindById_TeacherDoesntExist_ReturnEmptyOptional(){
        // Act
        Optional<Teacher> teacher1 = teacherRepository.findById(3L);
        // Assert
        assertThat(teacher1.isEmpty()).isTrue();
    }

    // -------
    // FindAll
    // -------

    @Test
    @DisplayName("when looking for all teachers in a populated table, an array of teachers should be returned")
    void testFindAll_TeacherExists_ReturnArrayOfTeachers(){
        // Act
        List<Teacher> teacherList = teacherRepository.findAll();
        // Assert
        assertThat(teacherList.get(0)).isNotNull();
        assertThat(teacherList.get(0).getId()).isEqualTo(1L);
        assertThat(teacherList.get(0).getFirstName()).isEqualTo(teacher1Fn);
        assertThat(teacherList.get(0).getLastName()).isEqualTo(teacher1Ln);
        assertThat(teacherList.get(1)).isNotNull();
        assertThat(teacherList.get(1).getId()).isEqualTo(2L);
        assertThat(teacherList.get(1).getFirstName()).isEqualTo(teacher2Fn);
        assertThat(teacherList.get(1).getLastName()).isEqualTo(teacher2Ln);
    }

    @Test
    @DisplayName("when looking for all users in an empty table, an empty array should be returned")
    void testFindAll_TeachersDontExist_ReturnAnEmptyArray(){
        // Act
        teacherRepository.deleteAll();
        List<Teacher> teacherList = teacherRepository.findAll();
        // Assert
        assertThat(teacherList.size()).isEqualTo(0);
    }
}