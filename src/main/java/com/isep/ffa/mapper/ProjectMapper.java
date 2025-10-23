package com.isep.ffa.mapper;

import com.isep.ffa.entity.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 项目Mapper接口
 */
@Mapper
public interface ProjectMapper extends CustomBaseMapper<Project> {

  /**
   * 根据名称查询项目
   */
  @Select("SELECT * FROM project WHERE name = #{name} AND is_deleted = 0")
  Project findByName(String name);

  /**
   * 根据干预者ID查询项目列表
   */
  @Select("SELECT * FROM project WHERE intervener_id = #{intervenerId} AND is_deleted = 0")
  List<Project> findByIntervenerId(Long intervenerId);

  /**
   * 根据获胜用户ID查询项目列表
   */
  @Select("SELECT * FROM project WHERE winner_user_id = #{winnerUserId} AND is_deleted = 0")
  List<Project> findByWinnerUserId(Long winnerUserId);

  /**
   * 根据提交日期范围查询项目列表
   */
  @Select("SELECT * FROM project WHERE submission_date BETWEEN #{startDate} AND #{endDate} AND is_deleted = 0")
  List<Project> findBySubmissionDateRange(String startDate, String endDate);

  /**
   * 根据描述模糊查询项目列表
   */
  @Select("SELECT * FROM project WHERE description LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = 0")
  List<Project> findByDescriptionLike(String keyword);
}
