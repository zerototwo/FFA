package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Country Entity
 * Database table：country
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("country")
public class Country {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Country Name
   */
  @TableField("name")
  private String name;

  /**
   * Phone Number Indicator
   */
  @TableField("phone_number_indicator")
  private String phoneNumberIndicator;

  /**
   * Continent ID
   */
  @TableField("continent_id")
  private Long continentId;

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
   * Continent information
   */
  @TableField(exist = false)
  private Continent continent;

  /**
   * Region list
   */
  @TableField(exist = false)
  private List<Region> regions;

  /**
   * Embassy list
   */
  @TableField(exist = false)
  private List<Embassy> embassies;
}
