package com.isep.ffa.dto;

import com.isep.ffa.entity.Country;
import lombok.Data;

import java.time.LocalDate;

/**
 * Country Response DTO
 * Used for returning country information in API responses
 */
@Data
public class CountryResponse {

  private Long id;
  private String name;
  private String phoneNumberIndicator;
  private Long continentId;
  private String continentName;
  private LocalDate creationDate;
  private LocalDate lastModificationDate;

  /**
   * Convert Country entity to CountryResponse DTO
   */
  public static CountryResponse fromEntity(Country country) {
    if (country == null) {
      return null;
    }

    CountryResponse response = new CountryResponse();
    response.setId(country.getId());
    response.setName(country.getName());
    response.setPhoneNumberIndicator(country.getPhoneNumberIndicator());
    response.setContinentId(country.getContinentId());
    response.setCreationDate(country.getCreationDate());
    response.setLastModificationDate(country.getLastModificationDate());

    // Set continent name if available
    if (country.getContinent() != null) {
      response.setContinentName(country.getContinent().getName());
    }

    return response;
  }
}
