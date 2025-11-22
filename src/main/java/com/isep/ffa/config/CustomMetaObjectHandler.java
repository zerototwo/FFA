package com.isep.ffa.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.isep.ffa.security.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * MyBatis-Plus Auto-fill Handler
 * Automatically fills fields like creation time, modification time, creator,
 * and modifier
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    // Auto-fill on insert
    this.strictInsertFill(metaObject, "creationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);

    // Get current user ID and fill creator/modifier fields
    Long currentUserId = getCurrentUserId();
    if (currentUserId != null) {
      this.strictInsertFill(metaObject, "creatorUser", Long.class, currentUserId);
      this.strictInsertFill(metaObject, "lastModificatorUser", Long.class, currentUserId);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    // Auto-fill on update
    this.strictUpdateFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());

    // Get current user ID and fill modifier field
    Long currentUserId = getCurrentUserId();
    if (currentUserId != null) {
      this.strictUpdateFill(metaObject, "lastModificatorUser", Long.class, currentUserId);
    }
  }

  /**
   * Get current user ID from SecurityContext
   * Returns null if user is not authenticated
   */
  private Long getCurrentUserId() {
    try {
      return SecurityUtils.getCurrentUserId();
    } catch (Exception e) {
      // If not authenticated or error occurs, return null
      // This allows the handler to work even when called from non-authenticated
      // contexts
      return null;
    }
  }
}
