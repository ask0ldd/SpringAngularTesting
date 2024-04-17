package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 12 Integration Tests

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/reset-database.sql")
public class TeacherControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    // teachers with id 1 & 2 are inserted by default when the db is initialized
    private final Teacher teacher3 = Teacher.builder()
            .id(3L)
            .firstName("teacher3Fn")
            .lastName("teacher3Ln")
            .build();

    // -------
    // FindById
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a teacher, the API should return said teacher")
    void admin_FindById_200() throws Exception {
        // Arrange
        teacherRepository.save(teacher3);
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "3")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value(teacher3.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(teacher3.getLastName()));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a teacher, the API should return said teacher")
    void user_FindById_200() throws Exception {
        // Arrange
        teacherRepository.save(teacher3);
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "3")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.firstName").value(teacher3.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(teacher3.getLastName()));
        // .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("When a non authenticated user searches for a teacher, the API should return a 401 Unauthorized response")
    void notloggedUser_FindById_401() throws Exception {
        // Arrange
        teacherRepository.save(teacher3);
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "3")).andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a non existent teacher, the API should return a 404 Not Found response")
    void admin_FindById_404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "3")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a non existent teacher, the API should return a 404 Not Found response")
    void user_FindById_404() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "3")).andExpect(status().isNotFound());;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for a teacher while passing a malformed id, the API should return a 400 Bad Request response")
    void adminFindById_InvalidId_400() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "aaaaaa")).andExpect(status().isBadRequest());;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for a teacher while passing a malformed id, the API should return a 400 Bad Request response")
    void userFindById_InvalidId_400() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher/{teacherId}", "aaaaaa")).andExpect(status().isBadRequest());;
    }

    // -------
    // FindAll
    // -------

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When an admin searches for all teachers in DB, the API should return an array of teachers")
    void admin_FindAll_200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Margot"))
                .andExpect(jsonPath("$[0].lastName").value("DELAHAYE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Helene"))
                .andExpect(jsonPath("$[1].lastName").value("THIERCELIN"));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for all teachers in DB, the API should return an array of teachers")
    void user_FindAll_200() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Margot"))
                .andExpect(jsonPath("$[0].lastName").value("DELAHAYE"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Helene"))
                .andExpect(jsonPath("$[1].lastName").value("THIERCELIN"));
    }

    @Test
    @DisplayName("When a non authenticateduser searches for all teachers in DB, the API should return a 401 Unauthorized response")
    void notLoggedUser_FindAll_401() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/teacher")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("When a admin searches for all teachers but none are in DB, the API should return an empty")
    void adminFindAllWithNoTeacherInDB_EmptyArray() throws Exception {
        // Arrange
        teacherRepository.deleteAll();
        // Act & Assert
        mockMvc.perform(get("/api/teacher")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("When a user searches for all teachers but none are in DB, the API should return an empty")
    void userFindAllWithNoTeacherInDB_EmptyArray() throws Exception {
        // Arrange
        teacherRepository.deleteAll();
        // Act & Assert
        mockMvc.perform(get("/api/teacher")).andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}
