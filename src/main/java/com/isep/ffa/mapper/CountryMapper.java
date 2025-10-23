package com.isep.ffa.mapper;

import com.isep.ffa.entity.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 国家Mapper接口
 */
@Mapper
public interface CountryMapper extends CustomBaseMapper<Country> {

  /**
   * 根据名称查询国家
   */
  @Select("SELECT * FROM country WHERE name = #{name} AND is_deleted = false")
  Country findByName(String name);

  /**
   * 根据电话区号查询国家
   */
  @Select("SELECT * FROM country WHERE phone_number_indicator = #{phoneIndicator} AND is_deleted = false")
  Country findByPhoneIndicator(String phoneIndicator);

  /**
   * 根据大陆ID查询国家列表
   */
  @Select("SELECT * FROM country WHERE continent_id = #{continentId} AND is_deleted = false")
  List<Country> findByContinentId(Long continentId);
}
