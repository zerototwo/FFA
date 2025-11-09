package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.Project;
import com.isep.ffa.mapper.ProjectMapper;
import com.isep.ffa.service.ProjectService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * Project Service Implementation
 * Implements business logic for project management
 */
@Service
public class ProjectServiceImpl extends BaseServiceImpl<ProjectMapper, Project> implements ProjectService {

  private final ProjectMapper projectMapper;

  public ProjectServiceImpl(ProjectMapper projectMapper) {
    this.projectMapper = projectMapper;
  }

  private int normalizePage(int page) {
    return Math.max(page, 0);
  }

  private int normalizeSize(int size) {
    return size <= 0 ? 10 : size;
  }

  private PagedResponse<Project> toPagedResponse(Page<Project> pageResult) {
    int zeroBasedPage = (int) pageResult.getCurrent() - 1;
    zeroBasedPage = Math.max(zeroBasedPage, 0);
    return PagedResponse.of(
        pageResult.getRecords(),
        zeroBasedPage,
        (int) pageResult.getSize(),
        pageResult.getTotal(),
        (int) pageResult.getPages());
  }

  @Override
  public BaseResponse<Project> findByName(String name) {
    if (StringUtils.isBlank(name)) {
      return BaseResponse.error("Project name must not be blank", 400);
    }
    Project project = projectMapper.findByName(name.trim());
    if (project == null) {
      return BaseResponse.error("Project not found with name: " + name, 404);
    }
    return BaseResponse.success("Project found", project);
  }

  @Override
  public BaseResponse<List<Project>> findByIntervenerId(Long intervenerId) {
    if (intervenerId == null) {
      return BaseResponse.error("Intervener ID is required", 400);
    }
    List<Project> projects = projectMapper.findByIntervenerId(intervenerId);
    return BaseResponse.success("Projects retrieved successfully", projects);
  }

  @Override
  public BaseResponse<List<Project>> findByWinnerUserId(Long winnerUserId) {
    if (winnerUserId == null) {
      return BaseResponse.error("Winner user ID is required", 400);
    }
    List<Project> projects = projectMapper.findByWinnerUserId(winnerUserId);
    return BaseResponse.success("Projects retrieved successfully", projects);
  }

  @Override
  public BaseResponse<List<Project>> findBySubmissionDateRange(LocalDate startDate, LocalDate endDate) {
    if (startDate == null || endDate == null) {
      return BaseResponse.error("Start date and end date are required", 400);
    }
    if (endDate.isBefore(startDate)) {
      return BaseResponse.error("End date must not be before start date", 400);
    }
    List<Project> projects = lambdaQuery()
        .between(Project::getSubmissionDate, startDate, endDate)
        .list();
    return BaseResponse.success("Projects retrieved successfully", projects);
  }

  @Override
  public BaseResponse<PagedResponse<Project>> searchByDescription(String keyword, int page, int size) {
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    QueryWrapper<Project> wrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(keyword)) {
      wrapper.like("description", keyword.trim());
    }
    Page<Project> pageRequest = new Page<>(safePage + 1L, safeSize);
    Page<Project> result = page(pageRequest, wrapper);
    return BaseResponse.success("Projects retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<PagedResponse<Project>> getProjectsByIntervener(Long intervenerId, int page, int size) {
    if (intervenerId == null) {
      return BaseResponse.error("Intervener ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    Page<Project> pageRequest = new Page<>(safePage + 1L, safeSize);
    QueryWrapper<Project> wrapper = new QueryWrapper<>();
    wrapper.eq("intervener_id", intervenerId);
    Page<Project> result = page(pageRequest, wrapper);
    return BaseResponse.success("Projects retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<PagedResponse<Project>> getAvailableProjects(int page, int size) {
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    Page<Project> pageRequest = new Page<>(safePage + 1L, safeSize);
    Page<Project> result = page(pageRequest);
    return BaseResponse.success("Projects retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<Project> createProject(Project project) {
    if (project == null || StringUtils.isBlank(project.getName())) {
      return BaseResponse.error("Project name is required", 400);
    }
    boolean created = save(project);
    if (!created) {
      return BaseResponse.error("Failed to create project");
    }
    return BaseResponse.success("Project created successfully", project);
  }

  @Override
  public BaseResponse<Project> updateProject(Project project) {
    if (project == null || project.getId() == null) {
      return BaseResponse.error("Project ID is required for update", 400);
    }
    Project existing = getById(project.getId());
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + project.getId(), 404);
    }
    boolean updated = lambdaUpdate()
        .eq(Project::getId, project.getId())
        .set(StringUtils.isNotBlank(project.getName()), Project::getName, project.getName())
        .set(StringUtils.isNotBlank(project.getDescription()), Project::getDescription, project.getDescription())
        .set(project.getSubmissionDate() != null, Project::getSubmissionDate, project.getSubmissionDate())
        .set(project.getIntervenerId() != null, Project::getIntervenerId, project.getIntervenerId())
        .set(project.getWinnerUserId() != null, Project::getWinnerUserId, project.getWinnerUserId())
        .update();
    if (!updated) {
      return BaseResponse.error("Failed to update project");
    }
    Project refreshed = getById(project.getId());
    return BaseResponse.success("Project updated successfully", refreshed);
  }

  @Override
  public BaseResponse<Boolean> deleteProject(Long id) {
    if (id == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    Project existing = getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete project");
    }
    return BaseResponse.success("Project deleted successfully", true);
  }

  @Override
  public BaseResponse<Boolean> defineWinner(Long projectId, Long winnerUserId) {
    if (projectId == null || winnerUserId == null) {
      return BaseResponse.error("Project ID and winner user ID are required", 400);
    }
    Project existing = getById(projectId);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + projectId, 404);
    }
    boolean updated = lambdaUpdate()
        .eq(Project::getId, projectId)
        .set(Project::getWinnerUserId, winnerUserId)
        .update();
    if (!updated) {
      return BaseResponse.error("Failed to define project winner");
    }
    return BaseResponse.success("Project winner defined successfully", true);
  }

  @Override
  public BaseResponse<Project> getProjectWithDetails(Long id) {
    if (id == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    Project project = getById(id);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + id, 404);
    }
    return BaseResponse.success("Project retrieved successfully", project);
  }

  @Override
  public BaseResponse<Object> getProjectStatistics(Long projectId) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    Project project = getById(projectId);
    if (project == null || Boolean.TRUE.equals(project.getIsDeleted())) {
      return BaseResponse.error("Project not found with ID: " + projectId, 404);
    }
    Map<String, Object> stats = Map.of(
        "projectId", projectId,
        "hasWinner", project.getWinnerUserId() != null,
        "submissionDate", project.getSubmissionDate(),
        "lastUpdated", project.getLastModificationDate());
    return BaseResponse.success("Project statistics retrieved successfully", stats);
  }
}
