package com.isep.ffa.mapper;

import com.isep.ffa.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Message Mapper Interface
 */
@Mapper
public interface MessageMapper extends CustomBaseMapper<Message> {

  /**
   * Find messages by sender ID
   */
  @Select("SELECT * FROM message WHERE sender_id = #{senderId} AND is_deleted = 0 ORDER BY create_date DESC")
  List<Message> findBySenderId(Long senderId);

  /**
   * Find messages by receiver ID
   */
  @Select("SELECT * FROM message WHERE receiver_id = #{receiverId} AND is_deleted = 0 ORDER BY create_date DESC")
  List<Message> findByReceiverId(Long receiverId);

  /**
   * Find messages by sender and receiver
   */
  @Select("SELECT * FROM message WHERE (sender_id = #{senderId} AND receiver_id = #{receiverId}) OR (sender_id = #{receiverId} AND receiver_id = #{senderId}) AND is_deleted = 0 ORDER BY create_date ASC")
  List<Message> findBySenderAndReceiver(Long senderId, Long receiverId);

  /**
   * Count unread messages
   */
  @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{receiverId} AND is_read = 0 AND is_deleted = 0")
  Integer countUnreadMessages(Long receiverId);
}
