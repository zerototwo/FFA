package com.isep.ffa.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分页配置类
 */
// @Configuration
public class PageConfig {

  /**
   * 默认分页参数
   */
  @Bean
  public Page<?> defaultPage() {
    return new Page<>(1, 10);
  }
}
