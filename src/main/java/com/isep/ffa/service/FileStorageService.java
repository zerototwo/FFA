package com.isep.ffa.service;

import com.isep.ffa.dto.BaseResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * File Storage Service Interface
 * Provides file upload and storage functionality
 */
public interface FileStorageService {

  /**
   * Upload file
   * 
   * @param file   file to upload
   * @param folder folder name (e.g., "documents", "applications")
   * @return file path
   */
  BaseResponse<String> uploadFile(MultipartFile file, String folder);

  /**
   * Delete file
   * 
   * @param filePath file path to delete
   * @return operation result
   */
  BaseResponse<Boolean> deleteFile(String filePath);

  /**
   * Get file content
   * 
   * @param filePath file path
   * @return file bytes
   */
  BaseResponse<byte[]> getFile(String filePath);

  /**
   * Check if file exists
   * 
   * @param filePath file path
   * @return true if exists
   */
  boolean fileExists(String filePath);
}
