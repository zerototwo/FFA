package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.dto.request.ApplicationStepRequest;
import com.isep.ffa.entity.*;
import com.isep.ffa.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.isep.ffa.security.SecurityUtils;
import java.util.List;

/**
 * User Controller
 * Provides REST API endpoints for user operations
 * Base path: /ffaAPI/user
 */
@RestController
@RequestMapping("/ffaAPI/user")
@Tag(name = "User API", description = "User operations for FFA platform")
public class UserController {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private DocumentService documentService;

  @Autowired
  private PersonService personService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private AlertService alertService;

  @Autowired
  private CountryService countryService;

  @Autowired
  private EmbassyService embassyService;

  @Autowired
  private FileStorageService fileStorageService;

  // ==================== PROJECT MANAGEMENT ====================

  /**
   * Get available projects
   */
  @GetMapping("/projects")
  @Operation(summary = "Get available projects", description = "Retrieve paginated list of available projects (PUBLISHED status)")
  public BaseResponse<PagedResponse<Project>> getAvailableProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return projectService.getAvailableProjects(page, size);
  }

  /**
   * Get project details
   */
  @GetMapping("/projects/{id}")
  @Operation(summary = "Get project details", description = "Get detailed information about a project")
  public BaseResponse<Project> getProjectDetails(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    return projectService.getProjectWithDetails(id);
  }

  /**
   * Search projects
   */
  @GetMapping("/projects/search")
  @Operation(summary = "Search projects", description = "Search available projects by keyword")
  public BaseResponse<PagedResponse<Project>> searchProjects(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return projectService.searchByDescription(keyword, page, size);
  }

  // ==================== APPLICATION MANAGEMENT ====================

  /**
   * Get my applications
   */
  @GetMapping("/applications")
  @Operation(summary = "Get my applications", description = "Retrieve paginated list of my applications")
  public BaseResponse<PagedResponse<Application>> getMyApplications(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return applicationService.getApplicationsByUser(currentUserId, page + 1, size);
  }

  /**
   * Submit application to project (simple version - for backward compatibility)
   */
  @PostMapping("/projects/{projectId}/apply")
  @Operation(summary = "Apply to project", description = "Submit application to a project. " +
      "Required: projectId and motivation (a text explaining why you want to apply for this project). " +
      "The userId will be automatically set to the current logged-in user.", security = @SecurityRequirement(name = "bearer-jwt"))
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "Application submitted successfully"),
      @ApiResponse(responseCode = "401", description = "User not authenticated"),
      @ApiResponse(responseCode = "404", description = "Project not found"),
      @ApiResponse(responseCode = "400", description = "Invalid request or application already exists"),
      @ApiResponse(responseCode = "500", description = "Unexpected error")
  })
  public BaseResponse<Application> applyToProject(
      @Parameter(description = "Project ID") @PathVariable Long projectId,
      @Parameter(description = "Motivation text - Explain why you want to apply for this project and what you can contribute") @RequestParam String motivation) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Submit application using service method
    return applicationService.submitApplication(projectId, currentUserId, motivation);
  }

  /**
   * Save application step (multi-step process)
   */
  @PostMapping("/applications/steps")
  @Operation(summary = "Save application step", description = "Save a step in the multi-step application process")
  public BaseResponse<Application> saveApplicationStep(
      @RequestBody ApplicationStepRequest request) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    if (request.getStep() == null || request.getStep() < 1 || request.getStep() > 5) {
      return BaseResponse.error("Step number must be between 1 and 5", 400);
    }
    if (request.getProjectId() == null) {
      return BaseResponse.error("Project ID is required", 400);
    }

    // If saveDraft is true, just save as draft
    if (Boolean.TRUE.equals(request.getSaveDraft())) {
      Application application = new Application();
      application.setId(request.getApplicationId());
      application.setProjectId(request.getProjectId());
      application.setUserId(currentUserId);
      application.setStatus("DRAFT");
      application.setCurrentStep(request.getStep());

      // Set fields based on step
      if (request.getStep() == 1) {
        application.setTitle(request.getTitle());
        application.setDescription(request.getDescription());
        application.setStartDate(request.getStartDate());
        application.setEndDate(request.getEndDate());
        application.setLocationId(request.getLocationId());
      } else if (request.getStep() == 2) {
        application.setScope(request.getScope());
      } else if (request.getStep() == 3) {
        application.setBudget(request.getBudget());
      }

      return applicationService.saveDraft(application);
    }

    // Otherwise, save step normally
    return applicationService.saveApplicationStep(
        request.getApplicationId(),
        request.getProjectId(),
        currentUserId,
        request.getStep(),
        request);
  }

  /**
   * Get draft application
   */
  @GetMapping("/projects/{projectId}/applications/draft")
  @Operation(summary = "Get draft application", description = "Get draft application for current user and project")
  public BaseResponse<Application> getDraftApplication(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return applicationService.getDraftApplication(projectId, currentUserId);
  }

  /**
   * Submit application (final submission)
   */
  @PostMapping("/applications/{applicationId}/submit")
  @Operation(summary = "Submit application", description = "Submit application for final review (Step 5)")
  public BaseResponse<Application> submitApplicationFinal(
      @Parameter(description = "Application ID") @PathVariable Long applicationId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to submit this application", 403);
    }
    return applicationService.submitApplicationFinal(applicationId);
  }

  // ==================== DOCUMENT MANAGEMENT ====================

  /**
   * Get document types for a project
   */
  @GetMapping("/projects/{projectId}/document-types")
  @Operation(summary = "Get document types", description = "Get available document types for a project")
  public BaseResponse<List<DocumentType>> getDocumentTypes(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    return documentService.getDocumentTypesByProject(projectId);
  }

  /**
   * Get document requirements for a project
   */
  @GetMapping("/projects/{projectId}/document-requirements")
  @Operation(summary = "Get document requirements", description = "Get document requirements for a project")
  public BaseResponse<List<DocumentsNeedForProject>> getDocumentRequirements(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    return documentService.getDocumentRequirementsByProject(projectId);
  }

  /**
   * Upload document file for application
   */
  @PostMapping(value = "/applications/{applicationId}/documents", consumes = "multipart/form-data")
  @Operation(summary = "Upload document", description = "Upload a document file for an application")
  public BaseResponse<DocumentsSubmitted> uploadDocument(
      @Parameter(description = "Application ID") @PathVariable Long applicationId,
      @Parameter(description = "Document type ID") @RequestParam Long documentTypeId,
      @Parameter(description = "Document file") @RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to upload documents for this application", 403);
    }
    return documentService.uploadDocumentFile(applicationId, documentTypeId, file);
  }

  /**
   * Get documents for application
   */
  @GetMapping("/applications/{applicationId}/documents")
  @Operation(summary = "Get application documents", description = "Get all documents for an application")
  public BaseResponse<List<DocumentsSubmitted>> getApplicationDocuments(
      @Parameter(description = "Application ID") @PathVariable Long applicationId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to view documents for this application", 403);
    }
    return documentService.getDocumentsByApplication(applicationId);
  }

  /**
   * Delete document
   */
  @DeleteMapping("/documents/{documentId}")
  @Operation(summary = "Delete document", description = "Delete a document")
  public BaseResponse<Boolean> deleteDocument(
      @Parameter(description = "Document ID") @PathVariable Long documentId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Get document to verify ownership
    BaseResponse<DocumentsSubmitted> docResponse = documentService.getDocumentById(documentId);
    if (!docResponse.isSuccess() || docResponse.getData() == null) {
      return BaseResponse.error("Document not found with ID: " + documentId, 404);
    }
    DocumentsSubmitted document = docResponse.getData();

    // Verify application belongs to current user
    Application application = applicationService.getById(document.getApplicationId());
    if (application == null || !currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to delete this document", 403);
    }

    return documentService.deleteDocument(documentId);
  }

  /**
   * Validate required documents for application
   */
  @GetMapping("/applications/{applicationId}/documents/validate")
  @Operation(summary = "Validate required documents", description = "Check if all required documents are uploaded")
  public BaseResponse<Boolean> validateRequiredDocuments(
      @Parameter(description = "Application ID") @PathVariable Long applicationId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to validate documents for this application", 403);
    }
    return documentService.validateRequiredDocuments(applicationId, application.getProjectId());
  }

  /**
   * Download document file
   */
  @GetMapping("/documents/{documentId}/download")
  @Operation(summary = "Download document", description = "Download a document file")
  public org.springframework.http.ResponseEntity<byte[]> downloadDocument(
      @Parameter(description = "Document ID") @PathVariable Long documentId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return org.springframework.http.ResponseEntity.status(401).build();
    }
    // Get document
    BaseResponse<DocumentsSubmitted> docResponse = documentService.getDocumentById(documentId);
    if (!docResponse.isSuccess() || docResponse.getData() == null) {
      return org.springframework.http.ResponseEntity.notFound().build();
    }
    DocumentsSubmitted document = docResponse.getData();

    // Verify application belongs to current user
    Application application = applicationService.getById(document.getApplicationId());
    if (application == null || !currentUserId.equals(application.getUserId())) {
      return org.springframework.http.ResponseEntity.status(403).build();
    }

    // Get file
    BaseResponse<byte[]> fileResponse = fileStorageService.getFile(document.getPath());
    if (!fileResponse.isSuccess() || fileResponse.getData() == null) {
      return org.springframework.http.ResponseEntity.notFound().build();
    }

    // Determine content type
    String contentType = "application/octet-stream";
    String filename = document.getPath();
    if (filename != null) {
      if (filename.endsWith(".pdf")) {
        contentType = "application/pdf";
      } else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
        contentType = "image/jpeg";
      } else if (filename.endsWith(".png")) {
        contentType = "image/png";
      } else if (filename.endsWith(".doc")) {
        contentType = "application/msword";
      } else if (filename.endsWith(".docx")) {
        contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
      }
    }

    return org.springframework.http.ResponseEntity.ok()
        .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
        .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, contentType)
        .body(fileResponse.getData());
  }

  /**
   * Get application details
   */
  @GetMapping("/applications/{id}")
  @Operation(summary = "Get application details", description = "Get detailed information about my application")
  public BaseResponse<Application> getApplicationDetails(
      @Parameter(description = "Application ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to view this application", 403);
    }
    return applicationService.getApplicationWithDetails(id);
  }

  /**
   * Update application
   */
  @PutMapping("/applications/{id}")
  @Operation(summary = "Update application", description = "Update my application")
  public BaseResponse<Application> updateApplication(
      @Parameter(description = "Application ID") @PathVariable Long id,
      @RequestBody Application application) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application existing = applicationService.getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(existing.getUserId())) {
      return BaseResponse.error("You don't have permission to update this application", 403);
    }
    application.setId(id);
    application.setUserId(currentUserId);
    application.setProjectId(existing.getProjectId());
    return applicationService.updateApplication(application);
  }

  /**
   * Cancel application
   */
  @DeleteMapping("/applications/{id}")
  @Operation(summary = "Cancel application", description = "Cancel my application")
  public BaseResponse<Boolean> cancelApplication(
      @Parameter(description = "Application ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to current user
    Application application = applicationService.getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(application.getUserId())) {
      return BaseResponse.error("You don't have permission to cancel this application", 403);
    }
    return applicationService.deleteApplication(id);
  }

  /**
   * Check if I applied to project
   */
  @GetMapping("/projects/{projectId}/applied")
  @Operation(summary = "Check if applied", description = "Check if I already applied to a project")
  public BaseResponse<Boolean> hasAppliedToProject(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return applicationService.hasUserAppliedToProject(projectId, currentUserId);
  }

  // ==================== MESSAGE MANAGEMENT ====================

  /**
   * Get my messages
   */
  @GetMapping("/messages")
  @Operation(summary = "Get my messages", description = "Retrieve paginated list of my messages")
  public BaseResponse<PagedResponse<Message>> getMyMessages(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return messageService.getMessagesByReceiver(currentUserId, page + 1, size);
  }

  /**
   * Send message to user
   */
  @PostMapping("/messages")
  @Operation(summary = "Send message", description = "Send message to a user")
  public BaseResponse<Message> sendMessage(
      @Parameter(description = "Receiver ID") @RequestParam Long receiverId,
      @Parameter(description = "Message content") @RequestParam String content) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    if (currentUserId.equals(receiverId)) {
      return BaseResponse.error("Cannot send message to yourself", 400);
    }
    return messageService.sendMessage(currentUserId, receiverId, content);
  }

  /**
   * Reply to message
   */
  @PostMapping("/messages/{messageId}/reply")
  @Operation(summary = "Reply to message", description = "Reply to a specific message")
  public BaseResponse<Message> replyToMessage(
      @Parameter(description = "Original message ID") @PathVariable Long messageId,
      @Parameter(description = "Reply content") @RequestParam String content) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return messageService.replyToMessage(messageId, currentUserId, content);
  }

  /**
   * Mark message as read
   */
  @PutMapping("/messages/{messageId}/read")
  @Operation(summary = "Mark message as read", description = "Mark a message as read")
  public BaseResponse<Boolean> markMessageAsRead(
      @Parameter(description = "Message ID") @PathVariable Long messageId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify user is the receiver
    Message message = messageService.getById(messageId);
    if (message == null || Boolean.TRUE.equals(message.getIsDeleted())) {
      return BaseResponse.error("Message not found", 404);
    }
    if (!currentUserId.equals(message.getReceiverId())) {
      return BaseResponse.error("You don't have permission to mark this message as read", 403);
    }
    return messageService.markAsRead(messageId);
  }

  /**
   * Mark all messages as read
   */
  @PutMapping("/messages/read-all")
  @Operation(summary = "Mark all messages as read", description = "Mark all my messages as read")
  public BaseResponse<Boolean> markAllMessagesAsRead() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return messageService.markAllAsRead(currentUserId);
  }

  /**
   * Get conversation with user
   */
  @GetMapping("/messages/conversation/{userId}")
  @Operation(summary = "Get conversation", description = "Get conversation with a specific user")
  public BaseResponse<List<Message>> getConversation(
      @Parameter(description = "User ID") @PathVariable Long userId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return messageService.findConversation(currentUserId, userId);
  }

  /**
   * Get unread messages count
   */
  @GetMapping("/messages/unread-count")
  @Operation(summary = "Get unread messages count", description = "Get count of unread messages")
  public BaseResponse<Integer> getUnreadMessagesCount() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return messageService.countUnreadMessages(currentUserId);
  }

  // ==================== ALERT MANAGEMENT ====================

  /**
   * Get my alerts
   */
  @GetMapping("/alerts")
  @Operation(summary = "Get my alerts", description = "Retrieve paginated list of my alerts")
  public BaseResponse<PagedResponse<Alert>> getMyAlerts(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return alertService.getAlertsByReceiver(currentUserId, page + 1, size);
  }

  /**
   * Mark alert as read
   */
  @PutMapping("/alerts/{alertId}/read")
  @Operation(summary = "Mark alert as read", description = "Mark an alert as read")
  public BaseResponse<Boolean> markAlertAsRead(
      @Parameter(description = "Alert ID") @PathVariable Long alertId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify user is the receiver
    Alert alert = alertService.getById(alertId);
    if (alert == null || Boolean.TRUE.equals(alert.getIsDeleted())) {
      return BaseResponse.error("Alert not found", 404);
    }
    if (!currentUserId.equals(alert.getReceiverId())) {
      return BaseResponse.error("You don't have permission to mark this alert as read", 403);
    }
    return alertService.markAsRead(alertId);
  }

  /**
   * Mark all alerts as read
   */
  @PutMapping("/alerts/read-all")
  @Operation(summary = "Mark all alerts as read", description = "Mark all my alerts as read")
  public BaseResponse<Boolean> markAllAlertsAsRead() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return alertService.markAllAsRead(currentUserId);
  }

  /**
   * Get unread alerts count
   */
  @GetMapping("/alerts/unread-count")
  @Operation(summary = "Get unread alerts count", description = "Get count of unread alerts")
  public BaseResponse<Integer> getUnreadAlertsCount() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return alertService.countUnreadAlerts(currentUserId);
  }

  /**
   * Get recent alerts
   */
  @GetMapping("/alerts/recent")
  @Operation(summary = "Get recent alerts", description = "Get recent alerts")
  public BaseResponse<List<Alert>> getRecentAlerts(
      @Parameter(description = "Number of alerts to return") @RequestParam(defaultValue = "5") int limit) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    return alertService.getRecentAlerts(currentUserId, limit);
  }

  // ==================== PROFILE MANAGEMENT ====================

  /**
   * Get my profile
   */
  @GetMapping("/profile")
  @Operation(summary = "Get my profile", description = "Get my profile information")
  public BaseResponse<Person> getMyProfile() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    Person person = personService.getById(currentUserId);
    if (person == null) {
      return BaseResponse.error("User not found", 404);
    }
    return BaseResponse.success("Profile retrieved successfully", person);
  }

  /**
   * Update my profile
   */
  @PutMapping("/profile")
  @Operation(summary = "Update my profile", description = "Update my profile information")
  public BaseResponse<Person> updateMyProfile(@RequestBody Person person) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    person.setId(currentUserId);
    // Prevent changing password through profile update
    Person existing = personService.getById(currentUserId);
    if (existing != null) {
      person.setPassword(existing.getPassword());
    }
    return personService.updatePerson(person);
  }

  // ==================== REFERENCE DATA ====================

  /**
   * Get all countries
   */
  @GetMapping("/countries")
  @Operation(summary = "Get all countries", description = "Retrieve list of all countries")
  public BaseResponse<List<Country>> getAllCountries() {
    return countryService.getAllCountries();
  }

  /**
   * Get countries with embassies
   */
  @GetMapping("/countries/with-embassies")
  @Operation(summary = "Get countries with embassies", description = "Retrieve countries that have embassies")
  public BaseResponse<List<Country>> getCountriesWithEmbassies() {
    return countryService.getCountriesWithEmbassies();
  }

  /**
   * Get embassies by country
   */
  @GetMapping("/countries/{countryId}/embassies")
  @Operation(summary = "Get embassies by country", description = "Retrieve embassies located in a specific country")
  public BaseResponse<List<Embassy>> getEmbassiesByCountry(
      @Parameter(description = "Country ID") @PathVariable Long countryId) {
    return embassyService.findByEmbassyInCountryId(countryId);
  }
}
