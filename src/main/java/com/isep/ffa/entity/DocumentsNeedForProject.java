package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Project所需文档实体类
 * Database table：documents_need_for_project
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("documents_need_for_project")
public class DocumentsNeedForProject {

  /**
   * Primary Key ID
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
   * Document TypeID
   */
  @TableField("document_type_id")
  private Long documentTypeId;

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
   * Document Type信息
   */
  @TableField(exist = false)
  private DocumentType documentType;

  /**
   * Project信息
   */
  @TableField(exist = false)
  private Project project;
}
