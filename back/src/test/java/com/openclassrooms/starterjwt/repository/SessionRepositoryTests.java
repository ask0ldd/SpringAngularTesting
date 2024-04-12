package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class SessionRepositoryTests {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    private Session session1;
    private Session session2;

    private Teacher teacher1;
    private Teacher teacher2;

    String teacher1Fn = "John";
    String teacher1Ln = "Doe";
    String teacher2Fn = "Jane";
    String teacher2Ln = "Doe";

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

        session1 = Session.builder().name("session_name1").description("session_description1").date(new Date()).teacher(teacher1)
                .build();

        session2 = Session.builder().name("session_name2").description("session_description2").date(new Date()).teacher(teacher2)
                .build();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
    }

    @Test
    public void givenSessionObject_whenSave_thenReturnSavedSession(){

        // when - action or the behaviour that we are going test
        Session savedSession = sessionRepository.save(session1);

        // then - verify the output
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getName()).isEqualTo("session_name1");
        assertThat(savedSession.getDescription()).isEqualTo("session_description1");
        assertThat(savedSession.getTeacher()).isEqualTo(teacher1);
    }

    @Test
    public void givenSessionsObject_whenSave_thenReturnSavedSession(){

        // when - action or the behaviour that we are going test
        Session savedSession1 = sessionRepository.save(session1);
        Session savedSession2 = sessionRepository.save(session2);

        // then - verify the output
        assertThat(savedSession1).isNotNull();
        assertThat(savedSession2).isNotNull();
        List<Session> sessionsList = sessionRepository.findAll();
        assertThat(sessionsList).isNotNull();
        assertThat(sessionsList.size()).isEqualTo(2);
    }
}
