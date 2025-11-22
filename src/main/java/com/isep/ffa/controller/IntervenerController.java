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

    // Pending approvals: projects that need approval (for now, we'll use projects without winner as pending)
    // This is a simplified logic - you may need to adjust based on actual business requirements
    Long pendingApprovalsCount = 0L; // TODO: Implement actual pending approvals logic

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
  @Operation(summary = "Get my projects", description = "Retrieve paginated list of my projects")
  public BaseResponse<PagedResponse<Project>> getMyProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Create new project
   */
  @PostMapping("/projects")
  @Operation(summary = "Create project", description = "Create a new project")
  public BaseResponse<Project> createProject(@RequestBody Project project) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Update my project
   */
  @PutMapping("/projects/{id}")
  @Operation(summary = "Update project", description = "Update my project information")
  public BaseResponse<Project> updateProject(
      @Parameter(description = "Project ID") @PathVariable Long id,
      @RequestBody Project project) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Delete my project
   */
  @DeleteMapping("/projects/{id}")
  @Operation(summary = "Delete project", description = "Delete my project")
  public BaseResponse<Boolean> deleteProject(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Get project details
   */
  @GetMapping("/projects/{id}")
  @Operation(summary = "Get project details", description = "Get detailed information about my project")
  public BaseResponse<Project> getProjectDetails(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    // TODO: Implement business logic
    return null;
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
    // TODO: Implement business logic
    return null;
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
    // TODO: Implement business logic
    return null;
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
    // TODO: Implement business logic
    return null;
  }

  /**
   * Get application details
   */
  @GetMapping("/applications/{id}")
  @Operation(summary = "Get application details", description = "Get detailed information about an application")
  public BaseResponse<Application> getApplicationDetails(
      @Parameter(description = "Application ID") @PathVariable Long id) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Define project winner
   */
  @PostMapping("/projects/{projectId}/winner")
  @Operation(summary = "Define project winner", description = "Define winner for my project")
  public BaseResponse<Boolean> defineProjectWinner(
      @Parameter(description = "Project ID") @PathVariable Long projectId,
      @Parameter(description = "Winner user ID") @RequestParam Long winnerUserId) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Get project statistics
   */
  @GetMapping("/projects/{projectId}/statistics")
  @Operation(summary = "Get project statistics", description = "Get statistics for my project")
  public BaseResponse<Object> getProjectStatistics(
      @Parameter(description = "Project ID") @PathVariable Long projectId) {
    // TODO: Implement business logic
    return null;
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
    // TODO: Implement business logic
    return null;
  }

  /**
   * Send message to user
   */
  @PostMapping("/messages")
  @Operation(summary = "Send message", description = "Send message to a user")
  public BaseResponse<Message> sendMessage(
      @Parameter(description = "Receiver ID") @RequestParam Long receiverId,
      @Parameter(description = "Message content") @RequestParam String content) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Reply to message
   */
  @PostMapping("/messages/{messageId}/reply")
  @Operation(summary = "Reply to message", description = "Reply to a specific message")
  public BaseResponse<Message> replyToMessage(
      @Parameter(description = "Original message ID") @PathVariable Long messageId,
      @Parameter(description = "Reply content") @RequestParam String content) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Mark message as read
   */
  @PutMapping("/messages/{messageId}/read")
  @Operation(summary = "Mark message as read", description = "Mark a message as read")
  public BaseResponse<Boolean> markMessageAsRead(
      @Parameter(description = "Message ID") @PathVariable Long messageId) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Get conversation with user
   */
  @GetMapping("/messages/conversation/{userId}")
  @Operation(summary = "Get conversation", description = "Get conversation with a specific user")
  public BaseResponse<List<Message>> getConversation(
      @Parameter(description = "User ID") @PathVariable Long userId) {
    // TODO: Implement business logic
    return null;
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
    // TODO: Implement business logic
    return null;
  }

  /**
   * Mark alert as read
   */
  @PutMapping("/alerts/{alertId}/read")
  @Operation(summary = "Mark alert as read", description = "Mark an alert as read")
  public BaseResponse<Boolean> markAlertAsRead(
      @Parameter(description = "Alert ID") @PathVariable Long alertId) {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Get unread alerts count
   */
  @GetMapping("/alerts/unread-count")
  @Operation(summary = "Get unread alerts count", description = "Get count of unread alerts")
  public BaseResponse<Integer> getUnreadAlertsCount() {
    // TODO: Implement business logic
    return null;
  }

  // ==================== PROFILE MANAGEMENT ====================

  /**
   * Get my profile
   */
  @GetMapping("/profile")
  @Operation(summary = "Get my profile", description = "Get my profile information")
  public BaseResponse<Person> getMyProfile() {
    // TODO: Implement business logic
    return null;
  }

  /**
   * Update my profile
   */
  @PutMapping("/profile")
  @Operation(summary = "Update my profile", description = "Update my profile information")
  public BaseResponse<Person> updateMyProfile(@RequestBody Person person) {
    // TODO: Implement business logic
    return null;
  }
}
