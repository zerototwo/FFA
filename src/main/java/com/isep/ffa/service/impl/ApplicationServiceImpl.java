package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.Application;
import com.isep.ffa.mapper.ApplicationMapper;
import com.isep.ffa.service.ApplicationService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Application Service Implementation
 * Implements business logic for application management
 */
@Service
public class ApplicationServiceImpl extends BaseServiceImpl<ApplicationMapper, Application>
    implements ApplicationService {

  @Autowired
  private ApplicationMapper applicationMapper;

  private int normalizePage(int page) {
    return Math.max(page, 1);
  }

  private int normalizeSize(int size) {
    return size <= 0 ? 10 : size;
  }

  private PagedResponse<Application> toPagedResponse(Page<Application> pageResult) {
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
  public BaseResponse<List<Application>> findByProjectId(Long projectId) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    List<Application> applications = applicationMapper.findByProjectId(projectId);
    return BaseResponse.success("Applications retrieved successfully", applications);
  }

  @Override
  public BaseResponse<List<Application>> findByUserId(Long userId) {
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    List<Application> applications = applicationMapper.findByUserId(userId);
    return BaseResponse.success("Applications retrieved successfully", applications);
  }

  @Override
  public BaseResponse<List<Application>> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    if (startDate == null || endDate == null) {
      return BaseResponse.error("Start date and end date are required", 400);
    }
    if (startDate.isAfter(endDate)) {
      return BaseResponse.error("Start date must be before end date", 400);
    }
    String startDateStr = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    String endDateStr = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    List<Application> applications = applicationMapper.findByDateApplicationRange(startDateStr, endDateStr);
    return BaseResponse.success("Applications retrieved successfully", applications);
  }

  @Override
  public BaseResponse<PagedResponse<Application>> searchByMotivation(String keyword, int page, int size) {
    if (StringUtils.isBlank(keyword)) {
      return BaseResponse.error("Search keyword is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    List<Application> allMatches = applicationMapper.findByMotivationLike(keyword.trim());
    
    // Manual pagination
    int start = (safePage - 1) * safeSize;
    int end = Math.min(start + safeSize, allMatches.size());
    List<Application> pagedList = start < allMatches.size() ? allMatches.subList(start, end) : List.of();
    
    int totalPages = (int) Math.ceil((double) allMatches.size() / safeSize);
    PagedResponse<Application> pagedResponse = PagedResponse.of(
        pagedList,
        safePage - 1,
        safeSize,
        allMatches.size(),
        totalPages);
    
    return BaseResponse.success("Applications retrieved successfully", pagedResponse);
  }

  @Override
  public BaseResponse<PagedResponse<Application>> getApplicationsByProject(Long projectId, int page, int size) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    QueryWrapper<Application> wrapper = new QueryWrapper<>();
    wrapper.eq("project_id", projectId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("date_application");
    
    Page<Application> pageRequest = new Page<>(safePage, safeSize);
    Page<Application> result = page(pageRequest, wrapper);
    
    return BaseResponse.success("Applications retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<PagedResponse<Application>> getApplicationsByUser(Long userId, int page, int size) {
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    QueryWrapper<Application> wrapper = new QueryWrapper<>();
    wrapper.eq("user_id", userId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("date_application");
    
    Page<Application> pageRequest = new Page<>(safePage, safeSize);
    Page<Application> result = page(pageRequest, wrapper);
    
    return BaseResponse.success("Applications retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<Application> createApplication(Application application) {
    if (application == null) {
      return BaseResponse.error("Application data is required", 400);
    }
    if (application.getProjectId() == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    if (application.getUserId() == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    if (application.getDateApplication() == null) {
      application.setDateApplication(LocalDateTime.now());
    }
    boolean saved = save(application);
    if (!saved) {
      return BaseResponse.error("Failed to create application", 500);
    }
    return BaseResponse.success("Application created successfully", application);
  }

  @Override
  public BaseResponse<Application> updateApplication(Application application) {
    if (application == null || application.getId() == null) {
      return BaseResponse.error("Application ID is required for update", 400);
    }
    Application existing = getById(application.getId());
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + application.getId(), 404);
    }
    boolean updated = updateById(application);
    if (!updated) {
      return BaseResponse.error("Failed to update application", 500);
    }
    return BaseResponse.success("Application updated successfully", application);
  }

  @Override
  public BaseResponse<Boolean> deleteApplication(Long id) {
    if (id == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    Application existing = getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete application", 500);
    }
    return BaseResponse.success("Application deleted successfully", true);
  }

  @Override
  public BaseResponse<Application> submitApplication(Long projectId, Long userId, String motivation) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    if (StringUtils.isBlank(motivation)) {
      return BaseResponse.error("Motivation is required", 400);
    }
    
    // Check if user already applied
    BaseResponse<Boolean> hasApplied = hasUserAppliedToProject(projectId, userId);
    if (hasApplied.isSuccess() && Boolean.TRUE.equals(hasApplied.getData())) {
      return BaseResponse.error("You have already applied to this project", 409);
    }
    
    Application application = new Application();
    application.setProjectId(projectId);
    application.setUserId(userId);
    application.setMotivation(motivation.trim());
    application.setDateApplication(LocalDateTime.now());
    
    return createApplication(application);
  }

  @Override
  public BaseResponse<Application> getApplicationWithDetails(Long id) {
    if (id == null) {
      return BaseResponse.error("Application ID is required", 400);
    }
    Application application = getById(id);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + id, 404);
    }
    return BaseResponse.success("Application retrieved successfully", application);
  }

  @Override
  public BaseResponse<Boolean> hasUserAppliedToProject(Long projectId, Long userId) {
    if (projectId == null || userId == null) {
      return BaseResponse.error("Project ID and User ID are required", 400);
    }
    QueryWrapper<Application> wrapper = new QueryWrapper<>();
    wrapper.eq("project_id", projectId);
    wrapper.eq("user_id", userId);
    wrapper.eq("is_deleted", false);
    long count = count(wrapper);
    return BaseResponse.success("Check completed", count > 0);
  }

  @Override
  public Long countApplicationsForIntervenerProjects(Long intervenerId) {
    if (intervenerId == null) {
      return 0L;
    }
    return applicationMapper.countApplicationsForIntervenerProjects(intervenerId);
  }

  @Override
  public Long countApplicationsForIntervenerProjectsThisWeek(Long intervenerId) {
    if (intervenerId == null) {
      return 0L;
    }
    return applicationMapper.countApplicationsForIntervenerProjectsThisWeek(intervenerId);
  }

  @Override
  public BaseResponse<List<Application>> getRecentApplicationsForIntervener(Long intervenerId, Integer limit) {
    if (intervenerId == null) {
      return BaseResponse.error("Intervener ID is required", 400);
    }
    int safeLimit = limit != null && limit > 0 ? limit : 10;
    List<Application> applications = applicationMapper.findRecentApplicationsForIntervener(intervenerId, safeLimit);
    return BaseResponse.success("Recent applications retrieved", applications);
  }

  @Override
  public BaseResponse<Application> saveApplicationStep(Long applicationId, Long projectId, Long userId, Integer step, Object data) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }
    if (step == null || step < 1 || step > 5) {
      return BaseResponse.error("Step number must be between 1 and 5", 400);
    }

    Application application;
    if (applicationId != null) {
      // Update existing application
      application = getById(applicationId);
      if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
        return BaseResponse.error("Application not found with ID: " + applicationId, 404);
      }
      if (!userId.equals(application.getUserId())) {
        return BaseResponse.error("You don't have permission to update this application", 403);
      }
      if (!projectId.equals(application.getProjectId())) {
        return BaseResponse.error("Project ID mismatch", 400);
      }
    } else {
      // Check if draft already exists
      BaseResponse<Application> draftResponse = getDraftApplication(projectId, userId);
      if (draftResponse.isSuccess() && draftResponse.getData() != null) {
        application = draftResponse.getData();
      } else {
        // Create new draft application
        application = new Application();
        application.setProjectId(projectId);
        application.setUserId(userId);
        application.setStatus("DRAFT");
        application.setCurrentStep(1);
        application.setDateApplication(LocalDateTime.now());
      }
    }

    // Update application based on step
    if (data instanceof com.isep.ffa.dto.request.ApplicationStepRequest) {
      com.isep.ffa.dto.request.ApplicationStepRequest request = (com.isep.ffa.dto.request.ApplicationStepRequest) data;
      
      // Step 1: Basics
      if (step == 1) {
        if (request.getTitle() != null) {
          application.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
          application.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
          application.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
          application.setEndDate(request.getEndDate());
        }
        if (request.getLocationId() != null) {
          application.setLocationId(request.getLocationId());
        }
        application.setCurrentStep(1);
      }
      // Step 2: Scope
      else if (step == 2) {
        if (request.getScope() != null) {
          application.setScope(request.getScope());
        }
        application.setCurrentStep(2);
      }
      // Step 3: Budget
      else if (step == 3) {
        if (request.getBudget() != null) {
          application.setBudget(request.getBudget());
        }
        application.setCurrentStep(3);
      }
      // Step 4: Documents (handled separately via document upload)
      else if (step == 4) {
        application.setCurrentStep(4);
      }
      // Step 5: Review and submit
      else if (step == 5) {
        application.setCurrentStep(5);
        if (Boolean.TRUE.equals(request.getSubmit())) {
          application.setStatus("SUBMITTED");
          application.setDateApplication(LocalDateTime.now());
        }
      }
    }

    // Save application
    if (application.getId() == null) {
      boolean saved = save(application);
      if (!saved) {
        return BaseResponse.error("Failed to save application step", 500);
      }
    } else {
      boolean updated = updateById(application);
      if (!updated) {
        return BaseResponse.error("Failed to update application step", 500);
      }
    }

    return BaseResponse.success("Application step saved successfully", application);
  }

  @Override
  public BaseResponse<Application> saveDraft(Application application) {
    if (application == null) {
      return BaseResponse.error("Application data is required", 400);
    }
    if (application.getProjectId() == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    if (application.getUserId() == null) {
      return BaseResponse.error("User ID is required", 400);
    }

    // Set status to DRAFT if not set
    if (application.getStatus() == null || application.getStatus().isEmpty()) {
      application.setStatus("DRAFT");
    }

    // Set current step if not set
    if (application.getCurrentStep() == null) {
      application.setCurrentStep(1);
    }

    if (application.getId() == null) {
      // Create new draft
      if (application.getDateApplication() == null) {
        application.setDateApplication(LocalDateTime.now());
      }
      boolean saved = save(application);
      if (!saved) {
        return BaseResponse.error("Failed to save draft application", 500);
      }
    } else {
      // Update existing draft
      Application existing = getById(application.getId());
      if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
        return BaseResponse.error("Application not found with ID: " + application.getId(), 404);
      }
      if (!"DRAFT".equals(existing.getStatus())) {
        return BaseResponse.error("Only DRAFT applications can be updated", 400);
      }
      boolean updated = updateById(application);
      if (!updated) {
        return BaseResponse.error("Failed to update draft application", 500);
      }
    }

    return BaseResponse.success("Draft application saved successfully", application);
  }

  @Override
  public BaseResponse<Application> getDraftApplication(Long projectId, Long userId) {
    if (projectId == null) {
      return BaseResponse.error("Project ID is required", 400);
    }
    if (userId == null) {
      return BaseResponse.error("User ID is required", 400);
    }

    QueryWrapper<Application> wrapper = new QueryWrapper<>();
    wrapper.eq("project_id", projectId);
    wrapper.eq("user_id", userId);
    wrapper.eq("status", "DRAFT");
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("creation_date");
    wrapper.last("LIMIT 1");

    Application draft = getOne(wrapper);
    if (draft == null) {
      return BaseResponse.error("No draft application found", 404);
    }

    return BaseResponse.success("Draft application retrieved", draft);
  }

  @Override
  public BaseResponse<Application> submitApplicationFinal(Long applicationId) {
    if (applicationId == null) {
      return BaseResponse.error("Application ID is required", 400);
    }

    Application application = getById(applicationId);
    if (application == null || Boolean.TRUE.equals(application.getIsDeleted())) {
      return BaseResponse.error("Application not found with ID: " + applicationId, 404);
    }

    if (!"DRAFT".equals(application.getStatus())) {
      return BaseResponse.error("Only DRAFT applications can be submitted", 400);
    }

    // Validate required fields before submission
    if (StringUtils.isBlank(application.getTitle())) {
      return BaseResponse.error("Title is required for submission", 400);
    }
    if (StringUtils.isBlank(application.getDescription())) {
      return BaseResponse.error("Description is required for submission", 400);
    }
    if (application.getStartDate() == null) {
      return BaseResponse.error("Start date is required for submission", 400);
    }
    if (application.getEndDate() == null) {
      return BaseResponse.error("End date is required for submission", 400);
    }

    // Update status to SUBMITTED
    application.setStatus("SUBMITTED");
    application.setDateApplication(LocalDateTime.now());
    application.setCurrentStep(5);

    boolean updated = updateById(application);
    if (!updated) {
      return BaseResponse.error("Failed to submit application", 500);
    }

    return BaseResponse.success("Application submitted successfully", application);
  }
}
