package com.isep.ffa.service;

import com.isep.ffa.entity.Message;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Message Service Interface
 * Provides business logic for message management
 */
public interface MessageService extends BaseService<Message> {

  /**
   * Find messages by sender ID
   * 
   * @param senderId sender ID
   * @return list of messages
   */
  BaseResponse<List<Message>> findBySenderId(Long senderId);

  /**
   * Find messages by receiver ID
   * 
   * @param receiverId receiver ID
   * @return list of messages
   */
  BaseResponse<List<Message>> findByReceiverId(Long receiverId);

  /**
   * Find conversation between two users
   * 
   * @param senderId   sender ID
   * @param receiverId receiver ID
   * @return list of messages
   */
  BaseResponse<List<Message>> findConversation(Long senderId, Long receiverId);

  /**
   * Count unread messages for user
   * 
   * @param receiverId receiver ID
   * @return unread message count
   */
  BaseResponse<Integer> countUnreadMessages(Long receiverId);

  /**
   * Get paginated messages by sender
   * 
   * @param senderId sender ID
   * @param page     page number
   * @param size     page size
   * @return paginated messages
   */
  BaseResponse<PagedResponse<Message>> getMessagesBySender(Long senderId, int page, int size);

  /**
   * Get paginated messages by receiver
   * 
   * @param receiverId receiver ID
   * @param page       page number
   * @param size       page size
   * @return paginated messages
   */
  BaseResponse<PagedResponse<Message>> getMessagesByReceiver(Long receiverId, int page, int size);

  /**
   * Create new message
   * 
   * @param message message information
   * @return created message
   */
  BaseResponse<Message> createMessage(Message message);

  /**
   * Send message to user
   * 
   * @param senderId   sender ID
   * @param receiverId receiver ID
   * @param content    message content
   * @return created message
   */
  BaseResponse<Message> sendMessage(Long senderId, Long receiverId, String content);

  /**
   * Reply to message
   * 
   * @param originalMessageId original message ID
   * @param senderId          sender ID
   * @param content           reply content
   * @return created reply message
   */
  BaseResponse<Message> replyToMessage(Long originalMessageId, Long senderId, String content);

  /**
   * Mark message as read
   * 
   * @param messageId message ID
   * @return operation result
   */
  BaseResponse<Boolean> markAsRead(Long messageId);

  /**
   * Mark all messages as read for user
   * 
   * @param receiverId receiver ID
   * @return operation result
   */
  BaseResponse<Boolean> markAllAsRead(Long receiverId);

  /**
   * Delete message by ID
   * 
   * @param id message ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteMessage(Long id);
}
