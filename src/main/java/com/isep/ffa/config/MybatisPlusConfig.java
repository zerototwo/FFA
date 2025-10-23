package com.isep.ffa.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus配置类
 */
@Configuration
@MapperScan("com.isep.ffa.mapper")
public class MybatisPlusConfig {

  /**
   * MyBatis-Plus插件配置
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    // 插件配置 - 暂时注释，需要单独引入插件依赖
    // interceptor.addInnerInterceptor(new
    // PaginationInnerInterceptor(DbType.POSTGRE_SQL));
    // interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    // interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

    return interceptor;
  }
}
