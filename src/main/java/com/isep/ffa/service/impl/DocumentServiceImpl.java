package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.isep.ffa.entity.Application;
import com.isep.ffa.entity.DocumentsSubmitted;
import com.isep.ffa.entity.DocumentType;
import com.isep.ffa.entity.DocumentsNeedForProject;
import com.isep.ffa.mapper.DocumentsSubmittedMapper;
import com.isep.ffa.mapper.DocumentTypeMapper;
import com.isep.ffa.mapper.DocumentsNeedForProjectMapper;
import com.isep.ffa.service.ApplicationService;
import com.isep.ffa.service.DocumentService;
import com.isep.ffa.service.FileStorageService;
import com.isep.ffa.dto.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Document Service Implementation
 * Implements business logic for document management
 */
@Service
public class DocumentServiceImpl implements DocumentService {

  @Autowired
  private DocumentTypeMapper documentTypeMapper;

  @Autowired
  private DocumentsNeedForProjectMapper documentsNeedForProjectMapper;

  @Autowired
  private DocumentsSubmittedMapper documentsSubmittedMapper;

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private FileStorageService fileStorageService;

  @Override
  public BaseResponse<List<DocumentType>> getDocumentTypesByProject(Long projectId) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    List<DocumentType> documentTypes = documentTypeMapper.findByProjectId(projectId);
    return BaseResponse.success("Document types retrieved successfully", documentTypes);
  }

  @Override
  public BaseResponse<List<DocumentsNeedForProject>> getDocumentRequirementsByProject(Long projectId) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    List<DocumentsNeedForProject> requirements = documentsNeedForProjectMapper.findByProjectId(projectId);
    return BaseResponse.success("Document requirements retrieved successfully", requirements);
  }

  @Override
  public BaseResponse<DocumentsSubmitted> uploadDocument(Long applicationId, Long documentTypeId, String filePath) {
    if (applicationId == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    if (documentTypeId == null) {
      return BaseResponse.error("Document type ID is required", 400);
    }
    if (filePath == null || filePath.trim().isEmpty()) {
      return BaseResponse.error("File path is required", 400);
    }

    // Verify application exists
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }

    // Verify document type exists
    DocumentType documentType = documentTypeMapper.selectById(documentTypeId);
    if (documentType == null || Boolean.TRUE.equals(documentType.getIsDeleted())) {
      return BaseResponse.error("Document type not found with ID: " + documentTypeId, 404);
    }

    // Check if document type belongs to the project
    if (!application.getProjectId().equals(documentType.getProjectId())) {
      return BaseResponse.error("Document type does not belong to this project", 400);
    }

    // Check document requirements
    DocumentsNeedForProject requirement = documentsNeedForProjectMapper
        .findByProjectIdAndDocumentTypeId(application.getProjectId(), documentTypeId);
    if (requirement != null) {
      // Check max number
      Long currentCount = documentsSubmittedMapper.countByApplicationIdAndDocumentTypeId(applicationId, documentTypeId);
      if (requirement.getMaxNumber() != null && currentCount >= requirement.getMaxNumber()) {
        return BaseResponse.error("Maximum number of documents for this type has been reached", 400);
      }
    }

    // Create document record
    DocumentsSubmitted document = new DocumentsSubmitted();
    document.setApplicationId(applicationId);
    document.setDocumentTypeId(documentTypeId);
    document.setPath(filePath.trim());

    boolean saved = documentsSubmittedMapper.insert(document) > 0;
    if (!saved) {
      return BaseResponse.error("Failed to upload document", 500);
    }

    return BaseResponse.success("Document uploaded successfully", document);
  }

  @Override
  public BaseResponse<List<DocumentsSubmitted>> getDocumentsByApplication(Long applicationId) {
    if (applicationId == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    List<DocumentsSubmitted> documents = documentsSubmittedMapper.findByApplicationId(applicationId);
    return BaseResponse.success("Documents retrieved successfully", documents);
  }

  @Override
  public BaseResponse<Boolean> deleteDocument(Long documentId) {
    if (documentId == null) {
      return BaseResponse.error("Document ID is required", 400);
    }
    DocumentsSubmitted document = documentsSubmittedMapper.selectById(documentId);
    if (document == null || Boolean.TRUE.equals(document.getIsDeleted())) {
      return BaseResponse.error("Document not found with ID: " + documentId, 404);
    }
    boolean deleted = documentsSubmittedMapper.deleteById(documentId) > 0;
    if (!deleted) {
      return BaseResponse.error("Failed to delete document", 500);
    }
    return BaseResponse.success("Document deleted successfully", true);
  }

  @Override
  public BaseResponse<Boolean> validateRequiredDocuments(Long applicationId, Long projectId) {
    if (applicationId == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }

    // Get mandatory document requirements
    List<DocumentsNeedForProject> mandatoryRequirements = documentsNeedForProjectMapper
        .findMandatoryByProjectId(projectId);

    if (mandatoryRequirements == null || mandatoryRequirements.isEmpty()) {
      return BaseResponse.success("No mandatory documents required", true);
    }

    // Check each mandatory requirement
    for (DocumentsNeedForProject requirement : mandatoryRequirements) {
      Long count = documentsSubmittedMapper
          .countByApplicationIdAndDocumentTypeId(applicationId, requirement.getDocumentTypeId());
      Integer minNumber = requirement.getMinNumber() != null ? requirement.getMinNumber() : 1;
      if (count < minNumber) {
        // Get document type name
        DocumentType documentType = documentTypeMapper.selectById(requirement.getDocumentTypeId());
        String documentTypeName = documentType != null ? documentType.getName()
            : "Document Type " + requirement.getDocumentTypeId();
        return BaseResponse.error(
            "Missing required document: " + documentTypeName + " (minimum: " + minNumber + ")",
            400);
      }
    }

    return BaseResponse.success("All required documents are uploaded", true);
  }

  @Override
  public BaseResponse<DocumentsSubmitted> getDocumentById(Long documentId) {
    if (documentId == null) {
      return BaseResponse.error("Document ID is required", 400);
    }
    DocumentsSubmitted document = documentsSubmittedMapper.selectById(documentId);
    if (document == null || Boolean.TRUE.equals(document.getIsDeleted())) {
      return BaseResponse.error("Document not found with ID: " + documentId, 404);
    }
    return BaseResponse.success("Document retrieved successfully", document);
  }

  @Override
  public BaseResponse<DocumentsSubmitted> uploadDocumentFile(Long applicationId, Long documentTypeId,
      MultipartFile file) {
    if (applicationId == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    if (documentTypeId == null) {
      return BaseResponse.error("Document type ID is required", 400);
    }
    if (file == null || file.isEmpty()) {
      return BaseResponse.error("File is required", 400);
    }

    // Verify application exists
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }

    // Verify document type exists
    DocumentType documentType = documentTypeMapper.selectById(documentTypeId);
    if (documentType == null || Boolean.TRUE.equals(documentType.getIsDeleted())) {
      return BaseResponse.error("Document type not found with ID: " + documentTypeId, 404);
    }

    // Check if document type belongs to the project
    if (!application.getProjectId().equals(documentType.getProjectId())) {
      return BaseResponse.error("Document type does not belong to this project", 400);
    }

    // Check document requirements
    DocumentsNeedForProject requirement = documentsNeedForProjectMapper
        .findByProjectIdAndDocumentTypeId(application.getProjectId(), documentTypeId);
    if (requirement != null) {
      // Check max number
      Long currentCount = documentsSubmittedMapper.countByApplicationIdAndDocumentTypeId(applicationId, documentTypeId);
      if (requirement.getMaxNumber() != null && currentCount >= requirement.getMaxNumber()) {
        return BaseResponse.error("Maximum number of documents for this type has been reached", 400);
      }
    }

    // Upload file
    BaseResponse<String> uploadResponse = fileStorageService.uploadFile(file, "documents");
    if (!uploadResponse.isSuccess()) {
      return BaseResponse.error("Failed to upload file: " + uploadResponse.getMessage(), uploadResponse.getStatus());
    }

    String filePath = uploadResponse.getData();

    // Create document record
    DocumentsSubmitted document = new DocumentsSubmitted();
    document.setApplicationId(applicationId);
    document.setDocumentTypeId(documentTypeId);
    document.setPath(filePath);

    boolean saved = documentsSubmittedMapper.insert(document) > 0;
    if (!saved) {
      // If database save fails, delete the uploaded file
      fileStorageService.deleteFile(filePath);
      return BaseResponse.error("Failed to save document record", 500);
    }

    return BaseResponse.success("Document uploaded successfully", document);
  }
}
