package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

// 4 Tests

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class TeacherServiceIntegrationTests {
    @Autowired
    private TeacherService teacherService;
    @SpyBean
    private TeacherRepository teacherRepository;

    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("Margot").lastName("DELAHAYE").build();
    private final Teacher teacher2 = Teacher.builder().id(2L).firstName("Helene").lastName("THIERCELIN").build();

    @BeforeEach
    void resetSpyCount(){ // reset SpyBean counts
        Mockito.reset(teacherRepository);
    }

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("When the teacher targeted by .findById() exists, said teacher should be returned")
    void testFindById_TeacherExists_ReturnATeacher() {
        // Act
        Teacher teacher = teacherService.findById(1L);
        // Assert
        assertThat(teacher).isNotNull();
        verify(teacherRepository, times(1)).findById(1L);
        assertThat(teacher.getFirstName()).isEqualTo(teacher1.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacher1.getLastName());
    }

    @Test
    @DisplayName("When the teacher targeted by .findById() doesn't exist, null should be returned")
    void testFindById_TeacherDoesntExists_ShouldReturnNull() {
        // Act
        Teacher teacher = teacherService.findById(3L);
        // Assert
        assertThat(teacher).isNull();
        verify(teacherRepository, times(1)).findById(3L);
    }

    // -------
    // FindALl
    // -------

    @Test
    @DisplayName("When multiple teachers exist in DB, an array of teachers should be returned")
    void testFindAll_MultipleTeachersExist_ReturnAListOfTeachers() {
        // Act
        List<Teacher> teachers = teacherService.findAll();
        // Assert
        assertThat(teachers.size()).isEqualTo(2);
        verify(teacherRepository, times(1)).findAll();
        assertThat(teachers.get(0).getFirstName()).isEqualTo(teacher1.getFirstName());
        assertThat(teachers.get(0).getLastName()).isEqualTo(teacher1.getLastName());
        assertThat(teachers.get(1).getFirstName()).isEqualTo(teacher2.getFirstName());
        assertThat(teachers.get(1).getLastName()).isEqualTo(teacher2.getLastName());
    }

    @Test
    @DisplayName("When no teachers exist in DB, an empty array should be returned")
    void testFindAll_NoTeachersInDB_ReturnAnEmptyArray() {
        // Act
        teacherRepository.deleteAll();
        List<Teacher> teachers = teacherService.findAll();
        // Assert
        assertThat(teachers.size()).isEqualTo(0);
    }

}
