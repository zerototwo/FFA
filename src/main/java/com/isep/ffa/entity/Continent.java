package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Continent entity class
 * Database table: continent
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("continent")
public class Continent {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Continent name
   */
  @TableField("name")
  private String name;

  // Related objects (non-persistent fields)
  /**
   * Country list
   */
  @TableField(exist = false)
  private List<Country> countries;
}
