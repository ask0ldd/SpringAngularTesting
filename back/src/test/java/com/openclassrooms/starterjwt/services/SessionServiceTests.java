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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
// @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SessionServiceTests {
    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    // TODO : should bcrypt the password

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final User user3 = User.builder().id(3L).admin(false).email("user3@ced.com").firstName("user3Fn").lastName("user3Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session sessionWithNoParticipant = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Collections.emptyList()))).build();

    @Test
    @DisplayName("When the session targeted by .getById() exists, it should return the contained session")
    void whenTheSessionTargetedByGetByIdExists_ShouldReturnASession() {
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
    @DisplayName("When the session targeted by .getById() doesn't exist, it should return the contained session")
    void whenTheSessionTargetedByGetByIdDoesntExist_ShouldReturnNull() {
        // Arrange
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Act
        Session session = sessionService.getById(1L);
        // Assert
        assertNull(session);
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("When a session is successfully created, it should return the created session")
    void whenASessionCanBeCreated_ShouldReturnASession() {
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

    @Test
    void whenCallingFindAll_AndRecevingAListOfSessions_ShouldReturnAListOfSessions() {
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

    @Test
    void whenSuccessfulUpdate() {
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

    @Test
    void whenSuccessfulDelete() {
        // Arrange
        // needs to use this syntax cause when(userRepository.deleteById(anyLong())).doNothing(); generates an IDE alert
        // when trying to mock methods returning void
        doNothing().when(sessionRepository).deleteById(anyLong());
        // Act
        sessionService.delete(1L);
        // Assert
        verify(sessionRepository, times(1)).deleteById(1L);
    }

    // participate
    @Test
    void whenRegisteringASuccessfulParticipation() {
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
    void subscribingToAnUnknownTargetSession() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        // Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void subscribingANonExistentUser() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void subscribingToASession_alreadySubscribedUser() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session1));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user1));
        // Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.participate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).save(any(Session.class));
    }

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

    @Test
    void unsubscribingFromAnUnknownTargetSession() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        // Assert
        assertThrows(NotFoundException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).deleteById(anyLong());
    }

    @Test
    void unsubscribingFromASession_alreadyUnsubscribed() {
        // Arrange
        Long userId = 1L;
        Long sessionId = 1L;
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(sessionWithNoParticipant));
        // Assert
        assertThrows(BadRequestException.class, () -> {
            sessionService.noLongerParticipate(sessionId, userId);
        });
        verify(sessionRepository, times(1)).findById(anyLong());
        verify(sessionRepository, never()).deleteById(anyLong());
    }
}