package com.isep.ffa.service;

import com.isep.ffa.entity.Role;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Role Service Interface
 * Provides business logic for role management
 */
public interface RoleService extends BaseService<Role> {

  /**
   * Find role by name
   * 
   * @param name role name
   * @return role information
   */
  BaseResponse<Role> findByName(String name);

  /**
   * Get all roles
   * 
   * @return list of all roles
   */
  BaseResponse<List<Role>> getAllRoles();

  /**
   * Create new role
   * 
   * @param role role information
   * @return created role
   */
  BaseResponse<Role> createRole(Role role);

  /**
   * Update role information
   * 
   * @param role role information
   * @return updated role
   */
  BaseResponse<Role> updateRole(Role role);

  /**
   * Delete role by ID
   * 
   * @param id role ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteRole(Long id);

  /**
   * Check if role exists
   * 
   * @param name role name
   * @return role exists or not
   */
  BaseResponse<Boolean> roleExists(String name);
}
