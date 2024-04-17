package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 24 Tests

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/reset-database.sql")
public class SessionControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;
    @SpyBean
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SessionMapper sessionMapper;

    private final User user1 = User.builder().id(1L).admin(false).email("user1@ced.com").firstName("user1Fn").lastName("user1Ln").password("aeazezeaeazeae").build();
    private final User user2 = User.builder().id(2L).admin(false).email("user2@ced.com").firstName("user2Fn").lastName("user2Ln").password("aeazezeaeazeae").build();
    private final User user3 = User.builder().id(3L).admin(false).email("user3@ced.com").firstName("user3Fn").lastName("user3Ln").password("aeazezeaeazeae").build();
    private final Teacher teacher1 = Teacher.builder().id(1L).firstName("teacher1Fn").lastName("teacher1Ln").build();
    private final Session session1 = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session1Update = Session.builder().id(1L).name("session1UpdateName").description("session1UpdateDescription").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session2 = Session.builder().id(2L).name("session2Name").description("session2Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session session3 = Session.builder().id(3L).name("session3Name").description("session3Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Arrays.asList(user1, user2)))).build();
    private final Session sessionWithNoParticipant = Session.builder().id(1L).name("session1Name").description("session1Description").date(new Date()).teacher(teacher1).users(new ArrayList<>((Collections.emptyList()))).build();

    @BeforeEach()
    void initDb(){
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        sessionRepository.save(session1);
        sessionRepository.save(session2);
        Mockito.reset(sessionRepository);
    }

    // -------
    // FindById
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a session, the API should return said Session")
    void admin_FindById() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(session1.getName()))
                .andExpect(jsonPath("$.description").value(session1.getDescription()));
        verify(sessionRepository, times(1)).findById(anyLong());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When an user searches for a session, the API should return said Session")
    void user_FindById() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "1")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value(session1.getName()))
                .andExpect(jsonPath("$.description").value(session1.getDescription()));
        verify(sessionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("When a non authenticated user searches for a session, the API should return a 401 Not Found response")
    void nonLoggedUser_FindById() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "1")).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        verify(sessionRepository, never()).findById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a session that does not exist, the API should return a 404 Not Found response")
    void admin_FindById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "3")).andExpect(status().isNotFound());
        verify(sessionRepository, times(1)).findById(anyLong());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a session that does not exist, the API should return a 404 Not Found response")
    void user_FindById_NotFound() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "3")).andExpect(status().isNotFound());
        verify(sessionRepository, times(1)).findById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a session sending a malformed id, the API should return a 400 Not Found response")
    void admin_FindById_InvalidId_BadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "aaaaaa")).andExpect(status().isBadRequest());
        verify(sessionRepository, never()).findById(anyLong());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a session sending a malformed id, the API should return a 400 Not Found response")
    void user_FindById_InvalidId_BadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session/{sessionId}", "aaaaaa")).andExpect(status().isBadRequest());
        verify(sessionRepository, never()).findById(anyLong());
    }

    // -------
    // FindAll
    // -------s

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user asks for all sessions, the API should return an Array of Sessions")
    void user_FindAll() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(session1.getName()))
                .andExpect(jsonPath("$[0].description").value(session1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value(session2.getName()))
                .andExpect(jsonPath("$[1].description").value(session2.getDescription()));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("When a non authenticated user asks for all sessions, the API should return a 401 Unauthorized response")
    void nonLoggedUser_FindAll_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/session")).andExpect(status().isUnauthorized());
        verify(sessionRepository, never()).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin asks for all sessions and no sessions exists in DB, the API should return an empty Array")
    void admin_FindAllWithNoTeacherInDB_EmptyArray() throws Exception {
        // Arrange
        sessionRepository.deleteAll();
        // Act & Assert
        mockMvc.perform(get("/api/session")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user asks for all sessions and no sessions exists in DB, the API should return an empty Array")
    void user_FindAllWithNoTeacherInDB_EmptyArray() throws Exception {
        // Arrange
        sessionRepository.deleteAll();
        // Act & Assert
        mockMvc.perform(get("/api/session")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
        verify(sessionRepository, times(1)).findAll();
    }

    // -------
    // DeleteById
    // -------s

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to delete a session, the API should return a 200 Success response")
    void user_DeleteById() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/{session}", "2")).andExpect(status().isOk());
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin tries to delete a session, the API should return a 200 Success response")
    void admin_DeleteById_200() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/{sessionId}", "2")).andExpect(status().isOk());
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("When a non authenticated user tries to delete a session, the API should return a 401 Unauthorized response")
    void nonLoggedUser_DeleteById_401() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/{sessionId}", "2")).andExpect(status().isUnauthorized());
        verify(sessionRepository, never()).deleteById(anyLong());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin tries to delete a non existent session, the API should return a 404 Not Found response")
    void admin_DeleteById_404() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/{sessionId}", "4")).andExpect(status().isNotFound());
        verify(sessionRepository, never()).deleteById(anyLong());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to delete a non existent session, the API should return a 404 Not Found response")
    void user_DeleteById_404() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/session/{sessionId}", "4")).andExpect(status().isNotFound());
        verify(sessionRepository, never()).deleteById(anyLong());
    }

    // -------
    // Participate
    // -------

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user subs to a session, the API should return a 200 Success response")
    void user_SubsToSession_200() throws Exception {
        sessionRepository.save(sessionWithNoParticipant);
        Mockito.reset(sessionRepository);
        String userId = "1";
        String sessionId = "1";
        // Act & Assert
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId)).andExpect(status().isOk());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to sub to a non existent session, the API should return a 404 Not Found response")
    void user_SubsToANonExistentSession_404() throws Exception {
        // Arrange
        sessionRepository.save(sessionWithNoParticipant);
        Mockito.reset(sessionRepository);
        String userId = "1";
        String sessionId = "5";
        // Act & Assert
        mockMvc.perform(post("/api/session/{id}/participate/{userId}", sessionId, userId)).andExpect(status().isNotFound());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // -------
    // Unsub
    // -------

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user unsubs from a session, the API should return a 200 Success response")
    void user_UnsubsFromSession_200() throws Exception {
        // Arrange
        String userId = "1";
        String sessionId = "1";
        // Act & Assert
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)).andExpect(status().isOk());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to unsub from a session he is not participating to, the API should return a 400 Bad Request response")
    void user_UnsubsFromSessionHeIsNotParticipatingTo_400() throws Exception {
        // Arrange
        sessionRepository.save(sessionWithNoParticipant);
        Mockito.reset(sessionRepository);
        String userId = "1";
        String sessionId = "1";
        // Act & Assert
        mockMvc.perform(delete("/api/session/{id}/participate/{userId}", sessionId, userId)).andExpect(status().isBadRequest());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // -------
    // Post Session
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin create a session, the API should return a 200 Success response")
    void admin_PostSession_200() throws Exception {
        // Arrange
        String session3Json = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session3));
        // Act & Assert
        mockMvc.perform(post("/api/session/").contentType(MediaType.APPLICATION_JSON)
                .content(session3Json)).andExpect(status().isOk());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("When a non authenticated user tries to create a session, the API should return a 401 Unauthorized response")
    void nonLoggedUser_PostSession_401() throws Exception {
        // Arrange
        String session3Json = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session3));
        // Act & Assert
        mockMvc.perform(post("/api/session/").contentType(MediaType.APPLICATION_JSON)
                .content(session3Json)).andExpect(status().isUnauthorized());
        verify(sessionRepository, never()).save(any(Session.class));
    }

    // -------
    // Update Session
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin tries to update a session, the API should return a 200 Success response")
    void admin_UpdateSession_200() throws Exception {
        // Arrange
        String session1UpdateJson = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session1Update));
        // Act & Assert
        mockMvc.perform(put("/api/session/{sessionId}", session1Update.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                .content(session1UpdateJson)).andExpect(status().isOk());
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    @DisplayName("When a non authenticated user tries to update a session, the API should return a 401 Unauthorized response")
    void nonLoggedUser_UpdateSession_401() throws Exception {
        // Arrange
        String session1UpdateJson = new ObjectMapper().writeValueAsString(sessionMapper.toDto(session1Update));
        // Act & Assert
        mockMvc.perform(put("/api/session/{sessionId}", session1Update.getId().toString()).contentType(MediaType.APPLICATION_JSON)
                .content(session1UpdateJson)).andExpect(status().isUnauthorized());
        verify(sessionRepository, never()).save(any(Session.class));
    }

}