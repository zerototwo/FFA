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

    // Set sorting - simplified version, sorting not supported temporarily
    // TODO: Implement sorting functionality
    if (sortBy != null && !sortBy.isEmpty()) {
      // Temporarily ignore sorting, use pagination directly
    }

    Page<T> result = page(pageParam);
    return PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
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
