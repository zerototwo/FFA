package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 大陆实体类
 * 对应数据库表：continent
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("continent")
public class Continent {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 大陆名称
   */
  @TableField("name")
  private String name;

  // 关联对象（非数据库字段）
  /**
   * 国家列表
   */
  @TableField(exist = false)
  private List<Country> countries;
}
