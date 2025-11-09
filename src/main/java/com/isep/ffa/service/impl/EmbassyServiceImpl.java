package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.Embassy;
import com.isep.ffa.mapper.EmbassyMapper;
import com.isep.ffa.service.EmbassyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Embassy Service Implementation
 * Implements business logic for embassy management
 */
@Service
public class EmbassyServiceImpl extends BaseServiceImpl<EmbassyMapper, Embassy> implements EmbassyService {

  @Autowired
  private EmbassyMapper embassyMapper;

  @Override
  public BaseResponse<List<Embassy>> findByEmbassyOfCountryId(Long countryId) {
    if (countryId == null) {
      return BaseResponse.error("Country ID is required", 400);
    }
    List<Embassy> embassies = embassyMapper.findByEmbassyOfCountryId(countryId);
    return BaseResponse.success("Embassies retrieved successfully", embassies);
  }

  @Override
  public BaseResponse<List<Embassy>> findByEmbassyInCountryId(Long countryId) {
    if (countryId == null) {
      return BaseResponse.error("Country ID is required", 400);
    }
    List<Embassy> embassies = embassyMapper.findByEmbassyInCountryId(countryId);
    return BaseResponse.success("Embassies retrieved successfully", embassies);
  }

  @Override
  public BaseResponse<List<Embassy>> findByCityId(Long cityId) {
    if (cityId == null) {
      return BaseResponse.error("City ID is required", 400);
    }
    List<Embassy> embassies = embassyMapper.findByCityId(cityId);
    return BaseResponse.success("Embassies retrieved successfully", embassies);
  }

  @Override
  public BaseResponse<PagedResponse<Embassy>> searchByAddress(String address, int page, int size) {
    if (address == null || address.trim().isEmpty()) {
      return BaseResponse.error("Address keyword is required", 400);
    }

    Page<Embassy> pageParam = new Page<>(page, size);
    LambdaQueryWrapper<Embassy> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(Embassy::getAddress, address);
    Page<Embassy> result = page(pageParam, wrapper);

    PagedResponse<Embassy> response = PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
    return BaseResponse.success("Embassies retrieved successfully", response);
  }

  @Override
  public BaseResponse<PagedResponse<Embassy>> getEmbassiesByEmbassyOfCountry(Long countryId, int page, int size) {
    if (countryId == null) {
      return BaseResponse.error("Country ID is required", 400);
    }

    Page<Embassy> pageParam = new Page<>(page, size);
    LambdaQueryWrapper<Embassy> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Embassy::getEmbassyOfCountryId, countryId);
    Page<Embassy> result = page(pageParam, wrapper);

    PagedResponse<Embassy> response = PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
    return BaseResponse.success("Embassies retrieved successfully", response);
  }

  @Override
  public BaseResponse<PagedResponse<Embassy>> getEmbassiesByEmbassyInCountry(Long countryId, int page, int size) {
    if (countryId == null) {
      return BaseResponse.error("Country ID is required", 400);
    }

    Page<Embassy> pageParam = new Page<>(page, size);
    LambdaQueryWrapper<Embassy> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(Embassy::getEmbassyInCountryId, countryId);
    Page<Embassy> result = page(pageParam, wrapper);

    PagedResponse<Embassy> response = PagedResponse.of(
        result.getRecords(),
        (int) result.getCurrent(),
        (int) result.getSize(),
        result.getTotal(),
        (int) result.getPages());
    return BaseResponse.success("Embassies retrieved successfully", response);
  }

  @Override
  public BaseResponse<Embassy> createEmbassy(Embassy embassy) {
    if (embassy == null) {
      return BaseResponse.error("Embassy data is required", 400);
    }
    boolean saved = save(embassy);
    return saved
        ? BaseResponse.success("Embassy created successfully", embassy)
        : BaseResponse.error("Failed to create embassy", 500);
  }

  @Override
  public BaseResponse<Embassy> updateEmbassy(Embassy embassy) {
    if (embassy == null || embassy.getId() == null) {
      return BaseResponse.error("Embassy ID is required", 400);
    }
    boolean updated = updateById(embassy);
    return updated
        ? BaseResponse.success("Embassy updated successfully", embassy)
        : BaseResponse.error("Failed to update embassy", 500);
  }

  @Override
  public BaseResponse<Boolean> deleteEmbassy(Long id) {
    if (id == null) {
      return BaseResponse.error("Embassy ID is required", 400);
    }
    boolean removed = removeById(id);
    return removed
        ? BaseResponse.success("Embassy deleted successfully", true)
        : BaseResponse.error("Failed to delete embassy", 500);
  }

  @Override
  public BaseResponse<Embassy> getEmbassyWithDetails(Long id) {
    if (id == null) {
      return BaseResponse.error("Embassy ID is required", 400);
    }
    Embassy embassy = getById(id);
    if (embassy == null) {
      return BaseResponse.error("Embassy not found", 404);
    }
    return BaseResponse.success("Embassy retrieved successfully", embassy);
  }
}
