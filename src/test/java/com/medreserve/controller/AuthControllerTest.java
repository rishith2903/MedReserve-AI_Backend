package com.medreserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medreserve.dto.JwtResponse;
import com.medreserve.dto.LoginRequest;
import com.medreserve.dto.SignupRequest;
import com.medreserve.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("Authentication Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest validLoginRequest;
    private SignupRequest validSignupRequest;
    private JwtResponse mockJwtResponse;

    @BeforeEach
    void setUp() {
        // Setup valid login request
        validLoginRequest = new LoginRequest();
        validLoginRequest.setEmail("test@example.com");
        validLoginRequest.setPassword("Password123!");

        // Setup valid signup request
        validSignupRequest = new SignupRequest();
        validSignupRequest.setEmail("newuser@example.com");
        validSignupRequest.setPassword("Password123!");
        validSignupRequest.setFirstName("Test");
        validSignupRequest.setLastName("User");
        validSignupRequest.setPhoneNumber("+1234567890");
        validSignupRequest.setDateOfBirth("1990-01-01");
        validSignupRequest.setGender("MALE");

        // Setup mock JWT response
        mockJwtResponse = new JwtResponse();
        mockJwtResponse.setAccessToken("mock-jwt-token");
        mockJwtResponse.setRefreshToken("mock-refresh-token");
        mockJwtResponse.setTokenType("Bearer");
        mockJwtResponse.setExpiresIn(3600L);
    }

    @Test
    @DisplayName("Should successfully login with valid credentials")
    void testSuccessfulLogin() throws Exception {
        // Given
        when(authService.authenticateUser(any(LoginRequest.class)))
                .thenReturn(mockJwtResponse);

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").value("mock-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("Should return 400 for invalid login credentials")
    void testInvalidLogin() throws Exception {
        // Given
        LoginRequest invalidRequest = new LoginRequest();
        invalidRequest.setEmail("invalid@example.com");
        invalidRequest.setPassword("wrongpassword");

        when(authService.authenticateUser(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        // When & Then
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should successfully register new user")
    void testSuccessfulSignup() throws Exception {
        // Given
        when(authService.registerUser(any(SignupRequest.class)))
                .thenReturn("User registered successfully!");

        // When & Then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSignupRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully!"));
    }

    @Test
    @DisplayName("Should return 400 for duplicate email registration")
    void testDuplicateEmailSignup() throws Exception {
        // Given
        when(authService.registerUser(any(SignupRequest.class)))
                .thenThrow(new RuntimeException("Email is already in use!"));

        // When & Then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validSignupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for invalid email format")
    void testInvalidEmailSignup() throws Exception {
        // Given
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setPassword("Password123!");
        invalidRequest.setFirstName("Test");
        invalidRequest.setLastName("User");

        // When & Then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 for missing required fields")
    void testMissingFieldsSignup() throws Exception {
        // Given
        SignupRequest incompleteRequest = new SignupRequest();
        incompleteRequest.setEmail("test@example.com");
        // Missing password, firstName, lastName

        // When & Then
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should handle CORS preflight requests")
    void testCorsPreflightRequest() throws Exception {
        mockMvc.perform(post("/auth/login")
                .header("Origin", "https://med-reserve-ai.vercel.app")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type,Authorization"))
                .andExpect(status().isOk());
    }
}
