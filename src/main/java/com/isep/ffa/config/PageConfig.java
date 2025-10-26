package com.isep.ffa.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Pagination Configuration Class
 */
// @Configuration
public class PageConfig {

  /**
   * Default pagination parameters
   */
  @Bean
  public Page<?> defaultPage() {
    return new Page<>(1, 10);
  }
}
