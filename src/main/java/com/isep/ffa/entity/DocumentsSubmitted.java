package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 已提交文档实体类
 * 对应数据库表：documents_submitted
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("documents_submitted")
public class DocumentsSubmitted {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 文档路径
   */
  @TableField("path")
  private String path;

  /**
   * 文档类型ID
   */
  @TableField("document_type_id")
  private Long documentTypeId;

  /**
   * 申请ID
   */
  @TableField("application_id")
  private Long applicationId;

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
   * 申请信息
   */
  @TableField(exist = false)
  private Application application;
}
