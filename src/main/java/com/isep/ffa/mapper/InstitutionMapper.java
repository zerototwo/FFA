package com.isep.ffa.mapper;

import com.isep.ffa.entity.Institution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Institution Mapper Interface
 */
@Mapper
public interface InstitutionMapper extends CustomBaseMapper<Institution> {

  /**
   * Find institutions by city ID
   */
  @Select("SELECT * FROM institution WHERE city_id = #{cityId} AND is_deleted = false")
  List<Institution> findByCityId(Long cityId);

  /**
   * Search institutions by name keyword
   */
  @Select("SELECT * FROM institution WHERE name LIKE CONCAT('%', #{keyword}, '%') AND is_deleted = false")
  List<Institution> findByNameLike(String keyword);
}

