package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 申请实体类
 * 对应数据库表：application
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("application")
public class Application {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 申请日期
   */
  @TableField("date_application")
  private LocalDateTime dateApplication;

  /**
   * 申请动机
   */
  @TableField("motivation")
  private String motivation;

  /**
   * 用户ID
   */
  @TableField("user_id")
  private Long userId;

  /**
   * 项目ID
   */
  @TableField("project_id")
  private Long projectId;

  /**
   * 创建日期
   */
  @TableField(value = "creation_date", fill = FieldFill.INSERT)
  private LocalDate creationDate;

  /**
   * 最后修改日期
   */
  @TableField(value = "last_modification_date", fill = FieldFill.INSERT_UPDATE)
  private LocalDate lastModificationDate;

  /**
   * 创建用户ID
   */
  @TableField("creator_user")
  private Long creatorUser;

  /**
   * 最后修改用户ID
   */
  @TableField("last_modificator_user")
  private Long lastModificatorUser;

  /**
   * 是否删除（逻辑删除）
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * 用户信息
   */
  @TableField(exist = false)
  private User user;

  /**
   * 项目信息
   */
  @TableField(exist = false)
  private Project project;

  /**
   * 已提交文档列表
   */
  @TableField(exist = false)
  private List<DocumentsSubmitted> documentsSubmitted;
}
