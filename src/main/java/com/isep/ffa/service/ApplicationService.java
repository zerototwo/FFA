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
}
