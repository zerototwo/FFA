package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Message实体类
 * Database table：message
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("message")
public class Message {

  /**
   * Primary Key ID
   */
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * Message内容
   */
  @TableField("content")
  private String content;

  /**
   * Sender ID
   */
  @TableField("sender_id")
  private Long senderId;

  /**
   * Receiver ID
   */
  @TableField("receiver_id")
  private Long receiverId;

  /**
   * Creation Date
   */
  @TableField("create_date")
  private LocalDateTime createDate;

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
   * 回复MessageID
   */
  @TableField("reply_to")
  private Long replyTo;

  /**
   * Is Deleted
   */
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * 发送者信息
   */
  @TableField(exist = false)
  private Person sender;

  /**
   * 接收者信息
   */
  @TableField(exist = false)
  private Person receiver;

  /**
   * 回复的Message
   */
  @TableField(exist = false)
  private Message replyMessage;
}
