package com.isep.ffa.mapper;

import com.isep.ffa.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Project Mapper Interface
 */
@Mapper
public interface ProjectMapper extends CustomBaseMapper<Project> {

  /**
   * Find project by name
   */
  @Select("SELECT * FROM project WHERE name = #{name} AND is_deleted = false")
  Project findByName(String name);

  /**
   * Find projects by intervener ID
   */
  @Select("SELECT * FROM project WHERE intervener_id = #{intervenerId} AND is_deleted = false")
  List<Project> findByIntervenerId(Long intervenerId);

  /**
   * Find projects by winner user ID
   */
  @Select("SELECT * FROM project WHERE winner_user_id = #{winnerUserId} AND is_deleted = false")
  List<Project> findByWinnerUserId(Long winnerUserId);

  /**
   * Find projects by submission date range
   */
  @Select("SELECT * FROM project WHERE submission_date BETWEEN #{startDate} AND #{endDate} AND is_deleted = false")
  List<Project> findBySubmissionDateRange(String startDate, String endDate);

  /**
   * Find projects by description keyword
   */
  @Select("SELECT * FROM project WHERE LOWER(description) LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = false")
  List<Project> findByDescriptionLike(String keyword);

  /**
   * Count projects by intervener ID
   */
  @Select("SELECT COUNT(*) FROM project WHERE intervener_id = #{intervenerId} AND is_deleted = false")
  Long countByIntervenerId(Long intervenerId);

  /**
   * Count projects created this month by intervener ID
   */
  @Select("SELECT COUNT(*) FROM project WHERE intervener_id = #{intervenerId} AND is_deleted = false AND DATE_TRUNC('month', creation_date) = DATE_TRUNC('month', CURRENT_DATE)")
  Long countByIntervenerIdThisMonth(Long intervenerId);

  /**
   * Count projects with PENDING_APPROVAL status by intervener ID
   */
  @Select("SELECT COUNT(*) FROM project WHERE intervener_id = #{intervenerId} AND status = 'PENDING_APPROVAL' AND is_deleted = false")
  Long countPendingApprovalsByIntervenerId(Long intervenerId);

  /**
   * Find projects by intervener ID and status
   */
  @Select("SELECT * FROM project WHERE intervener_id = #{intervenerId} AND status = #{status} AND is_deleted = false ORDER BY creation_date DESC")
  List<Project> findByIntervenerIdAndStatus(Long intervenerId, String status);

  /**
   * Count applications by project ID
   */
  @Select("SELECT COUNT(*) FROM application WHERE project_id = #{projectId} AND is_deleted = false")
  Long countApplicationsByProjectId(Long projectId);
}
