package com.medreserve.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    
    private final Path fileStorageLocation;
    
    // Allowed file types for medical reports
    private static final List<String> ALLOWED_REPORT_TYPES = Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/jpg", 
            "image/png",
            "image/gif",
            "image/bmp",
            "image/tiff"
    );
    
    // Allowed file types for prescriptions (mainly PDF)
    private static final List<String> ALLOWED_PRESCRIPTION_TYPES = Arrays.asList(
            "application/pdf",
            "image/jpeg",
            "image/jpg",
            "image/png"
    );
    
    // Maximum file size (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    
    public FileStorageService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        
        try {
            Files.createDirectories(this.fileStorageLocation);
            
            // Create subdirectories
            Files.createDirectories(this.fileStorageLocation.resolve("reports"));
            Files.createDirectories(this.fileStorageLocation.resolve("prescriptions"));
            
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    
    public String storeReportFile(MultipartFile file, Long patientId) {
        return storeFile(file, "reports", patientId, ALLOWED_REPORT_TYPES);
    }
    
    public String storePrescriptionFile(MultipartFile file, Long doctorId) {
        return storeFile(file, "prescriptions", doctorId, ALLOWED_PRESCRIPTION_TYPES);
    }
    
    private String storeFile(MultipartFile file, String category, Long userId, List<String> allowedTypes) {
        // Validate file
        validateFile(file, allowedTypes);
        
        // Generate unique filename
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFileName);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uniqueFileName = String.format("%s_%s_%s_%s%s", 
                category, userId, timestamp, UUID.randomUUID().toString().substring(0, 8), fileExtension);
        
        try {
            // Check if the filename contains invalid characters
            if (originalFileName.contains("..")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                        "Filename contains invalid path sequence " + originalFileName);
            }
            
            // Copy file to the target location
            Path categoryPath = this.fileStorageLocation.resolve(category);
            Path targetLocation = categoryPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            log.info("File stored successfully: {} -> {}", originalFileName, uniqueFileName);
            return uniqueFileName;
            
        } catch (IOException ex) {
            log.error("Could not store file {}. Error: {}", originalFileName, ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Could not store file " + originalFileName + ". Please try again!");
        }
    }
    
    public Resource loadFileAsResource(String fileName, String category) {
        try {
            Path categoryPath = this.fileStorageLocation.resolve(category);
            Path filePath = categoryPath.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                return resource;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + fileName);
        }
    }
    
    public void deleteFile(String fileName, String category) {
        try {
            Path categoryPath = this.fileStorageLocation.resolve(category);
            Path filePath = categoryPath.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
            log.info("File deleted successfully: {}", fileName);
        } catch (IOException ex) {
            log.error("Could not delete file {}. Error: {}", fileName, ex.getMessage());
        }
    }
    
    public String getFilePath(String fileName, String category) {
        Path categoryPath = this.fileStorageLocation.resolve(category);
        return categoryPath.resolve(fileName).toString();
    }
    
    private void validateFile(MultipartFile file, List<String> allowedTypes) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please select a file to upload");
        }
        
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "File size exceeds maximum allowed size of 10MB");
        }
        
        String contentType = file.getContentType();
        if (contentType == null || !allowedTypes.contains(contentType.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "File type not allowed. Allowed types: " + String.join(", ", allowedTypes));
        }
        
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name is required");
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }
    
    public boolean isValidReportFile(MultipartFile file) {
        try {
            validateFile(file, ALLOWED_REPORT_TYPES);
            return true;
        } catch (ResponseStatusException e) {
            return false;
        }
    }
    
    public boolean isValidPrescriptionFile(MultipartFile file) {
        try {
            validateFile(file, ALLOWED_PRESCRIPTION_TYPES);
            return true;
        } catch (ResponseStatusException e) {
            return false;
        }
    }
}
