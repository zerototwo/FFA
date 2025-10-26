package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Intervener实体类
 * Database table：intervener
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("intervener")
public class Intervener {

  /**
   * Primary Key ID（对应person表的id）
   */
  @TableId(value = "person_id", type = IdType.INPUT)
  private Long personId;

  /**
   * EmbassyID
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
   * Embassy信息
   */
  @TableField(exist = false)
  private Embassy embassy;

  /**
   * Project列表
   */
  @TableField(exist = false)
  private List<Project> projects;
}
