package com.isep.ffa.mapper;

import com.isep.ffa.entity.Embassy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 大使馆Mapper接口
 */
@Mapper
public interface EmbassyMapper extends CustomBaseMapper<Embassy> {

  /**
   * 根据所属国家查询大使馆列表
   */
  @Select("SELECT * FROM embassy WHERE embassy_of_country_id = #{countryId} AND is_deleted = 0")
  List<Embassy> findByEmbassyOfCountryId(Long countryId);

  /**
   * 根据所在国家查询大使馆列表
   */
  @Select("SELECT * FROM embassy WHERE embassy_in_country_id = #{countryId} AND is_deleted = 0")
  List<Embassy> findByEmbassyInCountryId(Long countryId);

  /**
   * 根据城市查询大使馆列表
   */
  @Select("SELECT * FROM embassy WHERE city_id = #{cityId} AND is_deleted = 0")
  List<Embassy> findByCityId(Long cityId);

  /**
   * 根据地址模糊查询大使馆列表
   */
  @Select("SELECT * FROM embassy WHERE address LIKE CONCAT('%', #{address}, '%') AND is_deleted = 0")
  List<Embassy> findByAddressLike(String address);
}
