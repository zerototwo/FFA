package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Global Exception Handler
 * Handles exceptions across all controllers
 */
@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

  /**
   * Handle generic exceptions
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<BaseResponse<Object>> handleGenericException(Exception ex, WebRequest request) {
    // Log the full exception for debugging
    System.err.println("Exception occurred: " + ex.getClass().getName());
    System.err.println("Message: " + ex.getMessage());
    ex.printStackTrace();
    
    BaseResponse<Object> response = BaseResponse.error(
        "An unexpected error occurred: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  /**
   * Handle illegal argument exceptions
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<BaseResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex,
      WebRequest request) {
    BaseResponse<Object> response = BaseResponse.error(
        "Invalid argument: " + ex.getMessage(),
        HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handle null pointer exceptions
   */
  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<BaseResponse<Object>> handleNullPointerException(NullPointerException ex, WebRequest request) {
    BaseResponse<Object> response = BaseResponse.error(
        "Required data is missing: A required field is null or empty",
        HttpStatus.BAD_REQUEST.value());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  /**
   * Handle runtime exceptions
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<BaseResponse<Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
    BaseResponse<Object> response = BaseResponse.error(
        "Runtime error: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}
