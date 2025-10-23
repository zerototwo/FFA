package com.isep.ffa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * 自定义BaseService接口
 * 继承MyBatis-Plus的IService，提供基础的CRUD操作
 * 
 * @param <T> 实体类型
 */
public interface BaseService<T> extends IService<T> {

  /**
   * 分页查询
   * 
   * @param page 页码
   * @param size 每页大小
   * @return 分页结果
   */
  PagedResponse<T> getPage(int page, int size);

  /**
   * 分页查询（带排序）
   * 
   * @param page    页码
   * @param size    每页大小
   * @param sortBy  排序字段
   * @param sortDir 排序方向
   * @return 分页结果
   */
  PagedResponse<T> getPage(int page, int size, String sortBy, String sortDir);

  /**
   * 根据ID列表查询
   * 
   * @param ids ID列表
   * @return 实体列表
   */
  List<T> getByIds(List<Long> ids);

  /**
   * 根据ID列表删除
   * 
   * @param ids ID列表
   * @return 是否成功
   */
  boolean deleteByIds(List<Long> ids);

  /**
   * 批量保存
   * 
   * @param entities 实体列表
   * @return 是否成功
   */
  boolean saveBatch(List<T> entities);

  /**
   * 批量更新
   * 
   * @param entities 实体列表
   * @return 是否成功
   */
  boolean updateBatchById(List<T> entities);
}
