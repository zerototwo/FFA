package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * City entity class
 * Database table: city
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("city")
public class City {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * City Name
   */
  @TableField("name")
  private String name;

  /**
   * Postal Code
   */
  @TableField("postal_code")
  private Integer postalCode;

  /**
   * Department ID
   */
  @TableField("department_id")
  private Long departmentId;

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
   * Department information
   */
  @TableField(exist = false)
  private Department department;

  /**
   * Embassy list
   */
  @TableField(exist = false)
  private List<Embassy> embassies;

  /**
   * Institution list
   */
  @TableField(exist = false)
  private List<Institution> institutions;
}
