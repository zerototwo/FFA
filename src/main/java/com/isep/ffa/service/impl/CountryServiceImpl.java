package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.Country;
import com.isep.ffa.mapper.CountryMapper;
import com.isep.ffa.service.CountryService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Country Service Implementation
 * Implements business logic for country management
 */
@Service
public class CountryServiceImpl extends BaseServiceImpl<CountryMapper, Country> implements CountryService {

  @Autowired
  private CountryMapper countryMapper;

  @Override
  public BaseResponse<Country> findByName(String name) {
    try {
      if (!StringUtils.hasText(name)) {
        return BaseResponse.error("Country name cannot be empty", 400);
      }

      Country country = countryMapper.findByName(name.trim());
      if (country != null) {
        return BaseResponse.success("Country found", country);
      } else {
        return BaseResponse.error("Country not found with name: " + name, 404);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error finding country by name: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<Country> findByPhoneIndicator(String phoneIndicator) {
    try {
      if (!StringUtils.hasText(phoneIndicator)) {
        return BaseResponse.error("Phone indicator cannot be empty", 400);
      }

      Country country = countryMapper.findByPhoneIndicator(phoneIndicator.trim());
      if (country != null) {
        return BaseResponse.success("Country found", country);
      } else {
        return BaseResponse.error("Country not found with phone indicator: " + phoneIndicator, 404);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error finding country by phone indicator: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<List<Country>> findByContinentId(Long continentId) {
    try {
      if (continentId == null || continentId <= 0) {
        return BaseResponse.error("Invalid continent ID", 400);
      }

      List<Country> countries = countryMapper.findByContinentId(continentId);
      if (countries != null && !countries.isEmpty()) {
        return BaseResponse.success("Countries found", countries);
      } else {
        return BaseResponse.error("No countries found for continent ID: " + continentId, 404);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error finding countries by continent: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<PagedResponse<Country>> getCountriesByContinent(Long continentId, int page, int size) {
    try {
      if (continentId == null || continentId <= 0) {
        return BaseResponse.error("Invalid continent ID", 400);
      }
      if (page < 1)
        page = 1;
      if (size < 1)
        size = 10;

      Page<Country> pageParam = new Page<>(page, size);
      QueryWrapper<Country> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq("continent_id", continentId);
      queryWrapper.eq("is_deleted", false);
      queryWrapper.orderByAsc("name");

      Page<Country> result = page(pageParam, queryWrapper);
      PagedResponse<Country> pagedResponse = PagedResponse.of(
          result.getRecords(),
          (int) result.getCurrent(),
          (int) result.getSize(),
          result.getTotal(),
          (int) result.getPages());

      return BaseResponse.success("Countries retrieved successfully", pagedResponse);
    } catch (Exception e) {
      return BaseResponse.error("Error getting countries by continent: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<PagedResponse<Country>> searchCountries(String keyword, int page, int size) {
    try {
      if (!StringUtils.hasText(keyword)) {
        return BaseResponse.error("Search keyword cannot be empty", 400);
      }
      if (page < 1)
        page = 1;
      if (size < 1)
        size = 10;

      Page<Country> pageParam = new Page<>(page, size);
      QueryWrapper<Country> queryWrapper = new QueryWrapper<>();
      queryWrapper.like("name", keyword.trim())
          .or()
          .like("phone_number_indicator", keyword.trim());
      queryWrapper.eq("is_deleted", false);
      queryWrapper.orderByAsc("name");

      Page<Country> result = page(pageParam, queryWrapper);
      PagedResponse<Country> pagedResponse = PagedResponse.of(
          result.getRecords(),
          (int) result.getCurrent(),
          (int) result.getSize(),
          result.getTotal(),
          (int) result.getPages());

      return BaseResponse.success("Search completed successfully", pagedResponse);
    } catch (Exception e) {
      return BaseResponse.error("Error searching countries: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<Country> createCountry(Country country) {
    try {
      // Validate input
      if (country == null) {
        return BaseResponse.error("Country information cannot be null", 400);
      }
      if (!StringUtils.hasText(country.getName())) {
        return BaseResponse.error("Country name is required", 400);
      }
      if (country.getContinentId() == null || country.getContinentId() <= 0) {
        return BaseResponse.error("Valid continent ID is required", 400);
      }

      // Check if country already exists
      Country existingCountry = countryMapper.findByName(country.getName().trim());
      if (existingCountry != null) {
        return BaseResponse.error("Country already exists with name: " + country.getName(), 409);
      }

      // Set default values
      country.setName(country.getName().trim());
      if (StringUtils.hasText(country.getPhoneNumberIndicator())) {
        country.setPhoneNumberIndicator(country.getPhoneNumberIndicator().trim());
      }

      // Save country
      boolean saved = save(country);
      if (saved) {
        return BaseResponse.success("Country created successfully", country);
      } else {
        return BaseResponse.error("Failed to create country", 500);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error creating country: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<Country> updateCountry(Country country) {
    try {
      // Validate input
      if (country == null) {
        return BaseResponse.error("Country information cannot be null", 400);
      }
      if (country.getId() == null || country.getId() <= 0) {
        return BaseResponse.error("Valid country ID is required", 400);
      }
      if (!StringUtils.hasText(country.getName())) {
        return BaseResponse.error("Country name is required", 400);
      }
      if (country.getContinentId() == null || country.getContinentId() <= 0) {
        return BaseResponse.error("Valid continent ID is required", 400);
      }

      // Check if country exists
      Country existingCountry = getById(country.getId());
      if (existingCountry == null) {
        return BaseResponse.error("Country not found with ID: " + country.getId(), 404);
      }

      // Check if name is already taken by another country
      Country countryWithSameName = countryMapper.findByName(country.getName().trim());
      if (countryWithSameName != null && !countryWithSameName.getId().equals(country.getId())) {
        return BaseResponse.error("Country name already exists: " + country.getName(), 409);
      }

      // Set updated values
      country.setName(country.getName().trim());
      if (StringUtils.hasText(country.getPhoneNumberIndicator())) {
        country.setPhoneNumberIndicator(country.getPhoneNumberIndicator().trim());
      }

      // Update country
      boolean updated = updateById(country);
      if (updated) {
        Country updatedCountry = getById(country.getId());
        return BaseResponse.success("Country updated successfully", updatedCountry);
      } else {
        return BaseResponse.error("Failed to update country", 500);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error updating country: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<Boolean> deleteCountry(Long id) {
    try {
      if (id == null || id <= 0) {
        return BaseResponse.error("Valid country ID is required", 400);
      }

      // Check if country exists
      Country existingCountry = getById(id);
      if (existingCountry == null) {
        return BaseResponse.error("Country not found with ID: " + id, 404);
      }

      // Perform logical delete
      boolean deleted = removeById(id);
      if (deleted) {
        return BaseResponse.success("Country deleted successfully", true);
      } else {
        return BaseResponse.error("Failed to delete country", 500);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error deleting country: " + e.getMessage(), 500);
    }
  }

  @Override
  public BaseResponse<List<Country>> getCountriesWithEmbassies() {
    try {
      // Get all countries
      List<Country> allCountries = list();

      if (allCountries != null && !allCountries.isEmpty()) {
        return BaseResponse.success("Countries with embassy information retrieved", allCountries);
      } else {
        return BaseResponse.error("No countries found", 404);
      }
    } catch (Exception e) {
      return BaseResponse.error("Error getting countries with embassies: " + e.getMessage(), 500);
    }
  }
}
