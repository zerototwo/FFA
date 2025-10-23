package com.isep.ffa.mapper;

import com.isep.ffa.entity.Alert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 警报Mapper接口
 */
@Mapper
public interface AlertMapper extends CustomBaseMapper<Alert> {

  /**
   * 根据接收者ID查询警报列表
   */
  @Select("SELECT * FROM alert WHERE receiver_id = #{receiverId} AND is_deleted = 0 ORDER BY alert_date DESC")
  List<Alert> findByReceiverId(Long receiverId);

  /**
   * 查询未读警报数量
   */
  @Select("SELECT COUNT(*) FROM alert WHERE receiver_id = #{receiverId} AND is_read = 0 AND is_deleted = 0")
  Integer countUnreadAlerts(Long receiverId);
}
