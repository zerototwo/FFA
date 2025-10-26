package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * Embassy实体类
 * Database table：embassy
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
   * 所属国家ID（Embassy代表的国家）
   */
  @TableField("embassy_of_country_id")
  private Long embassyOfCountryId;

  /**
   * 所在国家ID（Embassy所在的国家）
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
   * Is Deleted（逻辑删除）
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * 所属国家信息
   */
  @TableField(exist = false)
  private Country embassyOfCountry;

  /**
   * 所在国家信息
   */
  @TableField(exist = false)
  private Country embassyInCountry;

  /**
   * 城市信息
   */
  @TableField(exist = false)
  private City city;

  /**
   * Intervener列表
   */
  @TableField(exist = false)
  private List<Intervener> interveners;
}
