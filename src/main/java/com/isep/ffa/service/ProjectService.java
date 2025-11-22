package com.isep.ffa.service;

import com.isep.ffa.entity.Project;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Project Service Interface
 * Provides business logic for project management
 */
public interface ProjectService extends BaseService<Project> {

  /**
   * Find project by name
   * 
   * @param name project name
   * @return project information
   */
  BaseResponse<Project> findByName(String name);

  /**
   * Find projects by intervener ID
   * 
   * @param intervenerId intervener ID
   * @return list of projects
   */
  BaseResponse<List<Project>> findByIntervenerId(Long intervenerId);

  /**
   * Find projects by winner user ID
   * 
   * @param winnerUserId winner user ID
   * @return list of projects
   */
  BaseResponse<List<Project>> findByWinnerUserId(Long winnerUserId);

  /**
   * Find projects by submission date range
   * 
   * @param startDate start date
   * @param endDate   end date
   * @return list of projects
   */
  BaseResponse<List<Project>> findBySubmissionDateRange(LocalDate startDate, LocalDate endDate);

  /**
   * Search projects by description
   * 
   * @param keyword search keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<Project>> searchByDescription(String keyword, int page, int size);

  /**
   * Get paginated projects by intervener
   * 
   * @param intervenerId intervener ID
   * @param page         page number
   * @param size         page size
   * @return paginated projects
   */
  BaseResponse<PagedResponse<Project>> getProjectsByIntervener(Long intervenerId, int page, int size);

  /**
   * Get all available projects for users
   * 
   * @param page page number
   * @param size page size
   * @return paginated available projects
   */
  BaseResponse<PagedResponse<Project>> getAvailableProjects(int page, int size);

  /**
   * Create new project
   * 
   * @param project project information
   * @return created project
   */
  BaseResponse<Project> createProject(Project project);

  /**
   * Update project information
   * 
   * @param project project information
   * @return updated project
   */
  BaseResponse<Project> updateProject(Project project);

  /**
   * Delete project by ID
   * 
   * @param id project ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteProject(Long id);

  /**
   * Define project winner
   * 
   * @param projectId    project ID
   * @param winnerUserId winner user ID
   * @return operation result
   */
  BaseResponse<Boolean> defineWinner(Long projectId, Long winnerUserId);

  /**
   * Get project with full details
   * 
   * @param id project ID
   * @return project with intervener and applications information
   */
  BaseResponse<Project> getProjectWithDetails(Long id);

  /**
   * Get project statistics
   * 
   * @param projectId project ID
   * @return project statistics
   */
  BaseResponse<Object> getProjectStatistics(Long projectId);

  /**
   * Count projects by intervener ID
   * 
   * @param intervenerId intervener ID
   * @return project count
   */
  Long countByIntervenerId(Long intervenerId);

  /**
   * Count projects created this month by intervener ID
   * 
   * @param intervenerId intervener ID
   * @return project count this month
   */
  Long countByIntervenerIdThisMonth(Long intervenerId);
}
