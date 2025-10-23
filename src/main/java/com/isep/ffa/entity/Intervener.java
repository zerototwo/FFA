package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 干预者实体类
 * 对应数据库表：intervener
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("intervener")
public class Intervener {

  /**
   * 主键ID（对应person表的id）
   */
  @TableId(value = "person_id", type = IdType.INPUT)
  private Long personId;

  /**
   * 大使馆ID
   */
  @TableField("embassy_id")
  private Long embassyId;

  // 关联对象（非数据库字段）
  /**
   * 人员信息
   */
  @TableField(exist = false)
  private Person person;

  /**
   * 大使馆信息
   */
  @TableField(exist = false)
  private Embassy embassy;

  /**
   * 项目列表
   */
  @TableField(exist = false)
  private List<Project> projects;
}
