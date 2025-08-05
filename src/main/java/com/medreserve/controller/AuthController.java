package com.medreserve.controller;

import com.medreserve.dto.JwtResponse;
import com.medreserve.dto.LoginRequest;
import com.medreserve.dto.MessageResponse;
import com.medreserve.dto.SignupRequest;
import com.medreserve.entity.User;
import com.medreserve.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signin")
    @Operation(summary = "User login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User login (alias)", description = "Authenticate user and return JWT tokens - alias for /signin")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User login (alias)", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticateUser(loginRequest);
    }
    
    @PostMapping("/signup")
    @Operation(summary = "User registration", description = "Register a new user")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        try {
            MessageResponse response = authService.registerUser(signUpRequest);
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(MessageResponse.error("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Get new access token using refresh token")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam String refreshToken) {
        try {
            JwtResponse jwtResponse = authService.refreshToken(refreshToken);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current authenticated user details")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping("/signout")
    @Operation(summary = "User logout", description = "Logout user (client should remove tokens)")
    public ResponseEntity<MessageResponse> logoutUser() {
        // In a real application, you might want to blacklist the token
        return ResponseEntity.ok(MessageResponse.success("User logged out successfully!"));
    }
}
