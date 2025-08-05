package com.medreserve.controller;

import com.medreserve.dto.PrescriptionResponse;
import com.medreserve.dto.MessageResponse;
import com.medreserve.entity.User;
import com.medreserve.service.PrescriptionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Prescriptions", description = "Prescription management endpoints")
public class PrescriptionsController {

    private final PrescriptionsService prescriptionsService;

    @GetMapping
    @Operation(summary = "Get user prescriptions", description = "Get all prescriptions for the authenticated user")
    public ResponseEntity<Page<PrescriptionResponse>> getUserPrescriptions(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        try {
            log.info("Fetching prescriptions for user: {}", currentUser.getEmail());
            Page<PrescriptionResponse> prescriptions = prescriptionsService.getUserPrescriptions(currentUser.getId(), pageable);
            log.info("Successfully fetched {} prescriptions", prescriptions.getTotalElements());
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            log.error("Error fetching prescriptions for user {}: {}", currentUser.getEmail(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{prescriptionId}")
    @Operation(summary = "Get prescription by ID", description = "Get a specific prescription by ID")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(
            @PathVariable Long prescriptionId,
            @AuthenticationPrincipal User currentUser) {
        try {
            log.info("Fetching prescription {} for user: {}", prescriptionId, currentUser.getEmail());
            PrescriptionResponse prescription = prescriptionsService.getPrescriptionById(prescriptionId, currentUser.getId());
            if (prescription != null) {
                return ResponseEntity.ok(prescription);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching prescription {} for user {}: {}", prescriptionId, currentUser.getEmail(), e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Get active prescriptions", description = "Get all active prescriptions for the authenticated user")
    public ResponseEntity<Page<PrescriptionResponse>> getActivePrescriptions(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        try {
            log.info("Fetching active prescriptions for user: {}", currentUser.getEmail());
            Page<PrescriptionResponse> prescriptions = prescriptionsService.getActivePrescriptions(currentUser.getId(), pageable);
            return ResponseEntity.ok(prescriptions);
        } catch (Exception e) {
            log.error("Error fetching active prescriptions for user {}: {}", currentUser.getEmail(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{prescriptionId}/refill")
    @Operation(summary = "Request prescription refill", description = "Request a refill for an existing prescription")
    public ResponseEntity<MessageResponse> requestRefill(
            @PathVariable Long prescriptionId,
            @AuthenticationPrincipal User currentUser) {
        try {
            log.info("Refill request for prescription {} by user: {}", prescriptionId, currentUser.getEmail());
            boolean success = prescriptionsService.requestRefill(prescriptionId, currentUser.getId());
            if (success) {
                return ResponseEntity.ok(MessageResponse.success("Refill request submitted successfully. Your doctor will review and approve."));
            } else {
                return ResponseEntity.badRequest().body(MessageResponse.error("Unable to process refill request"));
            }
        } catch (Exception e) {
            log.error("Error processing refill request for prescription {} by user {}: {}", prescriptionId, currentUser.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(MessageResponse.error("Failed to process refill request"));
        }
    }

    @GetMapping("/medications")
    @Operation(summary = "Get medication list", description = "Get list of common medications")
    public ResponseEntity<String[]> getMedications() {
        String[] medications = {
            "Amoxicillin",
            "Lisinopril",
            "Metformin",
            "Amlodipine",
            "Metoprolol",
            "Omeprazole",
            "Simvastatin",
            "Losartan",
            "Albuterol",
            "Gabapentin"
        };
        return ResponseEntity.ok(medications);
    }
}
