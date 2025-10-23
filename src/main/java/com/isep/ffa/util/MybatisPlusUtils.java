package com.isep.ffa.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * MyBatis-Plus工具类
 * 提供常用的查询和更新方法
 */
public class MybatisPlusUtils {

  /**
   * 构建查询条件
   * 
   * @param params 查询参数
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

      // 处理特殊查询条件
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
        // 默认等值查询
        queryWrapper.eq(key, value);
      }
    }

    return queryWrapper;
  }

  /**
   * 构建更新条件
   * 
   * @param params 更新参数
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
   * 构建分页对象
   * 
   * @param page 页码
   * @param size 每页大小
   * @return Page对象
   */
  public static <T> Page<T> buildPage(int page, int size) {
    return new Page<>(page, size);
  }

  /**
   * 构建分页对象（带排序）
   * 
   * @param page    页码
   * @param size    每页大小
   * @param sortBy  排序字段
   * @param sortDir 排序方向
   * @return Page对象
   */
  public static <T> Page<T> buildPage(int page, int size, String sortBy, String sortDir) {
    Page<T> pageObj = new Page<>(page, size);

    // 暂时不支持排序
    // TODO: 实现排序功能
    if (sortBy != null && !sortBy.trim().isEmpty()) {
      // 暂时忽略排序
    }

    return pageObj;
  }
}
