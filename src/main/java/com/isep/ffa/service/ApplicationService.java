package com.isep.ffa.service;

import com.isep.ffa.entity.Application;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Application Service Interface
 * Provides business logic for application management
 */
public interface ApplicationService extends BaseService<Application> {

  /**
   * Find applications by project ID
   * 
   * @param projectId project ID
   * @return list of applications
   */
  BaseResponse<List<Application>> findByProjectId(Long projectId);

  /**
   * Find applications by user ID
   * 
   * @param userId user ID
   * @return list of applications
   */
  BaseResponse<List<Application>> findByUserId(Long userId);

  /**
   * Find applications by date range
   * 
   * @param startDate start date
   * @param endDate   end date
   * @return list of applications
   */
  BaseResponse<List<Application>> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);

  /**
   * Search applications by motivation
   * 
   * @param keyword search keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<Application>> searchByMotivation(String keyword, int page, int size);

  /**
   * Get paginated applications by project
   * 
   * @param projectId project ID
   * @param page      page number
   * @param size      page size
   * @return paginated applications
   */
  BaseResponse<PagedResponse<Application>> getApplicationsByProject(Long projectId, int page, int size);

  /**
   * Get paginated applications by user
   * 
   * @param userId user ID
   * @param page   page number
   * @param size   page size
   * @return paginated applications
   */
  BaseResponse<PagedResponse<Application>> getApplicationsByUser(Long userId, int page, int size);

  /**
   * Create new application
   * 
   * @param application application information
   * @return created application
   */
  BaseResponse<Application> createApplication(Application application);

  /**
   * Update application information
   * 
   * @param application application information
   * @return updated application
   */
  BaseResponse<Application> updateApplication(Application application);

  /**
   * Delete application by ID
   * 
   * @param id application ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteApplication(Long id);

  /**
   * Submit application to project
   * 
   * @param projectId  project ID
   * @param userId     user ID
   * @param motivation motivation letter
   * @return created application
   */
  BaseResponse<Application> submitApplication(Long projectId, Long userId, String motivation);

  /**
   * Get application with full details
   * 
   * @param id application ID
   * @return application with project and user information
   */
  BaseResponse<Application> getApplicationWithDetails(Long id);

  /**
   * Check if user already applied to project
   * 
   * @param projectId project ID
   * @param userId    user ID
   * @return application exists or not
   */
  BaseResponse<Boolean> hasUserAppliedToProject(Long projectId, Long userId);

  /**
   * Count applications for projects by intervener ID
   * 
   * @param intervenerId intervener ID
   * @return application count
   */
  Long countApplicationsForIntervenerProjects(Long intervenerId);

  /**
   * Count applications created this week for projects by intervener ID
   * 
   * @param intervenerId intervener ID
   * @return application count this week
   */
  Long countApplicationsForIntervenerProjectsThisWeek(Long intervenerId);

  /**
   * Get recent applications for intervener projects
   * 
   * @param intervenerId intervener ID
   * @param limit        limit number of results
   * @return list of recent applications
   */
  BaseResponse<List<Application>> getRecentApplicationsForIntervener(Long intervenerId, Integer limit);

  /**
   * Save application step (multi-step process)
   * 
   * @param applicationId application ID (null for new)
   * @param projectId     project ID
   * @param userId        user ID
   * @param step          step number (1-5)
   * @param data          step data
   * @return saved application
   */
  BaseResponse<Application> saveApplicationStep(Long applicationId, Long projectId, Long userId, Integer step, Object data);

  /**
   * Save application as draft
   * 
   * @param application application data
   * @return saved draft application
   */
  BaseResponse<Application> saveDraft(Application application);

  /**
   * Get draft application for user and project
   * 
   * @param projectId project ID
   * @param userId    user ID
   * @return draft application if exists
   */
  BaseResponse<Application> getDraftApplication(Long projectId, Long userId);

  /**
   * Submit application (final submission)
   * 
   * @param applicationId application ID
   * @return submitted application
   */
  BaseResponse<Application> submitApplicationFinal(Long applicationId);
}
