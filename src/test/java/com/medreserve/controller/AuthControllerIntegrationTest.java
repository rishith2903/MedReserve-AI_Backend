package com.medreserve.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medreserve.dto.LoginRequest;
import com.medreserve.dto.SignupRequest;
import com.medreserve.entity.Role;
import com.medreserve.entity.User;
import com.medreserve.repository.RoleRepository;
import com.medreserve.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Transactional
class AuthControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    private Role patientRole;
    
    @BeforeEach
    void setUp() {
        // Create patient role if it doesn't exist
        patientRole = roleRepository.findByName(Role.RoleName.PATIENT)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName(Role.RoleName.PATIENT);
                    role.setDescription("Patient role");
                    return roleRepository.save(role);
                });
    }
    
    @Test
    void testSignup() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("john.doe@example.com");
        signupRequest.setPassword("Password@123");
        signupRequest.setPhoneNumber("+1234567890");
        signupRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        signupRequest.setGender(User.Gender.MALE);
        signupRequest.setRole(Role.RoleName.PATIENT);
        
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"))
                .andExpect(jsonPath("$.success").value(true));
    }
    
    @Test
    void testSignupWithExistingEmail() throws Exception {
        // Create existing user
        User existingUser = new User();
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setEmail("existing@example.com");
        existingUser.setPassword(passwordEncoder.encode("password"));
        existingUser.setRole(patientRole);
        existingUser.setIsActive(true);
        existingUser.setEmailVerified(true);
        userRepository.save(existingUser);
        
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        signupRequest.setEmail("existing@example.com");
        signupRequest.setPassword("Password@123");
        signupRequest.setRole(Role.RoleName.PATIENT);
        
        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email is already in use!"))
                .andExpect(jsonPath("$.success").value(false));
    }
    
    @Test
    void testSignin() throws Exception {
        // Create test user
        User testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(patientRole);
        testUser.setIsActive(true);
        testUser.setEmailVerified(true);
        userRepository.save(testUser);
        
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("PATIENT"));
    }
    
    @Test
    void testSigninWithInvalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@example.com");
        loginRequest.setPassword("wrongpassword");
        
        mockMvc.perform(post("/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }
}
