package com.isep.ffa.mapper;

import com.isep.ffa.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 消息Mapper接口
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

  /**
   * 根据发送者ID查询消息列表
   */
  @Select("SELECT * FROM message WHERE sender_id = #{senderId} AND is_deleted = 0 ORDER BY create_date DESC")
  List<Message> findBySenderId(Long senderId);

  /**
   * 根据接收者ID查询消息列表
   */
  @Select("SELECT * FROM message WHERE receiver_id = #{receiverId} AND is_deleted = 0 ORDER BY create_date DESC")
  List<Message> findByReceiverId(Long receiverId);

  /**
   * 根据发送者和接收者查询消息列表
   */
  @Select("SELECT * FROM message WHERE (sender_id = #{senderId} AND receiver_id = #{receiverId}) OR (sender_id = #{receiverId} AND receiver_id = #{senderId}) AND is_deleted = 0 ORDER BY create_date ASC")
  List<Message> findBySenderAndReceiver(Long senderId, Long receiverId);

  /**
   * 查询未读消息数量
   */
  @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{receiverId} AND is_read = 0 AND is_deleted = 0")
  Integer countUnreadMessages(Long receiverId);
}
