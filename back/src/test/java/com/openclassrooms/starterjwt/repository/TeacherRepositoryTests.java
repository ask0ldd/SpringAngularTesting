package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

// @DataJpaTest
@SpringBootTest
public class TeacherRepositoryTests {

    @Autowired
    private TeacherRepository teacherRepository;
    // private final TeacherRepository teacherRepository;
    private Teacher teacher1;
    private Teacher teacher2;

    String teacher1Fn = "John";
    String teacher1Ln = "Doe";
    String teacher2Fn = "Jane";
    String teacher2Ln = "Doe";

    /*public TeacherRepositoryTests(TeacherRepository teacherRepository){
        this.teacherRepository = teacherRepository;
    }*/

    @BeforeEach
    public void setup(){
        teacher1 = Teacher.builder()
                .firstName(teacher1Fn)
                .lastName(teacher1Ln)
                .build();

        teacher2 = Teacher.builder()
                .firstName(teacher2Fn)
                .lastName(teacher2Ln)
                .build();
    }

    @Test
    public void givenTeacherObject_whenSave_thenReturnSavedEmployee(){

        // when - action or the behaviour that we are going test
        Teacher savedTeacher = teacherRepository.save(teacher1);

        // then - verify the output
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher.getId()).isGreaterThan(0);
        assertThat(savedTeacher.getFirstName()).isEqualTo(teacher1Fn);
        assertThat(savedTeacher.getLastName()).isEqualTo(teacher1Ln);
    }

    @Test
    public void givenTeachersObject_whenSave_thenReturnSavedEmployee(){

        // when - action or the behaviour that we are going test
        Teacher savedTeacher = teacherRepository.save(teacher1);
        Teacher savedTeacher2 = teacherRepository.save(teacher2);

        // then - verify the output
        assertThat(savedTeacher).isNotNull();
        assertThat(savedTeacher2).isNotNull();
        List<Teacher> teachersList = teacherRepository.findAll();
        assertThat(teachersList).isNotNull();
        assertThat(teachersList.size()).isEqualTo(2);
    }
}
