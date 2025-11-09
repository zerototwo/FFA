package com.isep.ffa.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Person Entity
 * Database table：person
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("person")
@Schema(description = "Person entity representing a platform user")
public class Person {

  /**
   * Primary Key ID
   */
  @Schema(description = "Primary key identifier", example = "11")
  @TableId(value = "id", type = IdType.AUTO)
  private Long id;

  /**
   * First Name
   */
  @Schema(description = "First name", example = "Alice")
  @TableField("first_name")
  private String firstName;

  /**
   * Last Name
   */
  @Schema(description = "Last name", example = "Johnson")
  @TableField("last_name")
  private String lastName;

  /**
   * Email
   */
  @Schema(description = "Unique email address", example = "alice.johnson@example.com")
  @TableField("email")
  private String email;

  /**
   * Address
   */
  @Schema(description = "Postal address", example = "123 Embassy Road, Ottawa")
  @TableField("address")
  private String address;

  /**
   * Password (hashed)
   */
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @TableField("password")
  private String password;

  /**
   * Login Name
   */
  @Schema(description = "Login identifier/username", example = "alice.johnson")
  @TableField("login")
  private String login;

  /**
   * Role ID
   */
  @Schema(description = "Role identifier", example = "3")
  @TableField("role_id")
  private Long roleId;

  /**
   * City ID
   */
  @Schema(description = "City identifier", example = "6")
  @TableField("city_id")
  private Long cityId;

  /**
   * Creation Date
   */
  @Schema(description = "Creation date", example = "2025-11-09")
  @TableField(value = "creation_date", fill = FieldFill.INSERT)
  private LocalDate creationDate;

  /**
   * Last Modification Date
   */
  @Schema(description = "Last modification date", example = "2025-11-09")
  @TableField(value = "last_modification_date", fill = FieldFill.INSERT_UPDATE)
  private LocalDate lastModificationDate;

  /**
   * Creator User ID
   */
  @Schema(description = "Creator user identifier", example = "1", nullable = true)
  @TableField("creator_user")
  private Long creatorUser;

  /**
   * Last Modificator User ID
   */
  @Schema(description = "Last modifier user identifier", example = "1", nullable = true)
  @TableField("last_modificator_user")
  private Long lastModificatorUser;

  /**
   * Is Deleted（逻辑删除）
   */
  @Schema(description = "Logical deletion flag", example = "false")
  @TableLogic
  @TableField("is_deleted")
  private Boolean isDeleted;

  // 关联对象（非数据库字段）
  /**
   * Role信息
   */
  @TableField(exist = false)
  private Role role;

  /**
   * 城市信息
   */
  @TableField(exist = false)
  private City city;

  /**
   * Organisation type selected during registration (EMBASSY/INSTITUTION/OTHER)
   */
  @Schema(description = "Organisation type chosen during registration (EMBASSY / INSTITUTION / OTHER)", example = "EMBASSY", nullable = true)
  @TableField("organization_type")
  private String organizationType;

  /**
   * Organisation identifier (embassy or institution ID)
   */
  @Schema(description = "Organisation identifier for EMBASSY/INSTITUTION types", example = "3", nullable = true)
  @TableField("organization_id")
  private Long organizationId;

  /**
   * Organisation name (for other organisations or display)
   */
  @Schema(description = "Organisation name when other organisation is provided", example = "Alliance Française", nullable = true)
  @TableField("organization_name")
  private String organizationName;
}
