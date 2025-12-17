package com.isep.ffa.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.isep.ffa.security.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * MyBatis-Plus Auto-fill Handler
 */
@Component
public class CustomMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    // 1. Handle existing fields (LocalDate)
    this.strictInsertFill(metaObject, "creationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());
    this.strictInsertFill(metaObject, "isDeleted", Boolean.class, false);

    // 2. Handle new fields for Announcement (LocalDateTime)
    this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

    // 3. Handle User IDs
    Long currentUserId = getCurrentUserId();
    if (currentUserId != null) {
      this.strictInsertFill(metaObject, "creatorUser", Long.class, currentUserId);
      this.strictInsertFill(metaObject, "lastModificatorUser", Long.class, currentUserId);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    // 1. Handle existing fields (LocalDate)
    this.strictUpdateFill(metaObject, "lastModificationDate", LocalDate.class, LocalDate.now());

    // 2. Handle new fields for Announcement (LocalDateTime)
    this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());

    // 3. Handle User IDs
    Long currentUserId = getCurrentUserId();
    if (currentUserId != null) {
      this.strictUpdateFill(metaObject, "lastModificatorUser", Long.class, currentUserId);
    }
  }

  private Long getCurrentUserId() {
    try {
      return SecurityUtils.getCurrentUserId();
    } catch (Exception e) {
      return null;
    }
  }
}
