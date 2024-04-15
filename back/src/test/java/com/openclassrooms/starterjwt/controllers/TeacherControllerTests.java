package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// 5 Tests

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTests {
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;
    @InjectMocks
    private TeacherController teacherController;

    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Teacher teacher2 = Teacher.builder().id(2L).firstName("teacher2Fn").lastName("teacher2Ln").build();
    private final TeacherDto teacher1Dto = new TeacherDto();
    private final TeacherDto teacher2Dto = new TeacherDto();

    public TeacherControllerTests(){
        teacher1Dto.setFirstName(teacher1.getFirstName());
        teacher1Dto.setLastName(teacher1.getLastName());
        teacher1Dto.setId(teacher1.getId());
        teacher2Dto.setFirstName(teacher2.getFirstName());
        teacher2Dto.setLastName(teacher2.getLastName());
        teacher2Dto.setId(teacher2.getId());
    }

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("when service.findById returns a teacher, ctrlr.findById should return a 200 Success response with the teacher")
    void testFindById_ValidTeacher(){
        when(teacherService.findById(anyLong())).thenReturn(teacher1);
        when(teacherMapper.toDto(any(Teacher.class))).thenReturn(teacher1Dto);
        ResponseEntity<?> response = teacherController.findById(teacher1.getId().toString());
        verify(teacherService, times(1)).findById(teacher1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teacher1Dto);
    }

    @Test
    @DisplayName("when service.findById hasn't been able to retrieve the target teacher, ctrlr.findById should return a 404 Not Found response")
    void testFindById_TeacherNotFound_404() {
        when(teacherService.findById(anyLong())).thenReturn(null);
        ResponseEntity<?> response = teacherController.findById(teacher1.getId().toString());
        verify(teacherService, times(1)).findById(teacher1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("when the teacherId passed to the controller is malformed, ctrlr.findById should return a 400 Bad Request response")
    void testFindById_InvalidTeacherId() {
        String invalidTeacherId = "aaa";
        ResponseEntity<?> response = teacherController.findById(invalidTeacherId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(teacherService, never()).findById(anyLong());
    }

    // -------
    // FindAll
    // -------

    @Test
    @DisplayName("when service.findAll is returning an array of 2 teachers, ctrlr.findAll should return a 200 Success response with two teachers")
    void testFindAll_ExistingTeachers(){
        List<Teacher> teachersList = Arrays.asList(teacher1, teacher2);
        List<TeacherDto> teachersDtoList = Arrays.asList(teacher1Dto, teacher2Dto);
        when(teacherService.findAll()).thenReturn(teachersList);
        when(teacherMapper.toDto(anyList())).thenReturn(teachersDtoList);
        ResponseEntity<?> response = teacherController.findAll();
        verify(teacherService, times(1)).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teachersDtoList);
    }

    @Test
    @DisplayName("when service.findAll is returning an empty array, ctrlr.findAll should return a 200 Success response with an empty array")
    void testFindAll_NoTeachers(){
        List<Teacher> teachersEmptyList = Collections.emptyList();
        List<TeacherDto> teachersDtoEmptyList = Collections.emptyList();
        when(teacherService.findAll()).thenReturn(teachersEmptyList);
        when(teacherMapper.toDto(anyList())).thenReturn(teachersDtoEmptyList);
        ResponseEntity<?> response = teacherController.findAll();
        verify(teacherService, times(1)).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(teachersDtoEmptyList);
    }
}
