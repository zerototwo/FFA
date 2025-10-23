package com.isep.ffa.mapper;

import com.isep.ffa.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 城市Mapper接口
 */
@Mapper
public interface CityMapper extends BaseMapper<City> {

  /**
   * 根据名称查询城市
   */
  @Select("SELECT * FROM city WHERE name = #{name} AND is_deleted = 0")
  City findByName(String name);

  /**
   * 根据部门ID查询城市列表
   */
  @Select("SELECT * FROM city WHERE department_id = #{departmentId} AND is_deleted = 0")
  List<City> findByDepartmentId(Long departmentId);

  /**
   * 根据邮政编码查询城市
   */
  @Select("SELECT * FROM city WHERE postal_code = #{postalCode} AND is_deleted = 0")
  City findByPostalCode(Integer postalCode);
}
