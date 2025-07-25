package com.medreserve.controller;

import com.medreserve.dto.ChatRequest;
import com.medreserve.dto.ChatResponse;
import com.medreserve.entity.User;
import com.medreserve.service.ChatbotService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
@Tag(name = "Chatbot", description = "AI-powered chatbot APIs")
public class ChatbotController {
    
    private final ChatbotService chatbotService;
    
    @PostMapping("/chat")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    @Operation(summary = "Chat with AI assistant", description = "Send a message to the AI chatbot and get a response")
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            @AuthenticationPrincipal User currentUser,
            HttpServletRequest httpRequest) {
        
        // Extract JWT token from request
        String authHeader = httpRequest.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        
        ChatResponse response = chatbotService.processMessage(request, jwtToken);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/intents")
    @Operation(summary = "Get available intents", description = "Get list of available chatbot intents and their descriptions")
    public ResponseEntity<Map<String, Object>> getAvailableIntents(
            HttpServletRequest httpRequest) {
        
        // Extract JWT token from request
        String authHeader = httpRequest.getHeader("Authorization");
        String jwtToken = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }
        
        Map<String, Object> intents = chatbotService.getAvailableIntents(jwtToken);
        return ResponseEntity.ok(intents);
    }
    
    @GetMapping("/health")
    @Operation(summary = "Chatbot service health", description = "Check chatbot service health status")
    public ResponseEntity<Map<String, Object>> getChatbotServiceHealth() {
        boolean isHealthy = chatbotService.isChatbotServiceHealthy();
        
        Map<String, Object> response = Map.of(
                "chatbot_service_healthy", isHealthy,
                "status", isHealthy ? "UP" : "DOWN",
                "timestamp", java.time.LocalDateTime.now()
        );
        
        return ResponseEntity.ok(response);
    }
}
