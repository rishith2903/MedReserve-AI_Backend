package com.medreserve.controller;

import com.medreserve.dto.MedicalReportResponse;
import com.medreserve.dto.MessageResponse;
import com.medreserve.entity.User;
import com.medreserve.service.MedicalReportsService;
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
@RequestMapping("/medical-reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Medical Reports", description = "Medical reports management endpoints")
public class MedicalReportsController {

    private final MedicalReportsService medicalReportsService;

    @GetMapping
    @Operation(summary = "Get user medical reports", description = "Get all medical reports for the authenticated user")
    public ResponseEntity<Page<MedicalReportResponse>> getUserMedicalReports(
            @AuthenticationPrincipal User currentUser,
            Pageable pageable) {
        try {
            log.info("Fetching medical reports for user: {}", currentUser.getEmail());
            Page<MedicalReportResponse> reports = medicalReportsService.getUserMedicalReports(currentUser.getId(), pageable);
            log.info("Successfully fetched {} medical reports", reports.getTotalElements());
            return ResponseEntity.ok(reports);
        } catch (Exception e) {
            log.error("Error fetching medical reports for user {}: {}", currentUser.getEmail(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{reportId}")
    @Operation(summary = "Get medical report by ID", description = "Get a specific medical report by ID")
    public ResponseEntity<MedicalReportResponse> getMedicalReportById(
            @PathVariable Long reportId,
            @AuthenticationPrincipal User currentUser) {
        try {
            log.info("Fetching medical report {} for user: {}", reportId, currentUser.getEmail());
            MedicalReportResponse report = medicalReportsService.getMedicalReportById(reportId, currentUser.getId());
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Error fetching medical report {} for user {}: {}", reportId, currentUser.getEmail(), e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/types")
    @Operation(summary = "Get report types", description = "Get all available medical report types")
    public ResponseEntity<String[]> getReportTypes() {
        String[] reportTypes = {
            "Lab Report",
            "Imaging",
            "Cardiac",
            "Blood Test",
            "X-Ray",
            "MRI",
            "CT Scan",
            "Ultrasound",
            "Pathology",
            "Biopsy"
        };
        return ResponseEntity.ok(reportTypes);
    }

    @PostMapping("/{reportId}/download")
    @Operation(summary = "Download medical report", description = "Download a medical report file")
    public ResponseEntity<MessageResponse> downloadMedicalReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal User currentUser) {
        try {
            log.info("Download request for medical report {} by user: {}", reportId, currentUser.getEmail());
            // In a real implementation, this would return the actual file
            // For now, we'll return a success message
            return ResponseEntity.ok(MessageResponse.success("Report download initiated. Check your downloads folder."));
        } catch (Exception e) {
            log.error("Error downloading medical report {} for user {}: {}", reportId, currentUser.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(MessageResponse.error("Failed to download report"));
        }
    }
}
