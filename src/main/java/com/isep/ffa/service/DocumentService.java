package com.isep.ffa.service;

import com.isep.ffa.entity.DocumentsSubmitted;
import com.isep.ffa.entity.DocumentType;
import com.isep.ffa.entity.DocumentsNeedForProject;
import com.isep.ffa.dto.BaseResponse;

import java.util.List;

/**
 * Document Service Interface
 * Provides business logic for document management
 */
public interface DocumentService {

  /**
   * Get document types for a project
   * 
   * @param projectId project ID
   * @return list of document types
   */
  BaseResponse<List<DocumentType>> getDocumentTypesByProject(Long projectId);

  /**
   * Get document requirements for a project
   * 
   * @param projectId project ID
   * @return list of document requirements
   */
  BaseResponse<List<DocumentsNeedForProject>> getDocumentRequirementsByProject(Long projectId);

  /**
   * Upload document for application
   * 
   * @param applicationId  application ID
   * @param documentTypeId document type ID
   * @param filePath       file path
   * @return created document
   */
  BaseResponse<DocumentsSubmitted> uploadDocument(Long applicationId, Long documentTypeId, String filePath);

  /**
   * Upload document file for application
   * 
   * @param applicationId  application ID
   * @param documentTypeId document type ID
   * @param file           uploaded file
   * @return created document
   */
  BaseResponse<DocumentsSubmitted> uploadDocumentFile(Long applicationId, Long documentTypeId,
      org.springframework.web.multipart.MultipartFile file);

  /**
   * Get documents for application
   * 
   * @param applicationId application ID
   * @return list of documents
   */
  BaseResponse<List<DocumentsSubmitted>> getDocumentsByApplication(Long applicationId);

  /**
   * Delete document
   * 
   * @param documentId document ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteDocument(Long documentId);

  /**
   * Check if required documents are uploaded for application
   * 
   * @param applicationId application ID
   * @param projectId     project ID
   * @return validation result
   */
  BaseResponse<Boolean> validateRequiredDocuments(Long applicationId, Long projectId);

  /**
   * Get document by ID
   * 
   * @param documentId document ID
   * @return document
   */
  BaseResponse<DocumentsSubmitted> getDocumentById(Long documentId);
}
