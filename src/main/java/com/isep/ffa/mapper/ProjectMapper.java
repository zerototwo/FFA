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
  @Select("SELECT * FROM project WHERE name = #{name} AND is_deleted = 0")
  Project findByName(String name);

  /**
   * Find projects by intervener ID
   */
  @Select("SELECT * FROM project WHERE intervener_id = #{intervenerId} AND is_deleted = 0")
  List<Project> findByIntervenerId(Long intervenerId);

  /**
   * Find projects by winner user ID
   */
  @Select("SELECT * FROM project WHERE winner_user_id = #{winnerUserId} AND is_deleted = 0")
  List<Project> findByWinnerUserId(Long winnerUserId);

  /**
   * Find projects by submission date range
   */
  @Select("SELECT * FROM project WHERE submission_date BETWEEN #{startDate} AND #{endDate} AND is_deleted = 0")
  List<Project> findBySubmissionDateRange(String startDate, String endDate);

  /**
   * Find projects by description keyword
   */
  @Select("SELECT * FROM project WHERE description LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = 0")
  List<Project> findByDescriptionLike(String keyword);
}
