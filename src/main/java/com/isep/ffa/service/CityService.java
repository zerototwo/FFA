package com.isep.ffa.service;

import com.isep.ffa.entity.City;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * City Service Interface
 * Provides business logic for city management
 */
public interface CityService extends BaseService<City> {

  /**
   * Find city by name
   * 
   * @param name city name
   * @return city information
   */
  BaseResponse<City> findByName(String name);

  /**
   * Find cities by department ID
   * 
   * @param departmentId department ID
   * @return list of cities
   */
  BaseResponse<List<City>> findByDepartmentId(Long departmentId);

  /**
   * Find city by postal code
   * 
   * @param postalCode postal code
   * @return city information
   */
  BaseResponse<City> findByPostalCode(Integer postalCode);

  /**
   * Get paginated cities by department
   * 
   * @param departmentId department ID
   * @param page         page number
   * @param size         page size
   * @return paginated cities
   */
  BaseResponse<PagedResponse<City>> getCitiesByDepartment(Long departmentId, int page, int size);

  /**
   * Search cities by keyword
   * 
   * @param keyword search keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<City>> searchCities(String keyword, int page, int size);

  /**
   * Create new city
   * 
   * @param city city information
   * @return created city
   */
  BaseResponse<City> createCity(City city);

  /**
   * Update city information
   * 
   * @param city city information
   * @return updated city
   */
  BaseResponse<City> updateCity(City city);

  /**
   * Delete city by ID
   * 
   * @param id city ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteCity(Long id);

  /**
   * Get city with full details
   * 
   * @param id city ID
   * @return city with department and country information
   */
  BaseResponse<City> getCityWithDetails(Long id);
}
