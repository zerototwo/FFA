package com.isep.ffa.mapper;

import com.isep.ffa.entity.Person;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 人员Mapper接口
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {

  /**
   * 根据邮箱查询用户
   */
  @Select("SELECT * FROM person WHERE email = #{email} AND is_deleted = 0")
  Person findByEmail(String email);

  /**
   * 根据登录名查询用户
   */
  @Select("SELECT * FROM person WHERE login = #{login} AND is_deleted = 0")
  Person findByLogin(String login);

  /**
   * 根据角色查询用户列表
   */
  @Select("SELECT * FROM person WHERE role_id = #{roleId} AND is_deleted = 0")
  List<Person> findByRoleId(Long roleId);

  /**
   * 根据城市查询用户列表
   */
  @Select("SELECT * FROM person WHERE city_id = #{cityId} AND is_deleted = 0")
  List<Person> findByCityId(Long cityId);
}
