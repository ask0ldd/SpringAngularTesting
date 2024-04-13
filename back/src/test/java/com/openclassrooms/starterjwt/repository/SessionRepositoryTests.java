package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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

        session1 = Session.builder().id(1L).name("session_name1").description("session_description1").date(new Date()).teacher(teacher1)
                .build();

        session2 = Session.builder().id(2L).name("session_name2").description("session_description2").date(new Date()).teacher(teacher2)
                .build();

        teacherRepository.save(teacher1);
        teacherRepository.save(teacher2);
        sessionRepository.save(session1);
    }

    @Test
    void findById_SessionExists_ReturnAUser(){
        // Act
        Session session = sessionRepository.findById(1L).orElse(null);
        // Assert
        assertThat(session).isNotNull();
        assertThat(session.getId()).isEqualTo(1L);
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
    }

    @Test
    void findById_SessionDoesntExist_ReturnAnEmptyOptional(){
        // Act
        Optional<Session> sessionOptional = sessionRepository.findById(3L);
        // Assert
        assertThat(sessionOptional.isEmpty()).isTrue();
    }

    @Test
    void findAll_MultipleSessionsExists_ReturnAnArrayOfSessions(){
        // Act
        sessionRepository.save(session2);
        List<Session> sessionList = sessionRepository.findAll();
        // Assert
        assertThat(sessionList.size()).isEqualTo(2);
        assertThat(sessionList.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessionList.get(1).getName()).isEqualTo(session2.getName());
        assertThat(sessionList.get(0).getDescription()).isEqualTo(session1.getDescription());
        assertThat(sessionList.get(1).getDescription()).isEqualTo(session2.getDescription());
    }

    @Test
    void findAll_NoExistingSessions_ReturnAnEmptyArray(){
        // Act
        sessionRepository.deleteAll();
        List<Session> sessionList = sessionRepository.findAll();
        // Assert
        assertThat(sessionList.size()).isEqualTo(0);
    }

    @Test
    void saveSession(){
        // Act
        Session savedSession = sessionRepository.save(session2);
        // Assert
        assertThat(savedSession).isNotNull();
        assertThat(savedSession.getId()).isEqualTo(2L);
        assertThat(savedSession.getName()).isEqualTo(session2.getName());
        assertThat(savedSession.getDescription()).isEqualTo(session2.getDescription());
    }

    @Test
    void deleteById(){
        Session retrievedSession = sessionRepository.findById(1L).orElse(null);
        assertThat(retrievedSession).isNotNull();
        sessionRepository.deleteById(1L);
        Optional<Session> sessionOptional = sessionRepository.findById(1L);
        assertThat(sessionOptional.isEmpty()).isTrue();
    }
}
