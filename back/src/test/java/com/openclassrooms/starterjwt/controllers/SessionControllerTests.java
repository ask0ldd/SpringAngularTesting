package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

// 17 Tests

@ExtendWith(MockitoExtension.class)
public class SessionControllerTests {
    @InjectMocks
    SessionController sessionController;
    @Mock
    SessionMapper sessionMapper;
    @Mock
    SessionService sessionService;

    private final LocalDateTime localDateTime = LocalDateTime.of(2023, 5, 15, 10, 30, 0);
    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build().setCreatedAt(localDateTime).setUpdatedAt(localDateTime);
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build().setCreatedAt(localDateTime).setUpdatedAt(localDateTime);
    private final SessionDto session1Dto = new SessionDto();
    private final SessionDto session2Dto = new SessionDto();

    public SessionControllerTests(){
        session1Dto.setId(session1.getId());
        session1Dto.setDate(session1.getDate());
        session1Dto.setTeacher_id(1L);
        session1Dto.setName(session1.getName());
        session1Dto.setDescription(session1.getDescription());
        session1Dto.setUsers(new ArrayList<>((Arrays.asList(1L, 2L))));
        session1Dto.setCreatedAt(session1.getCreatedAt());
        session1Dto.setUpdatedAt(session1.getUpdatedAt());
    }

    // -------
    // FindById
    // -------

    @Test
    @DisplayName("when service.findById returns a Session, ctrlr.findById should return a 200 Success response with the session")
    void findById_ValidSession_200(){
        // Arrange
        when(sessionService.getById(anyLong())).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(session1Dto);
        // Act
        ResponseEntity<?> response = sessionController.findById(session1.getId().toString());
        // Assert
        verify(sessionService, times(1)).getById(session1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(session1Dto);
    }

    @Test
    @DisplayName("when service.findById hasn't been able to retrieve the target Session, ctrlr.findById should return a 404 Not Found response")
    void findById_SessionNotFound_404(){
        when(sessionService.getById(anyLong())).thenReturn(null);
        ResponseEntity<?> response = sessionController.findById(session1.getId().toString());
        verify(sessionService, times(1)).getById(session1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("when the sessionId passed to the controller is malformed, ctrlr.findById should return a 400 Bad Request response")
    void findById_InvalidId_400(){
        String invalidSessionId = "aaa";
        ResponseEntity<?> response = sessionController.findById(invalidSessionId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).getById(anyLong());
    }

    // -------
    // FindAll
    // -------

    @Test
    @DisplayName("when service.findAll is returning an array of 2 Sessions, ctrlr.findAll should return a 200 Success response with two Sessions")
    void testFindAll_ExistingSessions_200(){
        List<Session> sessionsList = Arrays.asList(session1, session2);
        List<SessionDto> sessionsDtoList = Arrays.asList(session1Dto, session2Dto);
        when(sessionService.findAll()).thenReturn(sessionsList);
        when(sessionMapper.toDto(anyList())).thenReturn(sessionsDtoList);
        ResponseEntity<?> response = sessionController.findAll();
        verify(sessionService, times(1)).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionsDtoList);
    }

    @Test
    @DisplayName("when service.findAll is returning an empty array, ctrlr.findAll should return a 200 Success response with an empty array")
    void testFindAll_NoSessions_200(){
        List<Session> sessionsEmptyList = Collections.emptyList();
        List<SessionDto> sessionsDtoEmptyList = Collections.emptyList();
        when(sessionService.findAll()).thenReturn(sessionsEmptyList);
        when(sessionMapper.toDto(anyList())).thenReturn(sessionsDtoEmptyList);
        ResponseEntity<?> response = sessionController.findAll();
        verify(sessionService, times(1)).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sessionsDtoEmptyList);
    }

    // -------
    // Post
    // -------

    @Test
    @DisplayName("when service.create is returning a session, ctrlr.Create should return a 200 Success response with the session")
    void testPostSession_200(){
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session1);
        when(sessionService.create(any(Session.class))).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(session1Dto);
        ResponseEntity<?> response = sessionController.create(session1Dto);
        verify(sessionService, times(1)).create(any(Session.class));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(session1Dto);
    }

    // -------
    // Update
    // -------

