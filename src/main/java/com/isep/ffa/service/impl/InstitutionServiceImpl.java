package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.Institution;
import com.isep.ffa.mapper.InstitutionMapper;
import com.isep.ffa.service.InstitutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Institution Service Implementation
 */
@Service
public class InstitutionServiceImpl extends BaseServiceImpl<InstitutionMapper, Institution> implements InstitutionService {

  @Autowired
  private InstitutionMapper institutionMapper;

  @Override
  public BaseResponse<List<Institution>> findByCityId(Long cityId) {
    if (cityId == null) {
      return BaseResponse.error("City ID is required", 400);
    }
    List<Institution> institutions = institutionMapper.findByCityId(cityId);
    return BaseResponse.success("Institutions retrieved successfully", institutions);
  }

  @Override
  public BaseResponse<PagedResponse<Institution>> searchByName(String keyword, int page, int size) {
    if (keyword == null || keyword.trim().isEmpty()) {
      return BaseResponse.error("Keyword is required", 400);
    }

    Page<Institution> pageParam = new Page<>(page, size);
    LambdaQueryWrapper<Institution> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(Institution::getName, keyword);
    Page<Institution> result = page(pageParam, wrapper);

    PagedResponse<Institution> response = PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());

    return BaseResponse.success("Institutions retrieved successfully", response);
  }
}

