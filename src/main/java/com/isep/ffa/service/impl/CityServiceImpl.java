package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.isep.ffa.entity.City;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.mapper.CityMapper;
import com.isep.ffa.service.CityService;
import org.springframework.stereotype.Service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * City Service Implementation
 * Implements business logic for city management
 */
@Service
public class CityServiceImpl extends BaseServiceImpl<CityMapper, City> implements CityService {

  private final CityMapper cityMapper;

  public CityServiceImpl(CityMapper cityMapper) {
    this.cityMapper = cityMapper;
  }

  private int normalizePage(int page) {
    return Math.max(page, 0);
  }

  private int normalizeSize(int size) {
    return size <= 0 ? 10 : size;
  }

  private PagedResponse<City> toPagedResponse(Page<City> pageResult) {
    int zeroBasedPage = (int) pageResult.getCurrent() - 1;
    zeroBasedPage = Math.max(zeroBasedPage, 0);
    return PagedResponse.of(
        pageResult.getRecords(),
        zeroBasedPage,
        (int) pageResult.getSize(),
        pageResult.getTotal(),
        (int) pageResult.getPages());
  }

  @Override
  public PagedResponse<City> getPage(int page, int size) {
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    Page<City> pageRequest = new Page<>(safePage + 1L, safeSize);
    QueryWrapper<City> wrapper = new QueryWrapper<>();
    wrapper.eq("is_deleted", false);
    Page<City> result = super.page(pageRequest, wrapper);
    return toPagedResponse(result);
  }

  @Override
  public PagedResponse<City> getPage(int page, int size, String sortBy, String sortDir) {
    // Sorting not yet implemented; fall back to default pagination
    return getPage(page, size);
  }

  @Override
  public BaseResponse<City> findByName(String name) {
    if (StringUtils.isBlank(name)) {
      return BaseResponse.error("City name must not be blank", 400);
    }
    City city = cityMapper.findByName(name.trim());
    if (city == null) {
      return BaseResponse.error("City not found with name: " + name, 404);
    }
    return BaseResponse.success("City found", city);
  }

  @Override
  public BaseResponse<List<City>> findByDepartmentId(Long departmentId) {
    if (departmentId == null) {
      return BaseResponse.error("Department ID is required", 400);
    }
    List<City> cities = cityMapper.findByDepartmentId(departmentId);
    return BaseResponse.success("Cities retrieved successfully", cities);
  }

  @Override
  public BaseResponse<City> findByPostalCode(Integer postalCode) {
    if (postalCode == null) {
      return BaseResponse.error("Postal code is required", 400);
    }
    City city = cityMapper.findByPostalCode(postalCode);
    if (city == null) {
      return BaseResponse.error("City not found with postal code: " + postalCode, 404);
    }
    return BaseResponse.success("City found", city);
  }

  @Override
  public BaseResponse<PagedResponse<City>> getCitiesByDepartment(Long departmentId, int page, int size) {
    if (departmentId == null) {
      return BaseResponse.error("Department ID is required", 400);
    }
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    QueryWrapper<City> wrapper = new QueryWrapper<>();
    wrapper.eq("department_id", departmentId).eq("is_deleted", false);
    Page<City> pageRequest = new Page<>(safePage + 1L, safeSize);
    Page<City> result = super.page(pageRequest, wrapper);
    return BaseResponse.success("Cities retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<PagedResponse<City>> searchCities(String keyword, int page, int size) {
    int safePage = normalizePage(page);
    int safeSize = normalizeSize(size);
    QueryWrapper<City> wrapper = new QueryWrapper<>();
    wrapper.eq("is_deleted", false);
    if (StringUtils.isNotBlank(keyword)) {
      wrapper.like("name", keyword.trim());
    }
    Page<City> pageRequest = new Page<>(safePage + 1L, safeSize);
    Page<City> result = super.page(pageRequest, wrapper);
    return BaseResponse.success("Cities retrieved successfully", toPagedResponse(result));
  }

  @Override
  public BaseResponse<City> createCity(City city) {
    if (city == null) {
      return BaseResponse.error("City payload is required", 400);
    }
    if (StringUtils.isBlank(city.getName())) {
      return BaseResponse.error("City name is required", 400);
    }
    boolean created = save(city);
    if (!created) {
      return BaseResponse.error("Failed to create city");
    }
    return BaseResponse.success("City created successfully", city);
  }

  @Override
  public BaseResponse<City> updateCity(City city) {
    if (city == null || city.getId() == null) {
      return BaseResponse.error("City ID is required for update", 400);
    }
    City existing = getById(city.getId());
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("City not found with ID: " + city.getId(), 404);
    }
    boolean updated = lambdaUpdate()
        .eq(City::getId, city.getId())
        .set(StringUtils.isNotBlank(city.getName()), City::getName, city.getName())
        .set(city.getPostalCode() != null, City::getPostalCode, city.getPostalCode())
        .set(city.getDepartmentId() != null, City::getDepartmentId, city.getDepartmentId())
        .update();
    if (!updated) {
      return BaseResponse.error("Failed to update city");
    }
    City refreshed = getById(city.getId());
    return BaseResponse.success("City updated successfully", refreshed);
  }

  @Override
  public BaseResponse<Boolean> deleteCity(Long id) {
    if (id == null) {
      return BaseResponse.error("City ID is required", 400);
    }
    City existing = getById(id);
    if (existing == null || Boolean.TRUE.equals(existing.getIsDeleted())) {
      return BaseResponse.error("City not found with ID: " + id, 404);
    }
    boolean deleted = removeById(id);
    if (!deleted) {
      return BaseResponse.error("Failed to delete city");
    }
    return BaseResponse.success("City deleted successfully", true);
  }

  @Override
  public BaseResponse<City> getCityWithDetails(Long id) {
    if (id == null) {
      return BaseResponse.error("City ID is required", 400);
    }
    City city = getById(id);
    if (city == null || Boolean.TRUE.equals(city.getIsDeleted())) {
      return BaseResponse.error("City not found with ID: " + id, 404);
    }
    return BaseResponse.success("City retrieved successfully", city);
  }
}
