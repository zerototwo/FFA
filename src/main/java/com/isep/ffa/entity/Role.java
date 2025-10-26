package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Role实体类
 * Database table：role
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role")
public class Role {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Role名称
   */
  @TableField("name")
  private String name;

  /**
   * Creation Date
   */
  @TableField("creation_date")
  private LocalDate creationDate;
}
