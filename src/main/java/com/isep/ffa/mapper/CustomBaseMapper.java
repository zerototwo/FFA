package com.isep.ffa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * Custom BaseMapper Interface
 * Extends MyBatis-Plus BaseMapper, provides basic CRUD operations
 * 
 * @param <T> Entity type
 */
public interface CustomBaseMapper<T> extends BaseMapper<T> {
    // Can add custom common methods here
}
