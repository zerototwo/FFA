package com.isep.ffa.service;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.Institution;

import java.util.List;

/**
 * Institution Service Interface
 */
public interface InstitutionService extends BaseService<Institution> {

  /**
   * Find institutions by city ID
   */
  BaseResponse<List<Institution>> findByCityId(Long cityId);

  /**
   * Search institutions by name keyword
   */
  BaseResponse<PagedResponse<Institution>> searchByName(String keyword, int page, int size);
}

