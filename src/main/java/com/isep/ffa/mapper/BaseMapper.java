package com.isep.ffa.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 自定义BaseMapper接口
 * 继承MyBatis-Plus的BaseMapper，提供基础的CRUD操作
 * 
 * @param <T> 实体类型
 */
public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {
    // 可以在这里添加自定义的通用方法
}
