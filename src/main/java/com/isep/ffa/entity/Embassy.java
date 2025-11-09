package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Embassy entity class
 * Database table: embassy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("embassy")
public class Embassy {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Address
   */
  @TableField("address")
  private String address;

  /**
   * Country ID represented by the embassy
   */
  @TableField("embassy_of_country_id")
  private Long embassyOfCountryId;

  /**
   * Country ID where the embassy is located
   */
  @TableField("embassy_in_country_id")
  private Long embassyInCountryId;

  /**
   * City ID
   */
  @TableField("city_id")
  private Long cityId;

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
   * Represented country information
   */
  @TableField(exist = false)
  private Country embassyOfCountry;

  /**
   * Host country information
   */
  @TableField(exist = false)
  private Country embassyInCountry;

  /**
   * City information
   */
  @TableField(exist = false)
  private City city;

  /**
   * Intervener list
   */
  @TableField(exist = false)
  private List<Intervener> interveners;
}
