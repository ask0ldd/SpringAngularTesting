package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TeacherServiceTests {
    @Autowired
    private TeacherService teacherService;

    @MockBean
    private TeacherRepository teacherRepository;

    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Teacher teacher2 = Teacher.builder().id(2L).firstName("teacher2Fn").lastName("teacher2Ln").build();

    @Test
    void whenCallingFindById_AndRecevingAnOptionalContainingATeacher_ShouldReturnATeacher() {
        // Arrange
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.of(teacher1));
        // Act
        Teacher teacher = teacherService.findById(1L);
        // Assert
        assertNotNull(teacher);
        verify(teacherRepository, times(1)).findById(1L);
        assertThat(teacher.getFirstName()).isEqualTo(teacher1.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacher1.getLastName());
    }

    @Test
    void whenCallingFindById_AndRecevingAnEmptyOptional_ShouldReturnNull() {
        // Arrange
        when(teacherRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        Teacher teacher = teacherService.findById(1L);
        // Assert
        assertNull(teacher);
        verify(teacherRepository, times(1)).findById(1L);
    }

    @Test
    void whenCallingFindAll_AndRecevingAListOfTeachers_ShouldReturnAListOfTeachers() {
        // Arrange
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));
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

    // TODO : IllegalArgumentException for both
}
