package com.medreserve.controller;

import com.medreserve.dto.SpecialtyPredictionResponse;
import com.medreserve.dto.SymptomAnalysisRequest;
import com.medreserve.entity.User;
import com.medreserve.service.MLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ml")
@RequiredArgsConstructor
@Tag(name = "Machine Learning", description = "ML-powered medical analysis APIs")
public class MLController {
    
    private final MLService mlService;
    
    @PostMapping("/predict-specialty")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    @Operation(summary = "Predict medical specialty", description = "Predict recommended medical specialty based on symptoms")
    public ResponseEntity<SpecialtyPredictionResponse> predictSpecialty(
            @Valid @RequestBody SymptomAnalysisRequest request,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest httpRequest) {
        
        // Extract JWT token from request
        String authHeader = httpRequest.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        
        SpecialtyPredictionResponse response = mlService.predictSpecialty(request, jwtToken);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/specialties")
    @Operation(summary = "Get available specialties", description = "Get list of available medical specialties")
    public ResponseEntity<List<String>> getAvailableSpecialties(
            HttpServletRequest httpRequest) {
        
        // Extract JWT token from request
        String authHeader = httpRequest.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        
        List<String> specialties = mlService.getAvailableSpecialties(jwtToken);
        return ResponseEntity.ok(specialties);
    }
    
    @GetMapping("/health")
    @Operation(summary = "ML service health", description = "Check ML service health status")
    public ResponseEntity<Map<String, Object>> getMLServiceHealth() {
        boolean isHealthy = mlService.isMLServiceHealthy();
        
        Map<String, Object> response = Map.of(
                "ml_service_healthy", isHealthy,
                "status", isHealthy ? "UP" : "DOWN",
                "timestamp", java.time.LocalDateTime.now()
        );
        
        return ResponseEntity.ok(response);
    }
}
