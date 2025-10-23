package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户实体类
 * 对应数据库表：users
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
public class User {

  /**
   * 主键ID（对应person表的id）
   */
  @TableId(value = "person_id", type = IdType.INPUT)
  private Long personId;

  // 关联对象（非数据库字段）
  /**
   * 人员信息
   */
  @TableField(exist = false)
  private Person person;

  /**
   * 申请列表
   */
  @TableField(exist = false)
  private List<Application> applications;
}
