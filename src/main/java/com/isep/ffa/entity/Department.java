package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Department entity class
 * Database table: department
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("department")
public class Department {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Department name
   */
  @TableField("name")
  private String name;

  /**
   * RegionID
   */
  @TableField("region_id")
  private Long regionId;

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
   * Region information
   */
  @TableField(exist = false)
  private Region region;

  /**
   * City list
   */
  @TableField(exist = false)
  private List<City> cities;
}
