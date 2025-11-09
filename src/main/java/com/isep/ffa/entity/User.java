package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * User entity class
 * Database table: users
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("users")
public class User {

  /**
   * Primary key ID (references person table ID)
   */
  @TableId(value = "person_id", type = IdType.INPUT)
  private Long personId;

  // Related objects (non-persistent fields)
  /**
   * Person information
   */
  @TableField(exist = false)
  private Person person;

  /**
   * Application list
   */
  @TableField(exist = false)
  private List<Application> applications;
}
