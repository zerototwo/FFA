package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Project entity class
 * Database table: project
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project")
public class Project {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Project Name
   */
  @TableField("name")
  private String name;

  /**
   * Project Description
   */
  @TableField("description")
  private String description;

  /**
   * Submission Date
   */
  @TableField("submission_date")
  private LocalDate submissionDate;

  /**
   * Project Status (DRAFT, PENDING_APPROVAL, PUBLISHED)
   */
  @TableField("status")
  @Schema(description = "Project status", example = "DRAFT", allowableValues = {"DRAFT", "PENDING_APPROVAL", "PUBLISHED"})
  private String status;

  /**
   * Total Budget
   */
  @TableField("total_budget")
  @Schema(description = "Total budget for the project", example = "50000.00")
  private BigDecimal totalBudget;

  /**
   * Start Date
   */
  @TableField("start_date")
  @Schema(description = "Project start date", example = "2024-02-01")
  private LocalDate startDate;

  /**
   * Location ID (references city table)
   */
  @TableField("location_id")
  @Schema(description = "Location city ID", example = "1")
  private Long locationId;

  /**
   * Intervener ID
   */
  @TableField("intervener_id")
  private Long intervenerId;

  /**
   * Winner User ID
   */
  @TableField("winner_user_id")
  private Long winnerUserId;

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
   * Intervener information
   */
  @TableField(exist = false)
  private Intervener intervener;

  /**
   * Winner user information
   */
  @TableField(exist = false)
  private User winnerUser;

  /**
   * Application list
   */
  @TableField(exist = false)
  private List<Application> applications;

  /**
   * Location city information
   */
  @TableField(exist = false)
  private City location;
}
