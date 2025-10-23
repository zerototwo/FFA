package com.isep.ffa.service;

import com.isep.ffa.entity.Alert;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Alert Service Interface
 * Provides business logic for alert management
 */
public interface AlertService extends BaseService<Alert> {

  /**
   * Find alerts by receiver ID
   * 
   * @param receiverId receiver ID
   * @return list of alerts
   */
  BaseResponse<List<Alert>> findByReceiverId(Long receiverId);

  /**
   * Count unread alerts for user
   * 
   * @param receiverId receiver ID
   * @return unread alert count
   */
  BaseResponse<Integer> countUnreadAlerts(Long receiverId);

  /**
   * Get paginated alerts by receiver
   * 
   * @param receiverId receiver ID
   * @param page       page number
   * @param size       page size
   * @return paginated alerts
   */
  BaseResponse<PagedResponse<Alert>> getAlertsByReceiver(Long receiverId, int page, int size);

  /**
   * Create new alert
   * 
   * @param alert alert information
   * @return created alert
   */
  BaseResponse<Alert> createAlert(Alert alert);

  /**
   * Send alert to user
   * 
   * @param receiverId receiver ID
   * @param content    alert content
   * @return created alert
   */
  BaseResponse<Alert> sendAlert(Long receiverId, String content);

  /**
   * Mark alert as read
   * 
   * @param alertId alert ID
   * @return operation result
   */
  BaseResponse<Boolean> markAsRead(Long alertId);

  /**
   * Mark all alerts as read for user
   * 
   * @param receiverId receiver ID
   * @return operation result
   */
  BaseResponse<Boolean> markAllAsRead(Long receiverId);

  /**
   * Delete alert by ID
   * 
   * @param id alert ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteAlert(Long id);

  /**
   * Get recent alerts for user
   * 
   * @param receiverId receiver ID
   * @param limit      number of alerts to return
   * @return list of recent alerts
   */
  BaseResponse<List<Alert>> getRecentAlerts(Long receiverId, int limit);
}
