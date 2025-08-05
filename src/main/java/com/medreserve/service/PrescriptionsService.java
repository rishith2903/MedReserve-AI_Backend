package com.medreserve.service;

import com.medreserve.dto.PrescriptionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionsService {

    @Transactional(readOnly = true)
    public Page<PrescriptionResponse> getUserPrescriptions(Long userId, Pageable pageable) {
        try {
            // For now, return demo data since we don't have the full entity structure
            List<PrescriptionResponse> demoPrescriptions = createDemoPrescriptions(userId);
            
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), demoPrescriptions.size());
            
            if (start > demoPrescriptions.size()) {
                return new PageImpl<>(List.of(), pageable, demoPrescriptions.size());
            }
            
            List<PrescriptionResponse> pageContent = demoPrescriptions.subList(start, end);
            return new PageImpl<>(pageContent, pageable, demoPrescriptions.size());
            
        } catch (Exception e) {
            log.error("Error fetching prescriptions for user {}: {}", userId, e.getMessage());
            return new PageImpl<>(List.of(), pageable, 0);
        }
    }

    @Transactional(readOnly = true)
    public PrescriptionResponse getPrescriptionById(Long prescriptionId, Long userId) {
        try {
            List<PrescriptionResponse> demoPrescriptions = createDemoPrescriptions(userId);
            return demoPrescriptions.stream()
                    .filter(prescription -> prescription.getId().equals(prescriptionId))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error fetching prescription {} for user {}: {}", prescriptionId, userId, e.getMessage());
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Page<PrescriptionResponse> getActivePrescriptions(Long userId, Pageable pageable) {
        try {
            List<PrescriptionResponse> demoPrescriptions = createDemoPrescriptions(userId);
            List<PrescriptionResponse> activePrescriptions = demoPrescriptions.stream()
                    .filter(prescription -> "Active".equals(prescription.getStatus()))
                    .collect(Collectors.toList());
            
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), activePrescriptions.size());
            
            if (start > activePrescriptions.size()) {
                return new PageImpl<>(List.of(), pageable, activePrescriptions.size());
            }
            
            List<PrescriptionResponse> pageContent = activePrescriptions.subList(start, end);
            return new PageImpl<>(pageContent, pageable, activePrescriptions.size());
            
        } catch (Exception e) {
            log.error("Error fetching active prescriptions for user {}: {}", userId, e.getMessage());
            return new PageImpl<>(List.of(), pageable, 0);
        }
    }

    @Transactional
    public boolean requestRefill(Long prescriptionId, Long userId) {
        try {
            log.info("Processing refill request for prescription {} by user {}", prescriptionId, userId);
            // In a real implementation, this would update the database
            // For now, we'll just return success
            return true;
        } catch (Exception e) {
            log.error("Error processing refill request for prescription {} by user {}: {}", prescriptionId, userId, e.getMessage());
            return false;
        }
    }

    private List<PrescriptionResponse> createDemoPrescriptions(Long userId) {
        return Arrays.asList(
            PrescriptionResponse.builder()
                .id(1L)
                .medicationName("Lisinopril")
                .dosage("10mg")
                .frequency("Once daily")
                .duration("30 days")
                .instructions("Take with or without food. Monitor blood pressure regularly.")
                .prescribedDate(LocalDateTime.now().minusDays(5))
                .doctorName("Dr. Sarah Johnson")
                .status("Active")
                .refillsRemaining(2)
                .totalRefills(3)
                .pharmacyName("MedReserve Pharmacy")
                .notes("For blood pressure management")
                .sideEffects("May cause dizziness, dry cough")
                .warnings("Do not stop suddenly. Consult doctor before discontinuing.")
                .build(),
                
            PrescriptionResponse.builder()
                .id(2L)
                .medicationName("Metformin")
                .dosage("500mg")
                .frequency("Twice daily")
                .duration("90 days")
                .instructions("Take with meals to reduce stomach upset.")
                .prescribedDate(LocalDateTime.now().minusDays(15))
                .doctorName("Dr. Michael Chen")
                .status("Active")
                .refillsRemaining(1)
                .totalRefills(2)
                .pharmacyName("MedReserve Pharmacy")
                .notes("For diabetes management")
                .sideEffects("Nausea, diarrhea, metallic taste")
                .warnings("Monitor blood sugar levels. Avoid alcohol.")
                .build(),
                
            PrescriptionResponse.builder()
                .id(3L)
                .medicationName("Amoxicillin")
                .dosage("250mg")
                .frequency("Three times daily")
                .duration("7 days")
                .instructions("Take with food. Complete the full course even if feeling better.")
                .prescribedDate(LocalDateTime.now().minusDays(30))
                .doctorName("Dr. Emily Rodriguez")
                .status("Completed")
                .refillsRemaining(0)
                .totalRefills(0)
                .pharmacyName("MedReserve Pharmacy")
                .notes("For bacterial infection treatment")
                .sideEffects("Nausea, diarrhea, allergic reactions")
                .warnings("Complete full course. Report any allergic reactions immediately.")
                .build(),
                
            PrescriptionResponse.builder()
                .id(4L)
                .medicationName("Omeprazole")
                .dosage("20mg")
                .frequency("Once daily")
                .duration("60 days")
                .instructions("Take before breakfast on empty stomach.")
                .prescribedDate(LocalDateTime.now().minusDays(10))
                .doctorName("Dr. James Wilson")
                .status("Active")
                .refillsRemaining(3)
                .totalRefills(5)
                .pharmacyName("MedReserve Pharmacy")
                .notes("For acid reflux management")
                .sideEffects("Headache, stomach pain, diarrhea")
                .warnings("Long-term use may affect magnesium levels.")
                .build()
        );
    }
}
