package com.medreserve.controller;

import com.medreserve.dto.MessageResponse;
import com.medreserve.dto.PrescriptionRequest;
import com.medreserve.dto.PrescriptionResponse;
import com.medreserve.entity.User;
import com.medreserve.service.PrescriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/prescriptions")
@RequiredArgsConstructor
@Tag(name = "Prescriptions", description = "Prescription management APIs")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;
    
    @PostMapping
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Create prescription", description = "Create a new prescription for an appointment")
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @Valid @RequestBody PrescriptionRequest request,
            @AuthenticationPrincipal User currentUser) {
        PrescriptionResponse response = prescriptionService.createPrescription(request, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping(value = "/with-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Create prescription with file", description = "Create a prescription with an attached PDF file")
    public ResponseEntity<PrescriptionResponse> createPrescriptionWithFile(
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("prescription") @Valid PrescriptionRequest request,
            @AuthenticationPrincipal User currentUser) {
        PrescriptionResponse response = prescriptionService.createPrescriptionWithFile(file, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/patient/my-prescriptions")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Get patient prescriptions", description = "Get all prescriptions for the current patient")
    public ResponseEntity<Page<PrescriptionResponse>> getMyPrescriptions(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        Page<PrescriptionResponse> prescriptions = prescriptionService.getPatientPrescriptions(currentUser.getId(), pageable);
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/doctor/my-prescriptions")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Get doctor prescriptions", description = "Get all prescriptions created by the current doctor")
    public ResponseEntity<Page<PrescriptionResponse>> getDoctorPrescriptions(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        Page<PrescriptionResponse> prescriptions = prescriptionService.getDoctorPrescriptions(currentUser.getId(), pageable);
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/appointment/{appointmentId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    @Operation(summary = "Get appointment prescriptions", description = "Get all prescriptions for a specific appointment")
    public ResponseEntity<List<PrescriptionResponse>> getAppointmentPrescriptions(
            @PathVariable Long appointmentId,
            @AuthenticationPrincipal User currentUser) {
        List<PrescriptionResponse> prescriptions = prescriptionService.getAppointmentPrescriptions(appointmentId, currentUser.getId());
        return ResponseEntity.ok(prescriptions);
    }
    
    @GetMapping("/{prescriptionId}")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    @Operation(summary = "Get prescription details", description = "Get details of a specific prescription")
    public ResponseEntity<PrescriptionResponse> getPrescription(
            @PathVariable Long prescriptionId,
            @AuthenticationPrincipal User currentUser) {
        PrescriptionResponse prescription = prescriptionService.getPrescriptionById(prescriptionId, currentUser.getId());
        return ResponseEntity.ok(prescription);
    }
    
    @GetMapping("/{prescriptionId}/download")
    @PreAuthorize("hasRole('PATIENT') or hasRole('DOCTOR')")
    @Operation(summary = "Download prescription file", description = "Download the prescription PDF file")
    public ResponseEntity<Resource> downloadPrescription(
            @PathVariable Long prescriptionId,
            @AuthenticationPrincipal User currentUser) {
        Resource resource = prescriptionService.downloadPrescription(prescriptionId, currentUser.getId());
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    @PutMapping("/{prescriptionId}")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Update prescription", description = "Update an existing prescription")
    public ResponseEntity<PrescriptionResponse> updatePrescription(
            @PathVariable Long prescriptionId,
            @Valid @RequestBody PrescriptionRequest request,
            @AuthenticationPrincipal User currentUser) {
        PrescriptionResponse response = prescriptionService.updatePrescription(prescriptionId, request, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{prescriptionId}")
    @PreAuthorize("hasRole('DOCTOR')")
    @Operation(summary = "Delete prescription", description = "Delete a prescription")
    public ResponseEntity<MessageResponse> deletePrescription(
            @PathVariable Long prescriptionId,
            @AuthenticationPrincipal User currentUser) {
        MessageResponse response = prescriptionService.deletePrescription(prescriptionId, currentUser.getId());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('PATIENT')")
    @Operation(summary = "Search prescriptions", description = "Search prescriptions by keyword")
    public ResponseEntity<List<PrescriptionResponse>> searchPrescriptions(
            @RequestParam String keyword,
            @AuthenticationPrincipal User currentUser) {
        List<PrescriptionResponse> prescriptions = prescriptionService.searchPrescriptions(currentUser.getId(), keyword);
        return ResponseEntity.ok(prescriptions);
    }
}
