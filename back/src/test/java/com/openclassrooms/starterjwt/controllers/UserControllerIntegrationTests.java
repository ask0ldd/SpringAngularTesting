package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/reset-database.sql")
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // admin user with id 1 is inserted by default when the db is initialized
    private final User user2 = User.builder()
            .id(2L).admin(true)
            .email("ced@ced.com")
            .firstName("john")
            .lastName("doe")
            .password(passwordEncoder.encode("aeazezeaeazeae"))
            .build();

    // -------
    // FindById
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a user, the API should return said user")
    void admin_FindById_200() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(get("/api/user/{userId}", "2")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value(user2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user2.getLastName()))
                .andExpect(jsonPath("$.email").value(user2.getEmail()))
                .andExpect(jsonPath("$.admin").value(user2.isAdmin()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a target user, the API should return said user")
    void user_FindById_200() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(get("/api/user/{userId}", "2")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.firstName").value(user2.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user2.getLastName()))
                .andExpect(jsonPath("$.email").value(user2.getEmail()))
                .andExpect(jsonPath("$.admin").value(user2.isAdmin()));
                // .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When a non authenticated user searches for a user, the API should return a 401 Unauthorized response")
    void nonloggedUser_FindById_401() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(get("/api/user/{userId}", "2")).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a non existent user, the API should return a 404 Not Found response")
    void admin_FindById_404() throws Exception {
        mockMvc.perform(get("/api/user/{userId}", "2")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a non existent user, the API should return a 404 Not Found response")
    void user_FindById_404() throws Exception {
        mockMvc.perform(get("/api/user/{userId}", "2")).andExpect(status().isNotFound());;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a user passing a malformed id, the API should return a 400 Bad Request response")
    void admin_FindById_InvalidId_400() throws Exception {
        mockMvc.perform(get("/api/user/{teacherId}", "aaaaaa")).andExpect(status().isBadRequest());;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a user passing a malformed id, the API should return a 400 Bad Request response")
    void user_FindById_InvalidId_400() throws Exception {
        mockMvc.perform(get("/api/user/{teacherId}", "aaaaaa")).andExpect(status().isBadRequest());;
    }

    // -------
    // DeleteById
    // -------

    @Test
    @WithMockUser(username = "ced@ced.com", password = "aeazezeaeazeae", roles = "USER")
    @DisplayName("When a user tries to delete its own account, the API should return a 200 Success response")
    void user_DeleteByItselfById_200() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin tries to delete someone else account, the API should return a 401 Unauthorized response")
    void admin_DeleteAnotherUserById_401() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to delete someone else account, the API should return a 401 Unauthorized response")
    void user_DeleteAnotherUserById_401() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("When a non authenticated user tries to delete someone else account, the API should return a 401 Unauthorized response")
    void nonloggedUser_DeleteById_401() throws Exception {
        userRepository.save(user2);
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin tries to delete a non existent user, the API should return a 404 Not Found response")
    void AdminDeleteById_NotFound() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user tries to delete a non existent user, the API should return a 404 Not Found response")
    void user_DeleteById_404() throws Exception {
        mockMvc.perform(delete("/api/user/{userId}", "2")).andExpect(status().isNotFound());
    }
}