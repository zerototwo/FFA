package com.isep.ffa.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * MyBatis-Plus Utility Class
 * Provides common query and update methods
 */
public class MybatisPlusUtils {

  /**
   * Build query conditions
   * 
   * @param params Query parameters
   * @return QueryWrapper
   */
  public static <T> QueryWrapper<T> buildQueryWrapper(Map<String, Object> params) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<>();

    if (params == null || params.isEmpty()) {
      return queryWrapper;
    }

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (value == null || value.toString().trim().isEmpty()) {
        continue;
      }

      // Handle special query conditions
      if (key.endsWith("_like")) {
        String fieldName = key.substring(0, key.length() - 5);
        queryWrapper.like(fieldName, value);
      } else if (key.endsWith("_gt")) {
        String fieldName = key.substring(0, key.length() - 3);
        queryWrapper.gt(fieldName, value);
      } else if (key.endsWith("_lt")) {
        String fieldName = key.substring(0, key.length() - 3);
        queryWrapper.lt(fieldName, value);
      } else if (key.endsWith("_ge")) {
        String fieldName = key.substring(0, key.length() - 3);
        queryWrapper.ge(fieldName, value);
      } else if (key.endsWith("_le")) {
        String fieldName = key.substring(0, key.length() - 3);
        queryWrapper.le(fieldName, value);
      } else if (key.endsWith("_in")) {
        String fieldName = key.substring(0, key.length() - 3);
        if (value instanceof Iterable) {
          queryWrapper.in(fieldName, (Iterable<?>) value);
        } else if (value.getClass().isArray()) {
          queryWrapper.in(fieldName, (Object[]) value);
        }
      } else {
        // Default equality query
        queryWrapper.eq(key, value);
      }
    }

    return queryWrapper;
  }

  /**
   * Build update conditions
   * 
   * @param params Update parameters
   * @return UpdateWrapper
   */
  public static <T> UpdateWrapper<T> buildUpdateWrapper(Map<String, Object> params) {
    UpdateWrapper<T> updateWrapper = new UpdateWrapper<>();

    if (params == null || params.isEmpty()) {
      return updateWrapper;
    }

    for (Map.Entry<String, Object> entry : params.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (value == null) {
        continue;
      }

      updateWrapper.set(key, value);
    }

    return updateWrapper;
  }

  /**
   * Build pagination object
   * 
   * @param page Page number
   * @param size Page size
   * @return Page object
   */
  public static <T> Page<T> buildPage(int page, int size) {
    return new Page<>(page, size);
  }

  /**
   * Build pagination object (with sorting)
   * 
   * @param page    Page number
   * @param size    Page size
   * @param sortBy  Sort field
   * @param sortDir Sort direction
   * @return Page object
   */
  public static <T> Page<T> buildPage(int page, int size, String sortBy, String sortDir) {
    Page<T> pageObj = new Page<>(page, size);

    // Sorting not supported temporarily
    // TODO: Implement sorting functionality
    if (sortBy != null && !sortBy.trim().isEmpty()) {
      // Temporarily ignore sorting
    }

    return pageObj;
  }
}
