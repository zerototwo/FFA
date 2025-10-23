package com.isep.ffa.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Country Request DTO
 * Used for creating and updating country information
 */
@Data
public class CountryRequest {

  @NotBlank(message = "Country name is required")
  private String name;

  private String phoneNumberIndicator;

  @NotNull(message = "Continent ID is required")
  @Positive(message = "Continent ID must be positive")
  private Long continentId;
}
