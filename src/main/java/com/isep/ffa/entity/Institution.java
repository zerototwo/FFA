package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 机构实体类
 * 对应数据库表：institution
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("institution")
public class Institution {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 机构名称
   */
  @TableField("name")
  private String name;

  /**
   * 地址
   */
  @TableField("address")
  private String address;

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
   * 城市信息
   */
  @TableField(exist = false)
  private City city;
}
