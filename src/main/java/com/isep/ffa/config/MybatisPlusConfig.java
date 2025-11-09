package com.isep.ffa.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
// import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis-Plus Configuration Class
 */
@Configuration
@MapperScan("com.isep.ffa.mapper")
public class MybatisPlusConfig {

  /**
   * MyBatis-Plus Plugin Configuration
   */
  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
    // interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
    // interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());

    return interceptor;
  }
}
