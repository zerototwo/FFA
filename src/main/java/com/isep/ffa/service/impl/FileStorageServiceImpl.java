package com.isep.ffa.service.impl;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.service.FileStorageService;
import com.isep.ffa.util.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * File Storage Service Implementation
 * Handles file upload and storage
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

  @Value("${app.file.upload-dir:uploads}")
  private String uploadDir;

  @Value("${app.file.max-size:10485760}")
  private long maxFileSize; // Default 10MB

  @Value("${app.file.allowed-types:application/pdf,image/jpeg,image/png,image/jpg,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document}")
  private String allowedTypes;

  @Override
  public BaseResponse<String> uploadFile(MultipartFile file, String folder) {
    if (file == null || file.isEmpty()) {
      return BaseResponse.error("File is required", 400);
    }

    // Validate file size
    if (file.getSize() > maxFileSize) {
      return BaseResponse.error("File size exceeds maximum allowed size: " + (maxFileSize / 1024 / 1024) + "MB", 400);
    }

    // Validate file type
    String contentType = file.getContentType();
    if (contentType == null || !isAllowedType(contentType)) {
      return BaseResponse.error("File type not allowed. Allowed types: PDF, JPEG, PNG, JPG, DOC, DOCX", 400);
    }

    try {
      // Create upload directory if it doesn't exist
      String folderPath = folder != null && !folder.isEmpty() ? folder : "documents";
      Path uploadPath = Paths.get(uploadDir, folderPath);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      // Generate unique filename
      String originalFilename = file.getOriginalFilename();
      String extension = "";
      if (originalFilename != null && originalFilename.contains(".")) {
        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
      }
      String uniqueFilename = UUID.randomUUID().toString() + extension;

      // Save file
      Path filePath = uploadPath.resolve(uniqueFilename);
      Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

      // Return relative path
      String relativePath = folderPath + "/" + uniqueFilename;
      return BaseResponse.success("File uploaded successfully", relativePath);

    } catch (IOException e) {
      return BaseResponse.error("Failed to upload file: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<Boolean> deleteFile(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      return BaseResponse.error("File path is required", 400);
    }

    try {
      Path path = Paths.get(uploadDir, filePath);
      if (Files.exists(path)) {
        Files.delete(path);
        return BaseResponse.success("File deleted successfully", true);
      } else {
        return BaseResponse.error("File not found", 404);
      }
    } catch (IOException e) {
      return BaseResponse.error("Failed to delete file: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<byte[]> getFile(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      return BaseResponse.error("File path is required", 400);
    }

    try {
      Path path = Paths.get(uploadDir, filePath);
      if (!Files.exists(path)) {
        return BaseResponse.error("File not found", 404);
      }
      byte[] fileContent = Files.readAllBytes(path);
      return BaseResponse.success("File retrieved successfully", fileContent);
    } catch (IOException e) {
      return BaseResponse.error("Failed to read file: " + e.getMessage(), 500);
    }
  }

  @Override
  public boolean fileExists(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      return false;
    }
    try {
      Path path = Paths.get(uploadDir, filePath);
      return Files.exists(path);
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Check if file type is allowed
   */
  private boolean isAllowedType(String contentType) {
    if (contentType == null) {
      return false;
    }
    String[] allowed = allowedTypes.split(",");
    for (String type : allowed) {
      if (contentType.toLowerCase().contains(type.trim().toLowerCase())) {
        return true;
      }
    }
    return false;
  }
}
