package com.medreserve.service;

import com.medreserve.dto.PrescriptionResponse;
import com.medreserve.entity.Prescription;
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
                .medications("Lisinopril")
                .dosage("10mg")
                .instructions("Take with or without food. Monitor blood pressure regularly.")
                .prescriptionDate(LocalDateTime.now().minusDays(5))
                .doctorName("Dr. Sarah Johnson")
                .status(Prescription.PrescriptionStatus.ACTIVE)
                .notes("For blood pressure management")
                .createdAt(LocalDateTime.now().minusDays(5))
                .build(),
                
            PrescriptionResponse.builder()
                .id(2L)
                .medications("Metformin")
                .dosage("500mg")
                .instructions("Take with meals to reduce stomach upset.")
                .prescriptionDate(LocalDateTime.now().minusDays(15))
                .doctorName("Dr. Michael Chen")
                .status(Prescription.PrescriptionStatus.ACTIVE)
                .notes("For diabetes management")
                .createdAt(LocalDateTime.now().minusDays(15))
                .build(),
                
            PrescriptionResponse.builder()
                .id(3L)
                .medications("Amoxicillin")
                .dosage("250mg")
                .instructions("Take with food. Complete the full course even if feeling better.")
                .prescriptionDate(LocalDateTime.now().minusDays(30))
                .doctorName("Dr. Emily Rodriguez")
                .status(Prescription.PrescriptionStatus.COMPLETED)
                .notes("For bacterial infection treatment")
                .createdAt(LocalDateTime.now().minusDays(30))
                .build(),
                
            PrescriptionResponse.builder()
                .id(4L)
                .medications("Omeprazole")
                .dosage("20mg")
                .instructions("Take before breakfast on empty stomach.")
                .prescriptionDate(LocalDateTime.now().minusDays(10))
                .doctorName("Dr. James Wilson")
                .status(Prescription.PrescriptionStatus.ACTIVE)
                .notes("For acid reflux management")
                .createdAt(LocalDateTime.now().minusDays(10))
                .build()
        );
    }
}
