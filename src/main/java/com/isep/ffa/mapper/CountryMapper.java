package com.isep.ffa.mapper;

import com.isep.ffa.entity.Country;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Country Mapper Interface
 */
@Mapper
public interface CountryMapper extends CustomBaseMapper<Country> {

  /**
   * Find country by name
   */
  @Select("SELECT * FROM country WHERE name = #{name} AND is_deleted = false")
  Country findByName(String name);

  /**
   * Find country by phone number indicator
   */
  @Select("SELECT * FROM country WHERE phone_number_indicator = #{phoneIndicator} AND is_deleted = false")
  Country findByPhoneIndicator(String phoneIndicator);

  /**
   * Find countries by continent ID
   */
  @Select("SELECT * FROM country WHERE continent_id = #{continentId} AND is_deleted = false")
  List<Country> findByContinentId(Long continentId);
}
