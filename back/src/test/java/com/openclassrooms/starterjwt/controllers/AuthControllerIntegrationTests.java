package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// 4 Tests

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:sql/reset-database.sql")
public class AuthControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SessionMapper sessionMapper;

    String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ5b2dhQHN0dWRpby5jb20iLCJpYXQiOjE3MTMwMjU1MTQsImV4cCI6MTcxMzExMTkxNH0.PYv_gYN2oLCCTZNRGaUcsDU3wUl-h14lVab_QxuViTIq1gIBw_P_TPrNNgqqcbqiaCvoudbxRig72hNsTKtb1Q";

    // Login

    @Test
    @DisplayName("When a user log in with valid credentials, the API should return a 200 success response with a jwt")
    void user_LoginWithValidCredentials_200() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("test!1234");
        String LoginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);
        // Act & Assert
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(LoginRequestJson))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When a user tries to log in with invalid credentials, the API should return a 401 Unauthorized response")
    void user_LoginWithInvalidCredentials_401() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("yoga@studio.com");
        loginRequest.setPassword("invalidCredentials");
        String LoginRequestJson = new ObjectMapper().writeValueAsString(loginRequest);
        // Act & Assert
        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(LoginRequestJson))
                .andExpect(status().isUnauthorized());
    }

    // Register

    @Test
    @DisplayName("When a user tries to register with valid infos, the API should return a 200 success response with a jwt")
    void user_RegisteringWithValidInfos_200() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newEmail@studio.com");
        signupRequest.setPassword("randomValidPassword");
        signupRequest.setFirstName("User");
        signupRequest.setLastName("User");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        // Act & Assert
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(signupRequestJson))
                .andExpect(jsonPath("$.message").value("User registered successfully!"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("When a user tries to register with an email already used, the API should return a 400 Bad Request response")
    void user_RegisteringWithAnAlreadyUsedEmail_400() throws Exception {
        // Arrange
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("yoga@studio.com");
        signupRequest.setPassword("randomValidPassword");
        signupRequest.setFirstName("User");
        signupRequest.setLastName("User");
        String signupRequestJson = new ObjectMapper().writeValueAsString(signupRequest);
        // Act & Assert
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(signupRequestJson))
                .andExpect(status().isBadRequest());
    }
}