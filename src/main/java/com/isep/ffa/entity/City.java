package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * 城市实体类
 * Database table：city
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
   * Is Deleted（逻辑删除）
   */
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * Department信息
   */
  @TableField(exist = false)
  private Department department;

  /**
   * Embassy列表
   */
  @TableField(exist = false)
  private List<Embassy> embassies;

  /**
   * Institution列表
   */
  @TableField(exist = false)
  private List<Institution> institutions;
}
