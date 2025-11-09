package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Alert entity class
 * Database table: alert
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("alert")
public class Alert {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Alert content
   */
  @TableField("content")
  private String content;

  /**
   * Receiver ID
   */
  @TableField("receiver_id")
  private Long receiverId;

  /**
   * Is Read
   */
  @TableField("is_read")
  private Boolean isRead;

  /**
   * Read date
   */
  @TableField("read_date")
  private LocalDateTime readDate;

  /**
   * Is Deleted
   */
  @TableField("is_deleted")
  private Boolean isDeleted;

  /**
   * Alert date
   */
  @TableField("alert_date")
  private LocalDateTime alertDate;

  // Related objects (non-persistent fields)
  /**
   * Receiver information
   */
  @TableField(exist = false)
  private Person receiver;
}
