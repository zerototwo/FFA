package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.Message;
import com.isep.ffa.mapper.MessageMapper;
import com.isep.ffa.service.MessageService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Message Service Implementation
 * Implements business logic for message management
 */
@Service
public class MessageServiceImpl extends BaseServiceImpl<MessageMapper, Message> implements MessageService {

  @Autowired
  private MessageMapper messageMapper;

  private int normalizePage(int page) {
    return Math.max(page, 1);
  }

  private int normalizeSize(int size) {
    return size <= 0 ? 10 : size;
  }

  private PagedResponse<Message> toPagedResponse(Page<Message> pageResult) {
    int zeroBasedPage = (int) pageResult.getCurrent() - 1;
    zeroBasedPage = Math.max(zeroBasedPage, 0);
    return PagedResponse.of(
        pageResult.getRecords(),
        zeroBasedPage,
        (int) pageResult.getSize(),
        pageResult.getTotal(),
        (int) pageResult.getPages());
  }

  @Override
  public BaseResponse<List<Message>> findBySenderId(Long senderId) {
    if (senderId == null) {
      return BaseResponse.error("Sender ID is required", 400);
    }
    List<Message> messages = messageMapper.findBySenderId(senderId);
    return BaseResponse.success("Messages retrieved successfully", messages);
  }

  @Override
  public BaseResponse<List<Message>> findByReceiverId(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    List<Message> messages = messageMapper.findByReceiverId(receiverId);
    return BaseResponse.success("Messages retrieved successfully", messages);
  }

  @Override
  public BaseResponse<List<Message>> findConversation(Long senderId, Long receiverId) {
    if (senderId == null || receiverId == null) {
      return BaseResponse.error("Sender ID and Receiver ID are required", 400);
    }
    List<Message> messages = messageMapper.findBySenderAndReceiver(senderId, receiverId);
    return BaseResponse.success("Conversation retrieved successfully", messages);
  }

  @Override
  public BaseResponse<Integer> countUnreadMessages(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    Integer count = messageMapper.countUnreadMessages(receiverId);
    return BaseResponse.success("Unread count retrieved", count != null ? count : 0);
  }

  @Override
  public BaseResponse<PagedResponse<Message>> getMessagesBySender(Long senderId, int page, int size) {
    if (senderId == null) {
      return BaseResponse.error("Sender ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    QueryWrapper<Message> wrapper = new QueryWrapper<>();
    wrapper.eq("sender_id", senderId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("create_date");
    
    Page<Message> pageRequest = new Page<>(safePage, safeSize);
    Page<Message> result = page(pageRequest, wrapper);
    
    return BaseResponse.success("Messages retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<PagedResponse<Message>> getMessagesByReceiver(Long receiverId, int page, int size) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    QueryWrapper<Message> wrapper = new QueryWrapper<>();
    wrapper.eq("receiver_id", receiverId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("create_date");
    
    Page<Message> pageRequest = new Page<>(safePage, safeSize);
    Page<Message> result = page(pageRequest, wrapper);
    
    return BaseResponse.success("Messages retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<Message> createMessage(Message message) {
    if (message == null) {
      return BaseResponse.error("Message data is required", 400);
    }
    if (message.getSenderId() == null) {
      return BaseResponse.error("Sender ID is required", 400);
    }
    if (message.getReceiverId() == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    if (StringUtils.isBlank(message.getContent())) {
      return BaseResponse.error("Message content is required", 400);
    }
    if (message.getCreateDate() == null) {
      message.setCreateDate(LocalDateTime.now());
    }
    if (message.getIsRead() == null) {
      message.setIsRead(false);
    }
    boolean saved = save(message);
    if (!saved) {
      return BaseResponse.error("Failed to create message", 500);
    }
    return BaseResponse.success("Message created successfully", message);
  }

  @Override
  public BaseResponse<Message> sendMessage(Long senderId, Long receiverId, String content) {
    if (senderId == null) {
      return BaseResponse.error("Sender ID is required", 400);
    }
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    if (StringUtils.isBlank(content)) {
      return BaseResponse.error("Message content is required", 400);
    }
    Message message = new Message();
    message.setSenderId(senderId);
    message.setReceiverId(receiverId);
    message.setContent(content.trim());
    message.setCreateDate(LocalDateTime.now());
    message.setIsRead(false);
    return createMessage(message);
  }

  @Override
  public BaseResponse<Message> replyToMessage(Long originalMessageId, Long senderId, String content) {
    if (originalMessageId == null) {
      return BaseResponse.error("Original message ID is required", 400);
    }
    if (senderId == null) {
      return BaseResponse.error("Sender ID is required", 400);
    }
    if (StringUtils.isBlank(content)) {
      return BaseResponse.error("Reply content is required", 400);
    }
    Message originalMessage = getById(originalMessageId);
    if (originalMessage == null || Boolean.TRUE.equals(originalMessage.getIsDeleted())) {
      return BaseResponse.error("Original message not found", 404);
    }
    // Determine receiver: if sender is replying, receiver is the original sender
    Long receiverId = originalMessage.getSenderId().equals(senderId) 
        ? originalMessage.getReceiverId() 
        : originalMessage.getSenderId();
    
    Message reply = new Message();
    reply.setSenderId(senderId);
    reply.setReceiverId(receiverId);
    reply.setContent(content.trim());
    reply.setReplyTo(originalMessageId);
    reply.setCreateDate(LocalDateTime.now());
    reply.setIsRead(false);
    
    return createMessage(reply);
  }

  @Override
  public BaseResponse<Boolean> markAsRead(Long messageId) {
    if (messageId == null) {
      return BaseResponse.error("Message ID is required", 400);
    }
    Message message = getById(messageId);
    if (message == null || Boolean.TRUE.equals(message.getIsDeleted())) {
      return BaseResponse.error("Message not found with ID: " + messageId, 404);
    }
    UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("id", messageId);
    updateWrapper.set("is_read", true);
    updateWrapper.set("read_date", LocalDateTime.now());
    boolean updated = update(updateWrapper);
    if (!updated) {
      return BaseResponse.error("Failed to mark message as read", 500);
    }
    return BaseResponse.success("Message marked as read", true);
  }

  @Override
  public BaseResponse<Boolean> markAllAsRead(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    UpdateWrapper<Message> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("receiver_id", receiverId);
    updateWrapper.eq("is_read", false);
    updateWrapper.eq("is_deleted", false);
    updateWrapper.set("is_read", true);
    updateWrapper.set("read_date", LocalDateTime.now());
    boolean updated = update(updateWrapper);
    return BaseResponse.success("All messages marked as read", updated);
  }

  @Override
  public BaseResponse<Boolean> deleteMessage(Long id) {
    if (id == null) {
      return BaseResponse.error("Message ID is required", 400);
    }
    Message message = getById(id);
    if (message == null || Boolean.TRUE.equals(message.getIsDeleted())) {
      return BaseResponse.error("Message not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete message", 500);
    }
    return BaseResponse.success("Message deleted successfully", true);
  }
}
