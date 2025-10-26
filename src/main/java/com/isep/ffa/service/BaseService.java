package com.isep.ffa.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Custom BaseService Interface
 * Extends MyBatis-Plus IService, provides basic CRUD operations
 * 
 * @param <T> Entity type
 */
public interface BaseService<T> extends IService<T> {

  /**
   * Paginated query
   * 
   * @param page Page number
   * @param size Page size
   * @return Paginated result
   */
  PagedResponse<T> getPage(int page, int size);

  /**
   * Paginated query (with sorting)
   * 
   * @param page    Page number
   * @param size    Page size
   * @param sortBy  Sort field
   * @param sortDir Sort direction
   * @return Paginated result
   */
  PagedResponse<T> getPage(int page, int size, String sortBy, String sortDir);

  /**
   * Query by ID list
   * 
   * @param ids ID list
   * @return Entity list
   */
  List<T> getByIds(List<Long> ids);

  /**
   * Delete by ID list
   * 
   * @param ids ID list
   * @return Success or not
   */
  boolean deleteByIds(List<Long> ids);

  /**
   * Batch save
   * 
   * @param entities Entity list
   * @return Success or not
   */
  boolean saveBatch(List<T> entities);

  /**
   * Batch update
   * 
   * @param entities Entity list
   * @return Success or not
   */
  boolean updateBatchById(List<T> entities);
}
