package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Message entity class
 * Database table: message
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
   * Message content
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
   * Read date
   */
  @TableField("read_date")
  private LocalDateTime readDate;

  /**
   * Reply message ID
   */
  @TableField("reply_to")
  private Long replyTo;

  /**
   * Is Deleted
   */
  @TableField("is_deleted")
  private Boolean isDeleted;

  // Related objects (non-persistent fields)
  /**
   * Sender information
   */
  @TableField(exist = false)
  private Person sender;

  /**
   * Receiver information
   */
  @TableField(exist = false)
  private Person receiver;

  /**
   * Reply message
   */
  @TableField(exist = false)
  private Message replyMessage;
}
