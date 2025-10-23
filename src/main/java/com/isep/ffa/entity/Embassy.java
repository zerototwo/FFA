package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 大使馆实体类
 * 对应数据库表：embassy
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("embassy")
public class Embassy {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 地址
   */
  @TableField("address")
  private String address;

  /**
   * 所属国家ID（大使馆代表的国家）
   */
  @TableField("embassy_of_country_id")
  private Long embassyOfCountryId;

  /**
   * 所在国家ID（大使馆所在的国家）
   */
  @TableField("embassy_in_country_id")
  private Long embassyInCountryId;

  /**
   * 城市ID
   */
  @TableField("city_id")
  private Long cityId;

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
   * 干预者列表
   */
  @TableField(exist = false)
  private List<Intervener> interveners;
}
