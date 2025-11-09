package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}
