package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Person Entity
 * Database table：person
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("person")
public class Person {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * First Name
   */
  @TableField("first_name")
  private String firstName;

  /**
   * Last Name
   */
  @TableField("last_name")
  private String lastName;

  /**
   * Email
   */
  @TableField("email")
  private String email;

  /**
   * Address
   */
  @TableField("address")
  private String address;

  /**
   * Login Name
   */
  @TableField("login")
  private String login;

  /**
   * Role ID
   */
  @TableField("role_id")
  private Long roleId;

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
   * Role信息
   */
  @TableField(exist = false)
  private Role role;

  /**
   * 城市信息
   */
  @TableField(exist = false)
  private City city;
}