    @Test
    @DisplayName("when service.update returns a session, ctrlr.Update should return a 200 Success response with the session")
    void testUpdateSession_200(){
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session1);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session1);
        when(sessionMapper.toDto(any(Session.class))).thenReturn(session1Dto);
        ResponseEntity<?> response = sessionController.update(session1.getId().toString(), session1Dto);
        verify(sessionService, times(1)).update(anyLong(), any(Session.class));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(session1Dto);
    }

    @Test
    @DisplayName("when the sessionId passed to the ctrlr.Update is malformed, it should return a 400 Bad Request response")
    void testUpdateSession_invalidSessionId_400(){
        String invalidSessionId = "aaa";
        ResponseEntity<?> response = sessionController.update(invalidSessionId, session1Dto);
        // assertThrows(NumberFormatException.class, () -> sessionController.update(invalidSessionId, session1Dto));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).update(any(), any(Session.class));
    }

    // -------
    // Delete (!!!! NB : delete controller method is named save instead of delete !!!!)
    // -------

    @Test
    @DisplayName("when session.delete returns no value, ctrlr.Delete should return a 200 Success response") // !!!! error
    void testDeleteSession_200(){
        // Arrange
        doNothing().when(sessionService).delete(anyLong());
        when(sessionService.getById(anyLong())).thenReturn(session1);
        // Act
        // error in original code : controller method called save instead of delete
        ResponseEntity<?> response = sessionController.save(session1.getId().toString());
        // Assert
        verify(sessionService, times(1)).getById(session1.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("when a non existent sessionId is passed as a parameter to ctrlr.Delete, it should return a 404 Not Found response") // !!!! error
    void testDeleteSession_nonExistentSession_404() {
        // Arrange
        String nonExistentSessionId = "123";
        when(sessionService.getById(anyLong())).thenReturn(null);
        // Act
        ResponseEntity<?> response = sessionController.save(nonExistentSessionId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("when a malformed sessionId is passed as a parameter to ctrlr.Delete, it should return a 400 Bad Request response") // !!!! error
    void testDeleteSession_InvalidSessionId_400() {
        // Arrange
        String invalidSessionId = "invalidSessionId";
        // Act
        ResponseEntity<?> response = sessionController.save(invalidSessionId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).delete(anyLong());
    }

    // -------
    // Participate
    // -------

    @Test
    @DisplayName("when service.participate returns no value, ctrl.participate should return a 200 Success response")
    void testPostParticipate_200(){
        // Arrange
        doNothing().when(sessionService).participate(anyLong(), anyLong());
        // Act
        ResponseEntity<?> response = sessionController.participate(session1.getId().toString(), user1.getId().toString());
        // Assert
        verify(sessionService, times(1)).participate(anyLong(), anyLong());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("when an invalid sessionId is passed to ctrl.participate, it should return a 400 Bad Request response")
    void testPostParticipate_InvalidSessionId_400(){
        // Arrange
        String invalidSessionId = "invalidSessionId";
        // Act
        ResponseEntity<?> response = sessionController.participate(invalidSessionId, user1.getId().toString());
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    @DisplayName("when an invalid userId is passed to ctrl.participate, it should return a 400 Bad Request response")
    void testPostParticipate_whenInvalidUserId(){
        // Arrange
        String invalidUserId = "invalidUserId";
        // Act
        ResponseEntity<?> response = sessionController.participate(session1.getId().toString(), invalidUserId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    // -------
    // Unsub
    // -------

    @Test
    @DisplayName("when service.noLongerParticipate returns no value, ctrl.participate should return a 200 Success response")
    void testDeleteParticipate_200(){
        // Assert
        doNothing().when(sessionService).noLongerParticipate(anyLong(), anyLong());
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(session1.getId().toString(), user1.getId().toString());
        // Assert
        verify(sessionService, times(1)).noLongerParticipate(anyLong(), anyLong());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("when an invalid sessionId is passed to ctrl.noLongerParticipate, it should return a 400 Bad Request response")
    void testDeleteParticipate_whenInvalidSessionId(){
        // Arrange
        String invalidSessionId = "invalidSessionId";
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(invalidSessionId, user1.getId().toString());
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }

    @Test
    @DisplayName("when an invalid userId is passed to ctrl.noLongerParticipate, it should return a 400 Bad Request response")
    void testDeleteParticipate_whenInvalidUserId(){
        // Arrange
        String invalidUserId = "invalidUserId";
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(session1.getId().toString(), invalidUserId);
        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }

    /*
    @Test // !!!! to fix cause numberformatexception shouldn't be thrown by the service
    @DisplayName("")
    void testNoLongerParticipate_Exception() {
        // Arrange
        String id = "invalidSessionId";
        String userId = "invalidUserId";
        // use doThrow instead of thenThrow when the method doesn't return a value
        doThrow(new NumberFormatException("Test Exception")).when(sessionService).noLongerParticipate(any(), any());
        // Act
        ResponseEntity<?> response = sessionController.noLongerParticipate(id, userId);
        // Assert
        assertThrows(NumberFormatException.class, () -> {
            sessionService.noLongerParticipate(any(), any());
        });
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
    */

}
