package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 申请实体类
 * Database table：application
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
   * 申请Motivation
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
   * Is Deleted（逻辑删除）
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * User信息
   */
  @TableField(exist = false)
  private User user;

  /**
   * Project信息
   */
  @TableField(exist = false)
  private Project project;

  /**
   * 已提交文档列表
   */
  @TableField(exist = false)
  private List<DocumentsSubmitted> documentsSubmitted;
}
