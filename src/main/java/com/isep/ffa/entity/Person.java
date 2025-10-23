package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 人员实体类
 * 对应数据库表：person
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("person")
public class Person {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 名字
   */
  @TableField("first_name")
  private String firstName;

  /**
   * 姓氏
   */
  @TableField("last_name")
  private String lastName;

  /**
   * 邮箱
   */
  @TableField("email")
  private String email;

  /**
   * 地址
   */
  @TableField("address")
  private String address;

  /**
   * 登录名
   */
  @TableField("login")
  private String login;

  /**
   * 角色ID
   */
  @TableField("role_id")
  private Long roleId;

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
   * 角色信息
   */
  @TableField(exist = false)
  private Role role;

  /**
   * 城市信息
   */
  @TableField(exist = false)
  private City city;
}
