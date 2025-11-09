package com.isep.ffa.mapper;

import com.isep.ffa.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Person Mapper Interface
 */
@Mapper
public interface PersonMapper extends CustomBaseMapper<Person> {

  /**
   * Find user by email
   */
  @Select("SELECT * FROM person WHERE email = #{email} AND is_deleted = false")
  Person findByEmail(String email);

  /**
   * Find user by login name
   */
  @Select("SELECT * FROM person WHERE login = #{login} AND is_deleted = false")
  Person findByLogin(String login);

  /**
   * Find users by role ID
   */
  @Select("SELECT * FROM person WHERE role_id = #{roleId} AND is_deleted = false")
  List<Person> findByRoleId(Long roleId);

  /**
   * Find users by city ID
   */
  @Select("SELECT * FROM person WHERE city_id = #{cityId} AND is_deleted = false")
  List<Person> findByCityId(Long cityId);
}
