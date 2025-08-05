package com.medreserve.service;

import com.medreserve.dto.MedicalReportResponse;
import com.medreserve.entity.MedicalReport;
import com.medreserve.repository.MedicalReportRepository;
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
public class MedicalReportsService {

    private final MedicalReportRepository medicalReportRepository;

    @Transactional(readOnly = true)
    public Page<MedicalReportResponse> getUserMedicalReports(Long userId, Pageable pageable) {
        try {
            // For now, return demo data since we don't have the full entity structure
            // In a real implementation, this would query the database
            List<MedicalReportResponse> demoReports = createDemoReports(userId);
            
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), demoReports.size());
            
            if (start > demoReports.size()) {
                return new PageImpl<>(List.of(), pageable, demoReports.size());
            }
            
            List<MedicalReportResponse> pageContent = demoReports.subList(start, end);
            return new PageImpl<>(pageContent, pageable, demoReports.size());
            
        } catch (Exception e) {
            log.error("Error fetching medical reports for user {}: {}", userId, e.getMessage());
            // Return empty page on error
            return new PageImpl<>(List.of(), pageable, 0);
        }
    }

    @Transactional(readOnly = true)
    public MedicalReportResponse getMedicalReportById(Long reportId, Long userId) {
        try {
            // For now, return demo data
            List<MedicalReportResponse> demoReports = createDemoReports(userId);
            return demoReports.stream()
                    .filter(report -> report.getId().equals(reportId))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error fetching medical report {} for user {}: {}", reportId, userId, e.getMessage());
            return null;
        }
    }

    private List<MedicalReportResponse> createDemoReports(Long userId) {
        return Arrays.asList(
            MedicalReportResponse.builder()
                .id(1L)
                .title("Complete Blood Count (CBC)")
                .reportType("Lab Report")
                .category("Blood Test")
                .createdAt(LocalDateTime.now().minusDays(7))
                .doctorName("Dr. Sarah Johnson")
                .status("Normal")
                .description("Complete blood count test results showing all parameters within normal range")
                .findings("Hemoglobin: 14.2 g/dL (Normal), WBC: 7,200/μL (Normal), Platelets: 280,000/μL (Normal)")
                .recommendations("Continue current health regimen. Follow-up in 6 months.")
                .fileUrl("/reports/cbc_" + userId + "_001.pdf")
                .isDownloadable(true)
                .build(),
                
            MedicalReportResponse.builder()
                .id(2L)
                .title("Chest X-Ray")
                .reportType("Imaging")
                .category("X-Ray")
                .createdAt(LocalDateTime.now().minusDays(14))
                .doctorName("Dr. Michael Chen")
                .status("Normal")
                .description("Chest X-ray examination for routine health checkup")
                .findings("Clear lung fields, normal heart size, no acute abnormalities detected")
                .recommendations("No immediate action required. Annual follow-up recommended.")
                .fileUrl("/reports/xray_" + userId + "_002.pdf")
                .isDownloadable(true)
                .build(),
                
            MedicalReportResponse.builder()
                .id(3L)
                .title("Lipid Profile")
                .reportType("Lab Report")
                .category("Blood Test")
                .createdAt(LocalDateTime.now().minusDays(21))
                .doctorName("Dr. Emily Rodriguez")
                .status("Attention Required")
                .description("Comprehensive lipid panel to assess cardiovascular risk")
                .findings("Total Cholesterol: 220 mg/dL (Borderline High), LDL: 140 mg/dL (Borderline High), HDL: 45 mg/dL (Low)")
                .recommendations("Dietary modifications recommended. Increase physical activity. Follow-up in 3 months.")
                .fileUrl("/reports/lipid_" + userId + "_003.pdf")
                .isDownloadable(true)
                .build(),
                
            MedicalReportResponse.builder()
                .id(4L)
                .title("ECG Report")
                .reportType("Cardiac")
                .category("Electrocardiogram")
                .createdAt(LocalDateTime.now().minusDays(30))
                .doctorName("Dr. James Wilson")
                .status("Normal")
                .description("12-lead electrocardiogram for cardiac assessment")
                .findings("Normal sinus rhythm, rate 72 bpm, normal axis, no ST-T abnormalities")
                .recommendations("Continue current medications. Annual cardiac screening recommended.")
                .fileUrl("/reports/ecg_" + userId + "_004.pdf")
                .isDownloadable(true)
                .build(),
                
            MedicalReportResponse.builder()
                .id(5L)
                .title("Thyroid Function Test")
                .reportType("Lab Report")
                .category("Hormone Test")
                .createdAt(LocalDateTime.now().minusDays(45))
                .doctorName("Dr. Lisa Anderson")
                .status("Normal")
                .description("Comprehensive thyroid function assessment")
                .findings("TSH: 2.1 mIU/L (Normal), Free T4: 1.3 ng/dL (Normal), Free T3: 3.2 pg/mL (Normal)")
                .recommendations("Thyroid function normal. Continue current treatment plan.")
                .fileUrl("/reports/thyroid_" + userId + "_005.pdf")
                .isDownloadable(true)
                .build()
        );
    }
}
