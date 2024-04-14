package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

// 6 Tests

public class TeacherMapperTests {
    @InjectMocks
    private TeacherMapper teacherMapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    public void shouldMapTeacherDtoToTeacherEntity() {
        // Arrange
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
        // Act
        Teacher teacher = teacherMapper.toEntity(teacherDto);
        // Assert
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    void shouldMapTeacherEntityToDto() {
        // Arrange
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        // Act
        TeacherDto teacherDto = teacherMapper.toDto(teacher);
        // Assert
        assertThat(teacherDto).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
    }

    @Test
    void shouldReturnNullWhenMappingNullDto() {
        // Act
        Teacher teacher = teacherMapper.toEntity((TeacherDto) null);
        // Assert
        assertThat(teacher).isNull();
    }

    @Test
    void shouldReturnNullWhenMappingNullEntity() {
        // Act
        TeacherDto teacherDto = teacherMapper.toDto((Teacher) null);
        // Assert
        assertThat(teacherDto).isNull();
    }

    @Test
    void testMapToEntityList() {
        // Arrange
        List<TeacherDto> teacherDtoList = new ArrayList<>();
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setFirstName("John");
        teacherDto1.setLastName("Doe");
        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setFirstName("Jane");
        teacherDto2.setLastName("Doe");
        teacherDtoList.add(teacherDto1);
        teacherDtoList.add(teacherDto2);
        // Act
        List<Teacher> teacherList = teacherMapper.toEntity(teacherDtoList);
        // Assert
        assertThat(teacherList).isNotNull();
        assertThat(teacherList.size()).isEqualTo(2);
        assertThat(teacherList.get(0).getId()).isEqualTo(teacherDto1.getId());
        assertThat(teacherList.get(1).getId()).isEqualTo(teacherDto2.getId());
        assertThat(teacherList.get(0).getFirstName()).isEqualTo(teacherDto1.getFirstName());
        assertThat(teacherList.get(1).getFirstName()).isEqualTo(teacherDto2.getFirstName());
        assertThat(teacherList.get(0).getLastName()).isEqualTo(teacherDto1.getLastName());
        assertThat(teacherList.get(1).getLastName()).isEqualTo(teacherDto2.getLastName());
    }

    @Test
    void testMapToDtoList() {
        // Arrange
        List<Teacher> teacherList = new ArrayList<>();
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");
        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Doe");
        teacherList.add(teacher1);
        teacherList.add(teacher2);
        // Act
        List<TeacherDto> teacherDtoList = teacherMapper.toDto(teacherList);
        // Assert
        assertThat(teacherDtoList).isNotNull();
        assertThat(teacherDtoList.size()).isEqualTo(2);
        assertThat(teacherDtoList.get(0).getId()).isEqualTo(teacher1.getId());
        assertThat(teacherDtoList.get(1).getId()).isEqualTo(teacher2.getId());
        assertThat(teacherDtoList.get(0).getFirstName()).isEqualTo(teacher1.getFirstName());
        assertThat(teacherDtoList.get(1).getFirstName()).isEqualTo(teacher2.getFirstName());
        assertThat(teacherDtoList.get(0).getLastName()).isEqualTo(teacher1.getLastName());
        assertThat(teacherDtoList.get(1).getLastName()).isEqualTo(teacher2.getLastName());
    }
}
