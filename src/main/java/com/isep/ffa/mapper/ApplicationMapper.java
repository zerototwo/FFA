package com.isep.ffa.mapper;

import com.isep.ffa.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Application Mapper Interface
 */
@Mapper
public interface ApplicationMapper extends CustomBaseMapper<Application> {

  /**
   * Find applications by project ID
   */
  @Select("SELECT * FROM application WHERE project_id = #{projectId} AND is_deleted = 0")
  List<Application> findByProjectId(Long projectId);

  /**
   * Find applications by user ID
   */
  @Select("SELECT * FROM application WHERE user_id = #{userId} AND is_deleted = 0")
  List<Application> findByUserId(Long userId);

  /**
   * Find applications by application date range
   */
  @Select("SELECT * FROM application WHERE date_application BETWEEN #{startDate} AND #{endDate} AND is_deleted = 0")
  List<Application> findByDateApplicationRange(String startDate, String endDate);

  /**
   * Find applications by motivation keyword
   */
  @Select("SELECT * FROM application WHERE motivation LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = 0")
  List<Application> findByMotivationLike(String keyword);
}
