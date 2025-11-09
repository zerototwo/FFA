package com.isep.ffa.mapper;

import com.isep.ffa.entity.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * City Mapper Interface
 */
@Mapper
public interface CityMapper extends CustomBaseMapper<City> {

  /**
   * Find city by name
   */
  @Select("SELECT * FROM city WHERE name = #{name} AND is_deleted = false")
  City findByName(String name);

  /**
   * Find cities by department ID
   */
  @Select("SELECT * FROM city WHERE department_id = #{departmentId} AND is_deleted = false")
  List<City> findByDepartmentId(Long departmentId);

  /**
   * Find city by postal code
   */
  @Select("SELECT * FROM city WHERE postal_code = #{postalCode} AND is_deleted = false")
  City findByPostalCode(Integer postalCode);
}
