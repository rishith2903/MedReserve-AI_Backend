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
                .reportType(MedicalReport.ReportType.BLOOD_TEST)
                .createdAt(LocalDateTime.now().minusDays(7))
                .doctorName("Dr. Sarah Johnson")
                .description("Complete blood count test results showing all parameters within normal range")
                .reportDate(LocalDateTime.now().minusDays(7))
                .patientId(userId)
                .build(),
                
            MedicalReportResponse.builder()
                .id(2L)
                .title("Chest X-Ray")
                .reportType(MedicalReport.ReportType.X_RAY)
                .createdAt(LocalDateTime.now().minusDays(14))
                .doctorName("Dr. Michael Chen")
                .description("Chest X-ray examination for routine health checkup")
                .reportDate(LocalDateTime.now().minusDays(14))
                .patientId(userId)
                .build(),
                
            MedicalReportResponse.builder()
                .id(3L)
                .title("Lipid Profile")
                .reportType(MedicalReport.ReportType.BLOOD_TEST)
                .createdAt(LocalDateTime.now().minusDays(21))
                .doctorName("Dr. Emily Rodriguez")
                .description("Comprehensive lipid panel to assess cardiovascular risk")
                .reportDate(LocalDateTime.now().minusDays(21))
                .patientId(userId)
                .build(),
                
            MedicalReportResponse.builder()
                .id(4L)
                .title("ECG Report")
                .reportType(MedicalReport.ReportType.ECG)
                .createdAt(LocalDateTime.now().minusDays(30))
                .doctorName("Dr. James Wilson")
                .description("12-lead electrocardiogram for cardiac assessment")
                .reportDate(LocalDateTime.now().minusDays(30))
                .patientId(userId)
                .build(),
                
            MedicalReportResponse.builder()
                .id(5L)
                .title("Thyroid Function Test")
                .reportType(MedicalReport.ReportType.BLOOD_TEST)
                .createdAt(LocalDateTime.now().minusDays(45))
                .doctorName("Dr. Lisa Anderson")
                .description("Comprehensive thyroid function assessment")
                .reportDate(LocalDateTime.now().minusDays(45))
                .patientId(userId)
                .build()
        );
    }
}
