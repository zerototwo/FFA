package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * 角色实体类
 * 对应数据库表：role
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("role")
public class Role {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 角色名称
   */
  @TableField("name")
  private String name;

  /**
   * 创建日期
   */
  @TableField("creation_date")
  private LocalDate creationDate;
}
