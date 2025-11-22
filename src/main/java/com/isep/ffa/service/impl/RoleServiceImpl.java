package com.isep.ffa.service.impl;

import com.isep.ffa.entity.Role;
import com.isep.ffa.mapper.RoleMapper;
import com.isep.ffa.service.RoleService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Role Service Implementation
 * Implements business logic for role management
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

  @Autowired
  private RoleMapper roleMapper;

  @Override
  public BaseResponse<Role> findByName(String name) {
    if (name == null || name.trim().isEmpty()) {
      return BaseResponse.error("Role name is required", 400);
    }
    Role role = roleMapper.selectOne(
        new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role>()
            .eq("name", name.trim())
            .eq("is_deleted", false));
    if (role == null) {
      return BaseResponse.error("Role not found with name: " + name, 404);
    }
    return BaseResponse.success("Role found", role);
  }

  @Override
  public BaseResponse<List<Role>> getAllRoles() {
    List<Role> roles = roleMapper.selectList(
        new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role>()
            .eq("is_deleted", false));
    if (roles == null || roles.isEmpty()) {
      return BaseResponse.success("No roles found", List.of());
    }
    return BaseResponse.success("Roles retrieved successfully", roles);
  }

  @Override
  public BaseResponse<Role> createRole(Role role) {
    if (role == null || role.getName() == null || role.getName().trim().isEmpty()) {
      return BaseResponse.error("Role name is required", 400);
    }
    // Check if role already exists
    Role existing = roleMapper.selectOne(
        new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role>()
            .eq("name", role.getName().trim())
            .eq("is_deleted", false));
    if (existing != null) {
      return BaseResponse.error("Role with name '" + role.getName() + "' already exists", 409);
    }
    // Role entity doesn't have isDeleted field, skip setting it
    boolean saved = save(role);
    if (!saved) {
      return BaseResponse.error("Failed to create role", 500);
    }
    return BaseResponse.success("Role created successfully", role);
  }

  @Override
  public BaseResponse<Role> updateRole(Role role) {
    if (role == null || role.getId() == null) {
      return BaseResponse.error("Role ID is required for update", 400);
    }
    Role existing = getById(role.getId());
    if (existing == null) {
      return BaseResponse.error("Role not found with ID: " + role.getId(), 404);
    }
    // Check if name is being changed and if new name already exists
    if (role.getName() != null && !role.getName().equals(existing.getName())) {
      Role nameExists = roleMapper.selectOne(
          new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role>()
              .eq("name", role.getName().trim())
              .ne("id", role.getId())
              .eq("is_deleted", false));
      if (nameExists != null) {
        return BaseResponse.error("Role with name '" + role.getName() + "' already exists", 409);
      }
    }
    boolean updated = updateById(role);
    if (!updated) {
      return BaseResponse.error("Failed to update role", 500);
    }
    Role refreshed = getById(role.getId());
    return BaseResponse.success("Role updated successfully", refreshed);
  }

  @Override
  public BaseResponse<Boolean> deleteRole(Long id) {
    if (id == null) {
      return BaseResponse.error("Role ID is required", 400);
    }
    Role role = getById(id);
    if (role == null) {
      return BaseResponse.error("Role not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete role", 500);
    }
    return BaseResponse.success("Role deleted successfully", true);
  }

  @Override
  public BaseResponse<Boolean> roleExists(String name) {
    if (name == null || name.trim().isEmpty()) {
      return BaseResponse.error("Role name is required", 400);
    }
    Role role = roleMapper.selectOne(
        new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Role>()
            .eq("name", name.trim())
            .eq("is_deleted", false));
    boolean exists = role != null;
    return BaseResponse.success("Role existence checked", exists);
  }
}
