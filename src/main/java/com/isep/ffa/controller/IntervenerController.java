package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.dto.response.DashboardResponse;
import com.isep.ffa.entity.*;
import com.isep.ffa.security.SecurityUtils;
import com.isep.ffa.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Intervener Controller
 * Provides REST API endpoints for intervener operations
 * Base path: /ffaAPI/intervener
 */
@RestController
@RequestMapping("/ffaAPI/intervener")
@Tag(name = "Intervener API", description = "Intervener operations for FFA platform")
public class IntervenerController {

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private PersonService personService;

  @Autowired
  private MessageService messageService;

  @Autowired
  private AlertService alertService;

  @Autowired
  private CityService cityService;

  // ==================== DASHBOARD ====================

  /**
   * Get dashboard statistics and recent activities
   */
  @GetMapping("/dashboard")
  @Operation(summary = "Get dashboard", description = "Retrieve dashboard statistics and recent activities for intervener")
  public BaseResponse<DashboardResponse> getDashboard() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }

    // Get current user
    Person user = personService.getById(currentUserId);
    if (user == null) {
      return BaseResponse.error("User not found", 404);
    }

    // Get statistics
    Long myProjectsCount = projectService.countByIntervenerId(currentUserId);
    Long myProjectsIncreaseThisMonth = projectService.countByIntervenerIdThisMonth(currentUserId);
    Long applicationsInReviewCount = applicationService.countApplicationsForIntervenerProjects(currentUserId);
    Long applicationsInReviewIncreaseThisWeek = applicationService.countApplicationsForIntervenerProjectsThisWeek(currentUserId);

    // Pending approvals: projects with PENDING_APPROVAL status
    Long pendingApprovalsCount = projectService.countPendingApprovalsByIntervenerId(currentUserId);

    DashboardResponse.Statistics statistics = DashboardResponse.Statistics.builder()
        .myProjectsCount(myProjectsCount)
        .myProjectsIncreaseThisMonth(myProjectsIncreaseThisMonth)
        .pendingApprovalsCount(pendingApprovalsCount)
        .applicationsInReviewCount(applicationsInReviewCount)
        .applicationsInReviewIncreaseThisWeek(applicationsInReviewIncreaseThisWeek)
        .build();

    // Get recent activities
    List<DashboardResponse.ActivityItem> recentActivities = new ArrayList<>();
    
    // Get recent projects
    BaseResponse<List<Project>> projectsResponse = projectService.findByIntervenerId(currentUserId);
    if (projectsResponse.isSuccess() && projectsResponse.getData() != null) {
      List<Project> projects = projectsResponse.getData();
      recentActivities.addAll(projects.stream()
          .limit(3)
          .map(project -> DashboardResponse.ActivityItem.builder()
              .title(project.getName())
              .date(project.getCreationDate() != null 
                  ? project.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE) 
                  : "")
              .status(project.getWinnerUserId() != null ? "Published" : "Pending Approval")
              .type("PROJECT")
              .entityId(project.getId())
              .build())
          .collect(Collectors.toList()));
    }

    // Get recent applications
    BaseResponse<List<Application>> applicationsResponse = applicationService.getRecentApplicationsForIntervener(currentUserId, 3);
    if (applicationsResponse.isSuccess() && applicationsResponse.getData() != null) {
      List<Application> applications = applicationsResponse.getData();
      recentActivities.addAll(applications.stream()
          .map(app -> {
            // Get user information from userId
            String userName = "User";
            if (app.getUserId() != null) {
              Person applicant = personService.getById(app.getUserId());
              if (applicant != null) {
                userName = applicant.getFirstName() + " " + applicant.getLastName();
              }
            }
            return DashboardResponse.ActivityItem.builder()
                .title("Application from " + userName)
                .date(app.getDateApplication() != null 
                    ? app.getDateApplication().format(DateTimeFormatter.ISO_LOCAL_DATE) 
                    : "")
                .status("Under Review")
                .type("APPLICATION")
                .entityId(app.getId())
                .build();
          })
          .collect(Collectors.toList()));
    }

    // Sort by date (most recent first) and limit to 5
    recentActivities.sort((a, b) -> b.getDate().compareTo(a.getDate()));
    if (recentActivities.size() > 5) {
      recentActivities = recentActivities.subList(0, 5);
    }

    DashboardResponse dashboard = DashboardResponse.builder()
        .user(user)
        .organizationName(user.getOrganizationName())
        .statistics(statistics)
        .recentActivities(recentActivities)
        .build();

    return BaseResponse.success("Dashboard data retrieved successfully", dashboard);
  }

  // ==================== PROJECT MANAGEMENT ====================

  /**
   * Get my projects
   */
  @GetMapping("/projects")
  @Operation(summary = "Get my projects", description = "Retrieve paginated list of my projects with optional status filter")
  public BaseResponse<PagedResponse<Project>> getMyProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "Filter by status (DRAFT, PENDING_APPROVAL, PUBLISHED)") @RequestParam(required = false) String status) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // If status filter is provided, use filtered query
    if (status != null && !status.trim().isEmpty()) {
      return projectService.getProjectsByIntervenerAndStatus(currentUserId, status.trim().toUpperCase(), page + 1, size);
    }
    // Otherwise, get all projects
    BaseResponse<PagedResponse<Project>> response = projectService.getProjectsByIntervener(currentUserId, page + 1, size);
    // Enhance response with application counts and location info
    if (response.isSuccess() && response.getData() != null) {
      PagedResponse<Project> pagedData = response.getData();
      for (Project project : pagedData.getContent()) {
        // Set default status if null
        if (project.getStatus() == null) {
          project.setStatus("DRAFT");
        }
        // Load location if available
        if (project.getLocationId() != null && project.getLocation() == null) {
          City city = cityService.getById(project.getLocationId());
          if (city != null) {
            project.setLocation(city);
          }
        }
      }
    }
    return response;
  }

  /**
   * Create new project
   */
  @PostMapping("/projects")
  @Operation(summary = "Create project", description = "Create a new project. Status will default to DRAFT if not provided.")
  public BaseResponse<Project> createProject(@RequestBody Project project) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Set the current user as the intervener
    project.setIntervenerId(currentUserId);
    // Set default status to DRAFT if not provided
    if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {
      project.setStatus("DRAFT");
    }
    return projectService.createProject(project);
  }

  /**
   * Update my project
   */
  @PutMapping("/projects/{id}")
  @Operation(summary = "Update project", description = "Update my project information. Only DRAFT projects can be updated.")
  public BaseResponse<Project> updateProject(
      @Parameter(description = "Project ID") @PathVariable Long id,
      @RequestBody Project project) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project existing = projectService.getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(existing.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to update this project", 403);
    }
    // Only allow editing DRAFT projects
    String currentStatus = existing.getStatus();
    if (currentStatus == null) {
      currentStatus = "DRAFT";
    }
    if (!"DRAFT".equals(currentStatus)) {
      return BaseResponse.error("Only DRAFT projects can be updated. Current status: " + currentStatus, 400);
    }
    // Set ID and ensure intervener ID is not changed
    project.setId(id);
    project.setIntervenerId(currentUserId);
    // Preserve status if not provided
    if (project.getStatus() == null || project.getStatus().trim().isEmpty()) {
      project.setStatus("DRAFT");
    }
    return projectService.updateProject(project);
  }

  /**
   * Delete my project
   */
  @DeleteMapping("/projects/{id}")
  @Operation(summary = "Delete project", description = "Delete my project")
  public BaseResponse<Boolean> deleteProject(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project existing = projectService.getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(existing.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to delete this project", 403);
    }
    return projectService.deleteProject(id);
  }

  /**
   * Get project details
   */
  @GetMapping("/projects/{id}")
  @Operation(summary = "Get project details", description = "Get detailed information about my project including application count")
  public BaseResponse<Project> getProjectDetails(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project project = projectService.getById(id);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to view this project", 403);
    }
    BaseResponse<Project> response = projectService.getProjectWithDetails(id);
    // Enhance with application count and ensure status is set
    if (response.isSuccess() && response.getData() != null) {
      Project projectDetails = response.getData();
      if (projectDetails.getStatus() == null) {
        projectDetails.setStatus("DRAFT");
      }
    }
    return response;
  }

  /**
   * Search my projects
   */
  @GetMapping("/projects/search")
  @Operation(summary = "Search my projects", description = "Search my projects by keyword")
  public BaseResponse<PagedResponse<Project>> searchMyProjects(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Search projects and filter by current user
    BaseResponse<PagedResponse<Project>> searchResult = projectService.searchByDescription(keyword, page + 1, size);
    if (searchResult.isSuccess() && searchResult.getData() != null) {
      // Filter to only include projects owned by current user
      PagedResponse<Project> pagedData = searchResult.getData();
      List<Project> filteredProjects = pagedData.getContent().stream()
          .filter(p -> currentUserId.equals(p.getIntervenerId()))
          .collect(Collectors.toList());
      
      int totalPages = filteredProjects.isEmpty() ? 0 : (int) Math.ceil((double) filteredProjects.size() / size);
      PagedResponse<Project> filteredPagedData = PagedResponse.of(
          filteredProjects,
          page,
          size,
          filteredProjects.size(),
          totalPages);
      
      return BaseResponse.success("Projects retrieved successfully", filteredPagedData);
    }
    return searchResult;
  }

  // ==================== APPLICATION MANAGEMENT ====================

  /**
   * Get applications for my projects
   */
  @GetMapping("/applications")
  @Operation(summary = "Get applications for my projects", description = "Retrieve applications for all my projects")
  public BaseResponse<PagedResponse<Application>> getApplicationsForMyProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Get all applications for projects owned by current user
    // For now, we'll get a larger limit and manually paginate
    // In production, you might want to add a proper paginated query to the mapper
    BaseResponse<List<Application>> allAppsResponse = applicationService.getRecentApplicationsForIntervener(currentUserId, 1000);
    if (!allAppsResponse.isSuccess() || allAppsResponse.getData() == null) {
      return BaseResponse.error("Failed to retrieve applications", 500);
    }
    
    List<Application> allApps = allAppsResponse.getData();
    int start = page * size;
    int end = Math.min(start + size, allApps.size());
    List<Application> pagedApps = start < allApps.size() ? allApps.subList(start, end) : List.of();
    
    int totalPages = (int) Math.ceil((double) allApps.size() / size);
    PagedResponse<Application> pagedResponse = PagedResponse.of(
        pagedApps,
        page,
        size,
        allApps.size(),
        totalPages);
    
    return BaseResponse.success("Applications retrieved successfully", pagedResponse);
  }

  /**
   * Get applications for specific project
   */
  @GetMapping("/projects/{projectId}/applications")
  @Operation(summary = "Get applications for project", description = "Retrieve applications for a specific project")
  public BaseResponse<PagedResponse<Application>> getApplicationsForProject(
      @Parameter(description = "Project ID") @PathVariable Long projectId,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project project = projectService.getById(projectId);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + projectId, 404);
    }
    if (!currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to view applications for this project", 403);
    }
    return applicationService.getApplicationsByProject(projectId, page + 1, size);
  }

  /**
   * Get application details
   */
  @GetMapping("/applications/{id}")
  @Operation(summary = "Get application details", description = "Get detailed information about an application")
  public BaseResponse<Application> getApplicationDetails(
      @Parameter(description = "Application ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to a project owned by current user
    Application application = applicationService.getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    Project project = projectService.getById(application.getProjectId());
    if (project == null || !currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to view this application", 403);
    }
    return applicationService.getApplicationWithDetails(id);
  }

  /**
   * Approve application
   */
  @PutMapping("/applications/{id}/approve")
  @Operation(summary = "Approve application", description = "Approve an application for my project")
  public BaseResponse<Application> approveApplication(
      @Parameter(description = "Application ID") @PathVariable Long id) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to a project owned by current user
    Application application = applicationService.getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    Project project = projectService.getById(application.getProjectId());
    if (project == null || !currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to approve this application", 403);
    }
    // Update application status to APPROVED
    application.setStatus("APPROVED");
    BaseResponse<Application> updateResponse = applicationService.updateApplication(application);
    if (updateResponse.isSuccess()) {
      // Send alert to applicant
      alertService.sendAlert(application.getUserId(), 
          "Your application for project \"" + project.getName() + "\" has been approved!",
          "APPLICATION_APPROVED");
    }
    return updateResponse;
  }

  /**
   * Reject application
   */
  @PutMapping("/applications/{id}/reject")
  @Operation(summary = "Reject application", description = "Reject an application for my project")
  public BaseResponse<Application> rejectApplication(
      @Parameter(description = "Application ID") @PathVariable Long id,
      @Parameter(description = "Rejection reason (optional)") @RequestParam(required = false) String reason) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify application belongs to a project owned by current user
    Application application = applicationService.getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    Project project = projectService.getById(application.getProjectId());
    if (project == null || !currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to reject this application", 403);
    }
    // Update application status to REJECTED
    application.setStatus("REJECTED");
    BaseResponse<Application> updateResponse = applicationService.updateApplication(application);
    if (updateResponse.isSuccess()) {
      // Send alert to applicant
      String message = "Your application for project \"" + project.getName() + "\" has been rejected.";
      if (reason != null && !reason.trim().isEmpty()) {
        message += " Reason: " + reason;
      }
      alertService.sendAlert(application.getUserId(), message, "APPLICATION_REJECTED");
    }
    return updateResponse;
  }

  /**
   * Define project winner
   */
  @PostMapping("/projects/{projectId}/winner")
  @Operation(summary = "Define project winner", description = "Define winner for my project")
  public BaseResponse<Boolean> defineProjectWinner(
      @Parameter(description = "Project ID") @PathVariable Long projectId,
      @Parameter(description = "Winner user ID") @RequestParam Long winnerUserId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project project = projectService.getById(projectId);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + projectId, 404);
    }
    if (!currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to define winner for this project", 403);
    }
    // Verify winner has applied to the project
    BaseResponse<Boolean> hasApplied = applicationService.hasUserAppliedToProject(projectId, winnerUserId);
    if (!hasApplied.isSuccess() || !Boolean.TRUE.equals(hasApplied.getData())) {
      return BaseResponse.error("The selected user has not applied to this project", 400);
    }
    return projectService.defineWinner(projectId, winnerUserId);
  }

  /**
   * Get project statistics
   */
  @GetMapping("/projects/{projectId}/statistics")
  @Operation(summary = "Get project statistics", description = "Get statistics for my project")
  public BaseResponse<Object> getProjectStatistics(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project project = projectService.getById(projectId);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + projectId, 404);
    }
    if (!currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to view statistics for this project", 403);
    }
    return projectService.getProjectStatistics(projectId);
  }

  /**
   * Change project status
   */
  @PutMapping("/projects/{id}/status")
  @Operation(summary = "Change project status", description = "Change project status (DRAFT -> PENDING_APPROVAL -> PUBLISHED)")
  public BaseResponse<Boolean> changeProjectStatus(
      @Parameter(description = "Project ID") @PathVariable Long id,
      @Parameter(description = "New status (DRAFT, PENDING_APPROVAL, PUBLISHED)") @RequestParam String status) {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return BaseResponse.error("User not authenticated", 401);
    }
    // Verify project belongs to current user
    Project project = projectService.getById(id);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    if (!currentUserId.equals(project.getIntervenerId())) {
      return BaseResponse.error("You don't have permission to change status for this project", 403);
    }
    return projectService.changeProjectStatus(id, status);
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
    // Get messages where current user is the receiver
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
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
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
    // Verify user is part of the conversation
    Message originalMessage = messageService.getById(messageId);
    if (originalMessage == null || Boolean.TRUE.equals(originalMessage.getIsDeleted())) {
      return BaseResponse.error("Original message not found", 404);
    }
    if (!currentUserId.equals(originalMessage.getSenderId()) && 
        !currentUserId.equals(originalMessage.getReceiverId())) {
      return BaseResponse.error("You don't have permission to reply to this message", 403);
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
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    return messageService.findConversation(currentUserId, userId);
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
    // Ensure user can only update their own profile
    person.setId(currentUserId);
    // Don't allow changing login, email, or password through this endpoint
    Person existing = personService.getById(currentUserId);
    if (existing == null) {
      return BaseResponse.error("User not found", 404);
    }
    person.setLogin(existing.getLogin());
    person.setEmail(existing.getEmail());
    person.setPassword(existing.getPassword());
    person.setRoleId(existing.getRoleId());
    
    boolean updated = personService.updateById(person);
    if (!updated) {
      return BaseResponse.error("Failed to update profile", 500);
    }
    Person updatedPerson = personService.getById(currentUserId);
    return BaseResponse.success("Profile updated successfully", updatedPerson);
  }
}
