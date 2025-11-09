package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Submitted document entity
 * Database table: documents_submitted
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("documents_submitted")
public class DocumentsSubmitted {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Document path
   */
  @TableField("path")
  private String path;

  /**
   * Document TypeID
   */
  @TableField("document_type_id")
  private Long documentTypeId;

  /**
   * Application ID
   */
  @TableField("application_id")
  private Long applicationId;

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
   * Application information
   */
  @TableField(exist = false)
  private Application application;
}
