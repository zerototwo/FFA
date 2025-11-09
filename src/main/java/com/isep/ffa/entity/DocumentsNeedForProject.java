package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Documents required for project entity
 * Database table: documents_need_for_project
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
   * Whether the document is mandatory
   */
  @TableField("is_mandatory")
  private Boolean isMandatory;

  /**
   * Maximum number of documents
   */
  @TableField("max_number")
  private Integer maxNumber;

  /**
   * Minimum number of documents
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
   * Is Deleted (logical delete)
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // Related objects (non-persistent fields)
  /**
   * Document type information
   */
  @TableField(exist = false)
  private DocumentType documentType;

  /**
   * Project information
   */
  @TableField(exist = false)
  private Project project;
}
