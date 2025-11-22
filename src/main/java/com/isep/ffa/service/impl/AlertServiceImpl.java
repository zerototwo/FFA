package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.Alert;
import com.isep.ffa.mapper.AlertMapper;
import com.isep.ffa.service.AlertService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Alert Service Implementation
 * Implements business logic for alert management
 */
@Service
public class AlertServiceImpl extends BaseServiceImpl<AlertMapper, Alert> implements AlertService {

  @Autowired
  private AlertMapper alertMapper;

  private int normalizePage(int page) {
    return Math.max(page, 1);
  }

  private int normalizeSize(int size) {
    return size <= 0 ? 10 : size;
  }

  private PagedResponse<Alert> toPagedResponse(Page<Alert> pageResult) {
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
  public BaseResponse<List<Alert>> findByReceiverId(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    QueryWrapper<Alert> wrapper = new QueryWrapper<>();
    wrapper.eq("receiver_id", receiverId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("alert_date");
    List<Alert> alerts = list(wrapper);
    return BaseResponse.success("Alerts retrieved successfully", alerts);
  }

  @Override
  public BaseResponse<Integer> countUnreadAlerts(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    QueryWrapper<Alert> wrapper = new QueryWrapper<>();
    wrapper.eq("receiver_id", receiverId);
    wrapper.and(w -> w.eq("is_read", false).or().isNull("is_read"));
    wrapper.eq("is_deleted", false);
    long count = count(wrapper);
    return BaseResponse.success("Unread count retrieved", (int) count);
  }

  @Override
  public BaseResponse<PagedResponse<Alert>> getAlertsByReceiver(Long receiverId, int page, int size) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    
    QueryWrapper<Alert> wrapper = new QueryWrapper<>();
    wrapper.eq("receiver_id", receiverId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("alert_date");
    
    Page<Alert> pageRequest = new Page<>(safePage, safeSize);
    Page<Alert> result = page(pageRequest, wrapper);
    
    return BaseResponse.success("Alerts retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<Alert> createAlert(Alert alert) {
    if (alert == null) {
      return BaseResponse.error("Alert data is required", 400);
    }
    if (alert.getReceiverId() == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    if (StringUtils.isBlank(alert.getContent())) {
      return BaseResponse.error("Alert content is required", 400);
    }
    if (alert.getAlertDate() == null) {
      alert.setAlertDate(LocalDateTime.now());
    }
    if (alert.getIsRead() == null) {
      alert.setIsRead(false);
    }
    boolean saved = save(alert);
    if (!saved) {
      return BaseResponse.error("Failed to create alert", 500);
    }
    return BaseResponse.success("Alert created successfully", alert);
  }

  @Override
  public BaseResponse<Alert> sendAlert(Long receiverId, String content) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    if (StringUtils.isBlank(content)) {
      return BaseResponse.error("Alert content is required", 400);
    }
    Alert alert = new Alert();
    alert.setReceiverId(receiverId);
    alert.setContent(content.trim());
    alert.setAlertDate(LocalDateTime.now());
    alert.setIsRead(false);
    return createAlert(alert);
  }

  @Override
  public BaseResponse<Boolean> markAsRead(Long alertId) {
    if (alertId == null) {
      return BaseResponse.error("Alert ID is required", 400);
    }
    Alert alert = getById(alertId);
    if (alert == null || Boolean.TRUE.equals(alert.getIsDeleted())) {
      return BaseResponse.error("Alert not found with ID: " + alertId, 404);
    }
    UpdateWrapper<Alert> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("id", alertId);
    updateWrapper.set("is_read", true);
    updateWrapper.set("read_date", LocalDateTime.now());
    boolean updated = update(updateWrapper);
    if (!updated) {
      return BaseResponse.error("Failed to mark alert as read", 500);
    }
    return BaseResponse.success("Alert marked as read", true);
  }

  @Override
  public BaseResponse<Boolean> markAllAsRead(Long receiverId) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    UpdateWrapper<Alert> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("receiver_id", receiverId);
    updateWrapper.and(w -> w.eq("is_read", false).or().isNull("is_read"));
    updateWrapper.eq("is_deleted", false);
    updateWrapper.set("is_read", true);
    updateWrapper.set("read_date", LocalDateTime.now());
    boolean updated = update(updateWrapper);
    return BaseResponse.success("All alerts marked as read", updated);
  }

  @Override
  public BaseResponse<Boolean> deleteAlert(Long id) {
    if (id == null) {
      return BaseResponse.error("Alert ID is required", 400);
    }
    Alert alert = getById(id);
    if (alert == null || Boolean.TRUE.equals(alert.getIsDeleted())) {
      return BaseResponse.error("Alert not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete alert", 500);
    }
    return BaseResponse.success("Alert deleted successfully", true);
  }

  @Override
  public BaseResponse<List<Alert>> getRecentAlerts(Long receiverId, int limit) {
    if (receiverId == null) {
      return BaseResponse.error("Receiver ID is required", 400);
    }
    int safeLimit = limit <= 0 ? 10 : limit;
    QueryWrapper<Alert> wrapper = new QueryWrapper<>();
    wrapper.eq("receiver_id", receiverId);
    wrapper.eq("is_deleted", false);
    wrapper.orderByDesc("alert_date");
    wrapper.last("LIMIT " + safeLimit);
    List<Alert> alerts = list(wrapper);
    return BaseResponse.success("Recent alerts retrieved", alerts);
  }
}
