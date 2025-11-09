package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
  private String motivation;

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
}
