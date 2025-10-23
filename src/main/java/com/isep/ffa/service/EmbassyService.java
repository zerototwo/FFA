package com.isep.ffa.service;

import com.isep.ffa.entity.Embassy;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;

import java.util.List;

/**
 * Embassy Service Interface
 * Provides business logic for embassy management
 */
public interface EmbassyService extends BaseService<Embassy> {

  /**
   * Find embassies by embassy of country ID
   * 
   * @param countryId embassy of country ID
   * @return list of embassies
   */
  BaseResponse<List<Embassy>> findByEmbassyOfCountryId(Long countryId);

  /**
   * Find embassies by embassy in country ID
   * 
   * @param countryId embassy in country ID
   * @return list of embassies
   */
  BaseResponse<List<Embassy>> findByEmbassyInCountryId(Long countryId);

  /**
   * Find embassies by city ID
   * 
   * @param cityId city ID
   * @return list of embassies
   */
  BaseResponse<List<Embassy>> findByCityId(Long cityId);

  /**
   * Search embassies by address
   * 
   * @param address address keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<Embassy>> searchByAddress(String address, int page, int size);

  /**
   * Get paginated embassies by embassy of country
   * 
   * @param countryId embassy of country ID
   * @param page      page number
   * @param size      page size
   * @return paginated embassies
   */
  BaseResponse<PagedResponse<Embassy>> getEmbassiesByEmbassyOfCountry(Long countryId, int page, int size);

  /**
   * Get paginated embassies by embassy in country
   * 
   * @param countryId embassy in country ID
   * @param page      page number
   * @param size      page size
   * @return paginated embassies
   */
  BaseResponse<PagedResponse<Embassy>> getEmbassiesByEmbassyInCountry(Long countryId, int page, int size);

  /**
   * Create new embassy
   * 
   * @param embassy embassy information
   * @return created embassy
   */
  BaseResponse<Embassy> createEmbassy(Embassy embassy);

  /**
   * Update embassy information
   * 
   * @param embassy embassy information
   * @return updated embassy
   */
  BaseResponse<Embassy> updateEmbassy(Embassy embassy);

  /**
   * Delete embassy by ID
   * 
   * @param id embassy ID
   * @return operation result
   */
  BaseResponse<Boolean> deleteEmbassy(Long id);

  /**
   * Get embassy with full details
   * 
   * @param id embassy ID
   * @return embassy with country and city information
   */
  BaseResponse<Embassy> getEmbassyWithDetails(Long id);
}
