package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.mapper.CustomBaseMapper;
import com.isep.ffa.service.BaseService;

import java.util.List;

/**
 * Custom BaseService Implementation Class
 * 
 * @param <M> Mapper type
 * @param <T> Entity type
 */
public abstract class BaseServiceImpl<M extends CustomBaseMapper<T>, T> extends ServiceImpl<M, T>
    implements BaseService<T> {

  @Override
  public PagedResponse<T> getPage(int page, int size) {
    Page<T> pageParam = new Page<>(page, size);
    Page<T> result = page(pageParam);
    return PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
  }

  @Override
  public PagedResponse<T> getPage(int page, int size, String sortBy, String sortDir) {
    Page<T> pageParam = new Page<>(page, size);
    QueryWrapper<T> queryWrapper = new QueryWrapper<>();

    // Implement sorting functionality
    if (sortBy != null && !sortBy.trim().isEmpty()) {
      // Convert camelCase to snake_case for database column names
      String columnName = convertCamelToSnake(sortBy.trim());
      
      // Determine sort direction (default to ASC if not specified or invalid)
      boolean isAsc = true;
      if (sortDir != null && !sortDir.trim().isEmpty()) {
        String dir = sortDir.trim().toUpperCase();
        isAsc = "ASC".equals(dir);
      }
      
      // Apply sorting
      if (isAsc) {
        queryWrapper.orderByAsc(columnName);
      } else {
        queryWrapper.orderByDesc(columnName);
      }
    } else {
      // Default sorting by ID descending (newest first)
      queryWrapper.orderByDesc("id");
    }

    Page<T> result = page(pageParam, queryWrapper);
    return PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
  }

  /**
   * Convert camelCase to snake_case
   * Example: "creationDate" -> "creation_date"
   */
  private String convertCamelToSnake(String camelCase) {
    if (camelCase == null || camelCase.isEmpty()) {
      return camelCase;
    }
    // Simple conversion: insert underscore before uppercase letters
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < camelCase.length(); i++) {
      char c = camelCase.charAt(i);
      if (Character.isUpperCase(c) && i > 0) {
        result.append('_');
      }
      result.append(Character.toLowerCase(c));
    }
    return result.toString();
  }

  @Override
  public List<T> getByIds(List<Long> ids) {
    return listByIds(ids);
  }

  @Override
  public boolean deleteByIds(List<Long> ids) {
    return removeByIds(ids);
  }

  @Override
  public boolean saveBatch(List<T> entities) {
    return saveBatch(entities);
  }

  @Override
  public boolean updateBatchById(List<T> entities) {
    return updateBatchById(entities);
  }
}
