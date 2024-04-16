package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Sql(scripts = "classpath:sql/reset-database.sql")
public class SessionServiceIntegrationTests {
    @Autowired
    private SessionService sessionService;
    @SpyBean
    private SessionRepository sessionRepository;
    @SpyBean
    private UserRepository userRepository;

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final User user3 = User.builder().id(3L).admin(false).email("user3@ced.com").firstName("user3Fn").lastName("user3Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session1Update = Session.builder().id(1L).name("session1UpdateName").description("session1UpdateDescription").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session sessionWithNoParticipant = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Collections.emptyList()))).build();

    @BeforeEach
    void setup(){
        userRepository.save(user1);
        userRepository.save(user2);
        sessionRepository.save(session1);
        Mockito.reset(userRepository);
        Mockito.reset(sessionRepository);
    }

    // -------
    // GetById
    // -------

    @Test
    @DisplayName("When the session targeted by .getById() exists, said session should be returned")
    void testGetById_SessionExists_ReturnASession() {
        // Act
        Session session = sessionService.getById(1L);
        // Assert : A session is expected
        assertThat(session).isNotNull();
        verify(sessionRepository, times(1)).findById(1L);
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
    }

    @Test
    @DisplayName("When the session targeted by .getById() doesn't exist, null should be returned")
    void testGetById_SessionDoesntExist_ReturnNull() {
        // Act
        Session session = sessionService.getById(2L);
        // Assert : Null is expected
        assertThat(session).isNull();
        verify(sessionRepository, times(1)).findById(2L);
    }

    // -------
    // Create
    // -------

    @Test
    @DisplayName("When a session is successfully created, said session should be returned")
    void testCreate_SessionCanBeCreated_ReturnASession() {
        // Act
        Session session = sessionService.create(session2);
        // Assert : The created session is expected
        assertThat(session).isNotNull();
        verify(sessionRepository, times(1)).save(session2);
        assertThat(session.getName()).isEqualTo(session2.getName());
        assertThat(session.getDescription()).isEqualTo(session2.getDescription());
    }

    // -------
    // FindAll
    // -------

    @Test
    @DisplayName("When multiple sessions exist in DB, an array of sessions should be returned")
    void testFindAll_MultipleSessionsExist_ReturnAListOfSessions() {
        sessionRepository.save(session2);
        // Act
        List<Session> sessions = sessionService.findAll();
        // Assert : Two sessions are expected
        assertThat(sessions.size()).isEqualTo(2);
        verify(sessionRepository, times(1)).findAll();
        assertThat(sessions.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessions.get(0).getDescription()).isEqualTo(session1.getDescription());
        assertThat(sessions.get(1).getName()).isEqualTo(session2.getName());
        assertThat(sessions.get(1).getDescription()).isEqualTo(session2.getDescription());
    }

    // -------
    // Update
    // -------

    @Test
    @DisplayName("When a session is successfully updated, said session should be returned")
    void testUpdate_Success_ReturnUpdatedSession() {
        // Act
        Session session = sessionService.update(1L, session1Update);
        // Assert : The updated session should be returned
        assertThat(session).isNotNull();
        verify(sessionRepository, times(1)).save(any(Session.class));
        assertThat(session.getName()).isEqualTo(session1Update.getName());
        assertThat(session.getDescription()).isEqualTo(session1Update.getDescription());
    }

    // -------
    // Delete
    // -------

    @Test
    @DisplayName("When deleting a session, the user shouldn't be in DB anymore")
    void testDelete_Success_RepoDeleteCalled(){
        // Arrange : Check pre-deletion state
        Session session = sessionRepository.findById(1L).orElse(null);
        assertThat(session).isNotNull();
        // Act
        sessionService.delete(1L);
        // Assert : Check post-deletion state
        Session updatedSession = sessionRepository.findById(1L).orElse(null);
        assertThat(updatedSession).isNull();
    }

    // -------
    // Participate
    // -------

    @Test
    void testParticipate_Success_() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        // replace session 1L with a session with no participants
        sessionRepository.save(sessionWithNoParticipant);
        Mockito.reset(sessionRepository);
        // constructing what the updatedSession should look like for comparison purposes
        Session expectedUpdatedSession = Session.builder()
                .id(1L)
                .name("session1Name")
                .description("session1Description")
                .date(new Date())
                .teacher(teacher1)
                .users(new ArrayList<>((Collections.singletonList(user1))))
                .build();
        // Act
        sessionService.participate(sessionId, userId);
        // Assert : The service should build the expected update session & pass it to repo.save
        verify(sessionRepository, times(1)).save(expectedUpdatedSession);
    }

    // -------
    // Unsub
    // -------



        // TODO
/*
    // unparticipate
    @Test
    void unsubscribingFromASession_Success() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        Session expectedUpdatedSession = Session.builder()
                .id(1L)
                .name("session1Name")
                .description("session1Description")
                .date(new Date())
                .teacher(teacher1)
                .users(new ArrayList<>((Collections.singletonList(user2))))
                .build();

        // Act
        sessionService.noLongerParticipate(sessionId, userId);
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, times(1)).save(expectedUpdatedSession);
    }
*/
}
