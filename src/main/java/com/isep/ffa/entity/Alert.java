package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Alert实体类
 * Database table：alert
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
   * Alert内容
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
   * 阅读日期
   */
  @TableField("read_date")
  private LocalDateTime readDate;

  /**
   * Is Deleted
   */
  @TableField("is_deleted")
  private Boolean isDeleted;

  /**
   * Alert日期
   */
  @TableField("alert_date")
  private LocalDateTime alertDate;

  // 关联对象（非数据库字段）
  /**
   * 接收者信息
   */
  @TableField(exist = false)
  private Person receiver;
}
