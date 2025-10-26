package com.isep.ffa.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * MyBatis-Plus Auto-fill Handler
 * Automatically fills fields like creation time, modification time, etc.
 */
// @Component
public class CustomMetaObjectHandler implements com.baomidou.mybatisplus.core.handlers.MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    // Auto-fill on insert
    this.strictInsertFill(metaObject, "creationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);

    // Can get current user ID here and fill creatorUser field
    // Long currentUserId = getCurrentUserId();
    // this.strictInsertFill(metaObject, "creatorUser", Long.class, currentUserId);
    // this.strictInsertFill(metaObject, "lastModificatorUser", Long.class,
    // currentUserId);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    // Auto-fill on update
    this.strictUpdateFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());

    // Can get current user ID here and fill lastModificatorUser field
    // Long currentUserId = getCurrentUserId();
    // this.strictUpdateFill(metaObject, "lastModificatorUser", Long.class,
    // currentUserId);
  }

  /**
   * Get current user ID
   * This needs to be implemented according to the actual security framework
   */
  private Long getCurrentUserId() {
    // TODO: Implement logic to get current user ID
    // Can get current user information from SecurityContext
    return 1L; // Temporarily return 1, should actually get from authentication information
  }
}
