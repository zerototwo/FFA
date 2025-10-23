package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 项目所需文档实体类
 * 对应数据库表：documents_need_for_project
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("documents_need_for_project")
public class DocumentsNeedForProject {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 是否必需
   */
  @TableField("is_mandatory")
  private Boolean isMandatory;

  /**
   * 最大数量
   */
  @TableField("max_number")
  private Integer maxNumber;

  /**
   * 最小数量
   */
  @TableField("min_number")
  private Integer minNumber;

  /**
   * 文档类型ID
   */
  @TableField("document_type_id")
  private Long documentTypeId;

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
   * 文档类型信息
   */
  @TableField(exist = false)
  private DocumentType documentType;

  /**
   * 项目信息
   */
  @TableField(exist = false)
  private Project project;
}
