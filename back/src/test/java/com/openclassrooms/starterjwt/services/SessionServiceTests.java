package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

// 13 Tests

@ExtendWith(MockitoExtension.class)
public class SessionServiceTests {
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final User user3 = User.builder().id(3L).admin(false).email("user3@ced.com").firstName("user3Fn").lastName("user3Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session sessionWithNoParticipant = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Collections.emptyList()))).build();

    // GetById

    @Test
    @DisplayName("When the session targeted by .getById() exists, said session should be returned")
    void testGetbyId_TargetSessionExists_ShouldReturnASession() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        // Act
        Session session = sessionService.getById(1L);
        // Assert
        assertNotNull(session);
        verify(sessionRepository, times(1)).findById(1L);
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
        assertThat(session.getDate()).isEqualTo(session1.getDate());
    }

    @Test
    @DisplayName("When the session targeted by .getById() doesn't exist, null should be returned")
    void testGetbyId_TargetSessionDoesntExist_ShouldReturnASession() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        Session session = sessionService.getById(1L);
        // Assert
        assertNull(session);
        verify(sessionRepository, times(1)).findById(1L);
    }

    // Create

    @Test
    @DisplayName("When a session is successfully created, said session should be returned")
    void testCreate_SessionCanBeCreated_ShouldReturnASession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        // Act
        Session session = sessionService.create(session1);
        // Assert
        assertNotNull(session);
        verify(sessionRepository, times(1)).save(session1);
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
        assertThat(session.getDate()).isEqualTo(session1.getDate());
    }

    // FindAll

    @Test
    @DisplayName("When findAll() is called and multiple sessions are returned by the repository, an array of sessions should be returned")
    void testFindAllMultipleSessionsExist_ShouldReturnAListOfSessions() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2));
        // Act
        List<Session> sessions = sessionService.findAll();
        // Assert
        assertThat(sessions.size()).isEqualTo(2);
        verify(sessionRepository, times(1)).findAll();
        assertThat(sessions.get(0).getName()).isEqualTo(session1.getName());
        assertThat(sessions.get(0).getDescription()).isEqualTo(session1.getDescription());
        assertThat(sessions.get(1).getName()).isEqualTo(session2.getName());
        assertThat(sessions.get(1).getDescription()).isEqualTo(session2.getDescription());
    }

    // TODO : Empty array

    // Update

    @Test
    @DisplayName("When a session is successfully updated, said session should be returned")
    void testUpdate_SessionCanBeUpdated_ShouldReturnASession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });
        // Act
        Session session = sessionService.update(1L, session1);
        // Assert
        assertNotNull(session);
        verify(sessionRepository, times(1)).save(session1);
        assertThat(session.getName()).isEqualTo(session1.getName());
        assertThat(session.getDescription()).isEqualTo(session1.getDescription());
        assertThat(session.getDate()).isEqualTo(session1.getDate());
    }

    // Delete

    @Test
    @DisplayName("When a session is deleted, sessionRepository.deleteById should be called")
    void testDelete_respositoryDeleteByIdShouldBeCalled() {
        // Arrange
        // when(userRepository.deleteById(anyLong())).doNothing(); generates an IDE alert so inverted syntax mandatory
        // when trying to mock methods returning void
        doNothing().when(sessionRepository).deleteById(anyLong());
        // Act
        sessionService.delete(1L);
        // Assert
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    // Participate

    @Test
    @DisplayName("When a user sub to a yoga session, sessionRepository.save should be called")
    void testSubYogaSession_repositorySaveShouldBeCalled() {
        // Arrange
        Long userId = 3L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        // will try to retrieve user2 : sessionService.participate(1L, 2L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user3));
        // and add it to session1 : sessionService.participate(1L, 2L);
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            return invocation.getArgument(0);
        });

        // constructing what the updatedSession should look like
        Session expectedUpdatedSession = Session.builder()
                .id(1L)
                .name("session1Name")
                .description("session1Description")
                .date(new Date())
                .teacher(teacher1)
                .users(new ArrayList<>((Arrays.asList(user1, user2, user3))))
                .build();

        // Act
        sessionService.participate(sessionId, userId);
        // Assert
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(3L);
        verify(sessionRepository, times(1)).save(expectedUpdatedSession);
    }

    @Test
    @DisplayName("When a user sub to a non existent yoga session, sessionRepository.save shouldnt be called")
    void testSubYogaSession_NonExistentSession_repositorySaveShouldntBeCalled() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @DisplayName("When trying to sub a non existent user to a yoga session, sessionRepository.save shouldnt be called")
    void testSubYogaSession_NonExistentUser_repositorySaveShouldntBeCalled() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    @DisplayName("When trying to sub an already subbed user, sessionRepository.save shouldnt be called")
    void testSubYogaSession_AlreadySubUser_repositorySaveShouldntBeCalled() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // Unparticipate

    @Test
    @DisplayName("When a user unsub from a yoga session, sessionRepository.save should be called")
    void testUnsubYogaSession_repositorySaveShouldBeCalled() {
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
        // Assert
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, times(1)).save(expectedUpdatedSession);
    }

    @Test
    @DisplayName("When a user tries to unsub from a non existent yoga session, sessionRepository.save shouldnt be called")
    void testUnsubYogaSession_UnknownSession_RepositorySaveShouldntBeCalled() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("When a user tries to unsub from a session he is not subbed to, sessionRepository.save shouldnt be called")
    void testUnsubYogaSession_NotSubbedToTheSession_RepositorySaveShouldntBeCalled() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(sessionWithNoParticipant));
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).deleteById(anyLong());
    }
}