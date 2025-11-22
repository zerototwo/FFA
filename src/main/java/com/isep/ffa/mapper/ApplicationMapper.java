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
  @Select("SELECT * FROM application WHERE LOWER(motivation) LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = false")
  List<Application> findByMotivationLike(String keyword);

  /**
   * Count applications for projects by intervener ID
   */
  @Select("SELECT COUNT(*) FROM application a " +
      "INNER JOIN project p ON a.project_id = p.id " +
      "WHERE p.intervener_id = #{intervenerId} AND a.is_deleted = false AND p.is_deleted = false")
  Long countApplicationsForIntervenerProjects(Long intervenerId);

  /**
   * Count applications created this week for projects by intervener ID
   */
  @Select("SELECT COUNT(*) FROM application a " +
      "INNER JOIN project p ON a.project_id = p.id " +
      "WHERE p.intervener_id = #{intervenerId} AND a.is_deleted = false AND p.is_deleted = false " +
      "AND DATE_TRUNC('week', a.date_application) = DATE_TRUNC('week', CURRENT_DATE)")
  Long countApplicationsForIntervenerProjectsThisWeek(Long intervenerId);

  /**
   * Find applications for projects by intervener ID with project details
   */
  @Select("SELECT a.* FROM application a " +
      "INNER JOIN project p ON a.project_id = p.id " +
      "WHERE p.intervener_id = #{intervenerId} AND a.is_deleted = false AND p.is_deleted = false " +
      "ORDER BY a.date_application DESC LIMIT #{limit}")
  List<Application> findRecentApplicationsForIntervener(Long intervenerId, Integer limit);
}
