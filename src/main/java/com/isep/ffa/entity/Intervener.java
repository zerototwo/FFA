package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Intervener entity class
 * Database table: intervener
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("intervener")
public class Intervener {

  /**
   * Primary key ID (references person table ID)
   */
  @TableId(value = "person_id", type = IdType.INPUT)
  private Long personId;

  /**
   * Embassy ID
   */
  @TableField("embassy_id")
  private Long embassyId;

  // Related objects (non-persistent fields)
  /**
   * Person information
   */
  @TableField(exist = false)
  private Person person;

  /**
   * Embassy information
   */
  @TableField(exist = false)
  private Embassy embassy;

  /**
   * Project list
   */
  @TableField(exist = false)
  private List<Project> projects;
}
