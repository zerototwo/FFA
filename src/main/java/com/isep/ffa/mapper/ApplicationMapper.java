package com.isep.ffa.mapper;

import com.isep.ffa.entity.Application;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 申请Mapper接口
 */
@Mapper
public interface ApplicationMapper extends CustomBaseMapper<Application> {

  /**
   * 根据项目ID查询申请列表
   */
  @Select("SELECT * FROM application WHERE project_id = #{projectId} AND is_deleted = 0")
  List<Application> findByProjectId(Long projectId);

  /**
   * 根据用户ID查询申请列表
   */
  @Select("SELECT * FROM application WHERE user_id = #{userId} AND is_deleted = 0")
  List<Application> findByUserId(Long userId);

  /**
   * 根据申请日期范围查询申请列表
   */
  @Select("SELECT * FROM application WHERE date_application BETWEEN #{startDate} AND #{endDate} AND is_deleted = 0")
  List<Application> findByDateApplicationRange(String startDate, String endDate);

  /**
   * 根据动机模糊查询申请列表
   */
  @Select("SELECT * FROM application WHERE motivation LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = 0")
  List<Application> findByMotivationLike(String keyword);
}
