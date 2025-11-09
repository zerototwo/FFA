package com.isep.ffa.mapper;

import com.isep.ffa.entity.Embassy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Embassy Mapper Interface
 */
@Mapper
public interface EmbassyMapper extends CustomBaseMapper<Embassy> {

  /**
   * Find embassies by embassy country ID
   */
  @Select("SELECT * FROM embassy WHERE embassy_of_country_id = #{countryId} AND is_deleted = false")
  List<Embassy> findByEmbassyOfCountryId(Long countryId);

  /**
   * Find embassies by embassy location country ID
   */
  @Select("SELECT * FROM embassy WHERE embassy_in_country_id = #{countryId} AND is_deleted = false")
  List<Embassy> findByEmbassyInCountryId(Long countryId);

  /**
   * Find embassies by city ID
   */
  @Select("SELECT * FROM embassy WHERE city_id = #{cityId} AND is_deleted = false")
  List<Embassy> findByCityId(Long cityId);

  /**
   * Find embassies by address keyword
   */
  @Select("SELECT * FROM embassy WHERE address LIKE CONCAT('%', #{address}, '%') AND is_deleted = false")
  List<Embassy> findByAddressLike(String address);
}
