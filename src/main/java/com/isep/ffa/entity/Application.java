package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Application entity class
 * Database table: application
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("application")
public class Application {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Application Date
   */
  @TableField("date_application")
  private LocalDateTime dateApplication;

  /**
   * Application motivation
   */
  @TableField("motivation")
  @Schema(description = "Application motivation letter", example = "I am interested in this project because...")
  private String motivation;

  /**
   * Application status (DRAFT, SUBMITTED, REVIEWING, APPROVED, REJECTED)
   */
  @TableField("status")
  @Schema(description = "Application status", example = "DRAFT", allowableValues = { "DRAFT", "SUBMITTED", "REVIEWING",
      "APPROVED", "REJECTED" })
  private String status;

  /**
   * Application title (Step 1: Basics)
   */
  @TableField("title")
  @Schema(description = "Application title", example = "Youth Entrepreneurship Program 2024")
  private String title;

  /**
   * Application description (Step 1: Basics)
   */
  @TableField("description")
  @Schema(description = "Application description", example = "A comprehensive program to support young entrepreneurs...")
  private String description;

  /**
   * Project scope (Step 2: Scope)
   */
  @TableField("scope")
  @Schema(description = "Project scope and objectives", example = "The project aims to support 50 young entrepreneurs...")
  private String scope;

  /**
   * Budget (Step 3: Budget)
   */
  @TableField("budget")
  @Schema(description = "Total budget for the application", example = "50000.00")
  private BigDecimal budget;

  /**
   * Start date (Step 1: Basics)
   */
  @TableField("start_date")
  @Schema(description = "Project start date", example = "2024-02-01")
  private LocalDate startDate;

  /**
   * End date (Step 1: Basics)
   */
  @TableField("end_date")
  @Schema(description = "Project end date", example = "2024-12-31")
  private LocalDate endDate;

  /**
   * Location ID (references city table)
   */
  @TableField("location_id")
  @Schema(description = "Location city ID", example = "1")
  private Long locationId;

  /**
   * Current step in the multi-step process (1-5)
   */
  @TableField("current_step")
  @Schema(description = "Current step in the application process", example = "3", allowableValues = { "1", "2", "3",
      "4", "5" })
  private Integer currentStep;

  /**
   * User ID
   */
  @TableField("user_id")
  private Long userId;

  /**
   * Project ID
   */
  @TableField("project_id")
  private Long projectId;

  /**
   * Creation Date
   */
  @TableField(value = "creation_date", fill = FieldFill.INSERT)
  private LocalDate creationDate;

  /**
   * Last Modification Date
   */
  @TableField(value = "last_modification_date", fill = FieldFill.INSERT_UPDATE)
  private LocalDate lastModificationDate;

  /**
   * Creator User ID
   */
  @TableField("creator_user")
  private Long creatorUser;

  /**
   * Last Modificator User ID
   */
  @TableField("last_modificator_user")
  private Long lastModificatorUser;

  /**
   * Is Deleted (logical delete)
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // Related objects (non-persistent fields)
  /**
   * User information
   */
  @TableField(exist = false)
  private User user;

  /**
   * Project information
   */
  @TableField(exist = false)
  private Project project;

  /**
   * Submitted documents list
   */
  @TableField(exist = false)
  private List<DocumentsSubmitted> documentsSubmitted;

  /**
   * Location city information
   */
  @TableField(exist = false)
  private City location;
}
