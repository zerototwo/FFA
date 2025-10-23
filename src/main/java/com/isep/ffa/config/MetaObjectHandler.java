package com.isep.ffa.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * MyBatis-Plus自动填充处理器
 * 自动填充创建时间、修改时间等字段
 */
@Component
public class MetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    // 插入时自动填充
    this.strictInsertFill(metaObject, "creationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);

    // 可以在这里获取当前用户ID并填充creatorUser字段
    // Long currentUserId = getCurrentUserId();
    // this.strictInsertFill(metaObject, "creatorUser", Long.class, currentUserId);
    // this.strictInsertFill(metaObject, "lastModificatorUser", Long.class,
    // currentUserId);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    // 更新时自动填充
    this.strictUpdateFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());

    // 可以在这里获取当前用户ID并填充lastModificatorUser字段
    // Long currentUserId = getCurrentUserId();
    // this.strictUpdateFill(metaObject, "lastModificatorUser", Long.class,
    // currentUserId);
  }

  /**
   * 获取当前用户ID
   * 这里需要根据实际的安全框架来实现
   */
  private Long getCurrentUserId() {
    // TODO: 实现获取当前用户ID的逻辑
    // 可以从SecurityContext中获取当前用户信息
    return 1L; // 临时返回1，实际应该从认证信息中获取
  }
}
