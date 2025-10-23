package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 警报实体类
 * 对应数据库表：alert
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("alert")
public class Alert {

  /**
   * 主键ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * 警报内容
   */
  @TableField("content")
  private String content;

  /**
   * 接收者ID
   */
  @TableField("receiver_id")
  private Long receiverId;

  /**
   * 是否已读
   */
  @TableField("is_read")
  private Boolean isRead;

  /**
   * 阅读日期
   */
  @TableField("read_date")
  private LocalDateTime readDate;

  /**
   * 是否删除
   */
  @TableField("is_deleted")
  private Boolean isDeleted;

  /**
   * 警报日期
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
