package com.isep.ffa.mapper;

import com.isep.ffa.entity.Alert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Alert Mapper Interface
 */
@Mapper
public interface AlertMapper extends CustomBaseMapper<Alert> {

  /**
   * Find alerts by receiver ID
   */
  @Select("SELECT * FROM alert WHERE receiver_id = #{receiverId} AND is_deleted = false ORDER BY alert_date DESC")
  List<Alert> findByReceiverId(Long receiverId);

  /**
   * Count unread alerts
   */
  @Select("SELECT COUNT(*) FROM alert WHERE receiver_id = #{receiverId} AND (is_read = false OR is_read IS NULL) AND is_deleted = false")
  Integer countUnreadAlerts(Long receiverId);
}
