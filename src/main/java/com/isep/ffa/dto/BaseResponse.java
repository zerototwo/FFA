package com.isep.ffa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse<T> {
  private boolean success;
  private String message;
  private T data;
  private LocalDateTime timestamp;
  private int status;

  public static <T> BaseResponse<T> success(T data) {
    return new BaseResponse<>(true, "Operation successful", data, LocalDateTime.now(), 200);
  }

  public static <T> BaseResponse<T> success(String message, T data) {
    return new BaseResponse<>(true, message, data, LocalDateTime.now(), 200);
  }

  public static <T> BaseResponse<T> error(String message, int status) {
    return new BaseResponse<>(false, message, null, LocalDateTime.now(), status);
  }

  public static <T> BaseResponse<T> error(String message) {
    return new BaseResponse<>(false, message, null, LocalDateTime.now(), 500);
  }
}
