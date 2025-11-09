package com.isep.ffa.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class BaseResponse<T> {
  @Schema(description = "Flag indicating whether the request was successful", example = "true")
  private boolean success;
  @Schema(description = "Human readable message describing the operation outcome", example = "Operation successful")
  private String message;
  @Schema(description = "Response payload")
  private T data;
  @Schema(description = "Timestamp of the response", example = "2025-11-09T17:32:57.57633")
  private LocalDateTime timestamp;
  @Schema(description = "HTTP status code", example = "200")
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
