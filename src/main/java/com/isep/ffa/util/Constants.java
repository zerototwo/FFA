package com.isep.ffa.util;

public class Constants {

  // API Paths
  public static final String API_BASE_PATH = "/ffaAPI";
  public static final String ADMIN_PATH = "/admin";
  public static final String INTERVENER_PATH = "/intervener";
  public static final String USER_PATH = "/user";
  public static final String AUTH_PATH = "/auth";

  // User Roles
  public static final String ROLE_ADMIN = "ADMIN";
  public static final String ROLE_INTERVENER = "INTERVENER";
  public static final String ROLE_USER = "USER";

  // Pagination
  public static final String DEFAULT_PAGE_NUMBER = "0";
  public static final String DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_SORT_BY = "id";
  public static final String DEFAULT_SORT_DIRECTION = "asc";

  // JWT
  public static final String JWT_HEADER = "Authorization";
  public static final String JWT_PREFIX = "Bearer ";

  // File Upload
  public static final String UPLOAD_DIR = "uploads/";
  public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  // Validation Messages
  public static final String VALIDATION_EMAIL_INVALID = "Email format is invalid";
  public static final String VALIDATION_REQUIRED = "This field is required";
  public static final String VALIDATION_MIN_LENGTH = "Minimum length is {min} characters";
  public static final String VALIDATION_MAX_LENGTH = "Maximum length is {max} characters";

  // Error Messages
  public static final String ERROR_RESOURCE_NOT_FOUND = "Resource not found";
  public static final String ERROR_RESOURCE_ALREADY_EXISTS = "Resource already exists";
  public static final String ERROR_VALIDATION_FAILED = "Validation failed";
  public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
  public static final String ERROR_FORBIDDEN = "Access forbidden";
  public static final String ERROR_INTERNAL_SERVER = "Internal server error";
}
