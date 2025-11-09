package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Region entity class
 * Database table: region
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("region")
public class Region {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Region name
   */
  @TableField("name")
  private String name;

  /**
   * Country ID
   */
  @TableField("country_id")
  private Long countryId;

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
   * Country information
   */
  @TableField(exist = false)
  private Country country;

  /**
   * Department list
   */
  @TableField(exist = false)
  private List<Department> departments;
}
