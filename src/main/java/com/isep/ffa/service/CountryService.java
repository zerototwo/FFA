package com.isep.ffa.service;

import com.isep.ffa.entity.Country;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Country Service Interface
 * Provides business logic for country management
 */
public interface CountryService extends BaseService<Country> {

  /**
   * Find country by name
   * 
   * @param name country name
   * @return country information
   */
  BaseResponse<Country> findByName(String name);

  /**
   * Find country by phone indicator
   * 
   * @param phoneIndicator phone indicator
   * @return country information
   */
  BaseResponse<Country> findByPhoneIndicator(String phoneIndicator);

  /**
   * Find countries by continent ID
   * 
   * @param continentId continent ID
   * @return list of countries
   */
  BaseResponse<List<Country>> findByContinentId(Long continentId);

  /**
   * Get paginated countries by continent
   * 
   * @param continentId continent ID
   * @param page        page number
   * @param size        page size
   * @return paginated countries
   */
  BaseResponse<PagedResponse<Country>> getCountriesByContinent(Long continentId, int page, int size);

  /**
   * Search countries by keyword
   * 
   * @param keyword search keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<Country>> searchCountries(String keyword, int page, int size);

  /**
   * Create new country
   * 
   * @param country country information
   * @return created country
   */
  BaseResponse<Country> createCountry(Country country);

  /**
   * Update country information
   * 
   * @param country country information
   * @return updated country
   */
  BaseResponse<Country> updateCountry(Country country);

  /**
   * Delete country by ID
   * 
   * @param id country ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteCountry(Long id);

  /**
   * Get all countries with embassies
   * 
   * @return list of countries with embassy information
   */
  BaseResponse<List<Country>> getCountriesWithEmbassies();

  /**
   * Get all countries
   * 
   * @return list of all countries
   */
  BaseResponse<List<Country>> getAllCountries();
}
