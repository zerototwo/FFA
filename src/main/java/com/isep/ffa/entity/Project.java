package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 项目实体类
 * 对应数据库表：project
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("project")
public class Project {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 项目名称
   */
  @TableField("name")
  private String name;

  /**
   * 项目描述
   */
  @TableField("description")
  private String description;

  /**
   * 提交日期
   */
  @TableField("submission_date")
  private LocalDate submissionDate;

  /**
   * 干预者ID
   */
  @TableField("intervener_id")
  private Long intervenerId;

  /**
   * 获胜用户ID
   */
  @TableField("winner_user_id")
  private Long winnerUserId;

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
   * 干预者信息
   */
  @TableField(exist = false)
  private Intervener intervener;

  /**
   * 获胜用户信息
   */
  @TableField(exist = false)
  private User winnerUser;

  /**
   * 申请列表
   */
  @TableField(exist = false)
  private List<Application> applications;
}
