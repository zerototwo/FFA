package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.*;
import com.isep.ffa.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Public Controller
 * Provides REST API endpoints for public operations (no authentication
 * required)
 * Base path: /ffaAPI/public
 */
@RestController
@RequestMapping("/public")
@Tag(name = "Public API", description = "Public operations accessible without authentication")
public class PublicController {

  @Autowired
  private CountryService countryService;

  @Autowired
  private EmbassyService embassyService;

  @Autowired
  private InstitutionService institutionService;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private CityService cityService;

  // ==================== COUNTRY INFORMATION ====================

  /**
   * Get all countries
   */
  @GetMapping("/countries")
  @Operation(summary = "Get all countries", description = "Retrieve list of all countries")
  public BaseResponse<List<Country>> getAllCountries() {
    return countryService.getCountriesWithEmbassies();
  }

  /**
   * Get country by ID
   */
  @GetMapping("/countries/{id}")
  @Operation(summary = "Get country by ID", description = "Retrieve country information by ID")
  public BaseResponse<Country> getCountryById(
      @Parameter(description = "Country ID") @PathVariable Long id) {
    Country country = countryService.getById(id);
    return country != null
        ? BaseResponse.success("Country found", country)
        : BaseResponse.error("Country not found with ID: " + id, 404);
  }

  /**
   * Search countries
   */
  @GetMapping("/countries/search")
  @Operation(summary = "Search countries", description = "Search countries by keyword")
  public BaseResponse<PagedResponse<Country>> searchCountries(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return countryService.searchCountries(keyword, page, size);
  }

    /**
     *  Get countries by continent by id
     * @param continentId  long
     * @return List<Country>
     */
  @GetMapping("/countries/continent/{continentId}")
  @Operation(summary = "Get countries by continent", description = "Retrieve countries by continent ID")
  public BaseResponse<List<Country>> getCountriesByContinent(
      @Parameter(description = "Continent ID") @PathVariable Long continentId) {

    return countryService.findByContinentId(continentId);
  }

  /**
   * Get paginated countries by continent
   */
  @GetMapping("/countries/continent/{continentId}/paginated")
  @Operation(summary = "Get paginated countries by continent", description = "Retrieve paginated countries by continent ID")
  public BaseResponse<PagedResponse<Country>> getPaginatedCountriesByContinent(
      @Parameter(description = "Continent ID") @PathVariable Long continentId,
      @Parameter(description = "Page number") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return countryService.getCountriesByContinent(continentId, page, size);
  }

  // ==================== EMBASSY INFORMATION ====================

  /**
   * Get all embassies
   */
  @GetMapping("/embassies")
  @Operation(summary = "Get all embassies", description = "Retrieve paginated list of all embassies")
  public BaseResponse<PagedResponse<Embassy>> getAllEmbassies(
      @Parameter(description = "Page number (starting from 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    PagedResponse<Embassy> response = embassyService.getPage(page, size);
    return BaseResponse.success("Embassies retrieved successfully", response);
  }

  /**
   * Get embassy by ID
   */
  @GetMapping("/embassies/{id}")
  @Operation(summary = "Get embassy by ID", description = "Retrieve embassy information by ID")
  public BaseResponse<Embassy> getEmbassyById(
      @Parameter(description = "Embassy ID") @PathVariable Long id) {
    return embassyService.getEmbassyWithDetails(id);
  }

  /**
   * Get embassies by country
   */
  @GetMapping("/countries/{countryId}/embassies")
  @Operation(summary = "Get embassies by country", description = "Retrieve embassies for a specific country")
  public BaseResponse<List<Embassy>> getEmbassiesByCountry(
      @Parameter(description = "Country ID") @PathVariable Long countryId) {
    return embassyService.findByEmbassyInCountryId(countryId);
  }

  /**
   * Search embassies
   */
  @GetMapping("/embassies/search")
  @Operation(summary = "Search embassies", description = "Search embassies by address")
  public BaseResponse<PagedResponse<Embassy>> searchEmbassies(
      @Parameter(description = "Address keyword") @RequestParam String address,
      @Parameter(description = "Page number (starting from 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return embassyService.searchByAddress(address, page, size);
  }

  // ==================== INSTITUTION INFORMATION ====================

  /**
   * Get all institutions
   */
  @GetMapping("/institutions")
  @Operation(summary = "Get all institutions", description = "Retrieve paginated list of all institutions")
  public BaseResponse<PagedResponse<Institution>> getAllInstitutions(
      @Parameter(description = "Page number (starting from 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    PagedResponse<Institution> response = institutionService.getPage(page, size);
    return BaseResponse.success("Institutions retrieved successfully", response);
  }

  /**
   * Get institution by ID
   */
  @GetMapping("/institutions/{id}")
  @Operation(summary = "Get institution by ID", description = "Retrieve institution information by ID")
  public BaseResponse<Institution> getInstitutionById(
      @Parameter(description = "Institution ID") @PathVariable Long id) {
    Institution institution = institutionService.getById(id);
    return institution != null
        ? BaseResponse.success("Institution found", institution)
        : BaseResponse.error("Institution not found with ID: " + id, 404);
  }

  /**
   * Get institutions by city
   */
  @GetMapping("/cities/{cityId}/institutions")
  @Operation(summary = "Get institutions by city", description = "Retrieve institutions for a specific city")
  public BaseResponse<List<Institution>> getInstitutionsByCity(
      @Parameter(description = "City ID") @PathVariable Long cityId) {
    return institutionService.findByCityId(cityId);
  }

  /**
   * Search institutions
   */
  @GetMapping("/institutions/search")
  @Operation(summary = "Search institutions", description = "Search institutions by name keyword")
  public BaseResponse<PagedResponse<Institution>> searchInstitutions(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (starting from 1)") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return institutionService.searchByName(keyword, page, size);
  }

  // ==================== PROJECT INFORMATION ====================

  /**
   * Get available projects
   */
  @GetMapping("/projects")
  @Operation(summary = "Get available projects", description = "Retrieve paginated list of available projects")
  public BaseResponse<PagedResponse<Project>> getAvailableProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return projectService.getAvailableProjects(page, size);
  }

  /**
   * Get project by ID
   */
  @GetMapping("/projects/{id}")
  @Operation(summary = "Get project by ID", description = "Retrieve project information by ID")
  public BaseResponse<Project> getProjectById(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    return projectService.getProjectWithDetails(id);
  }

  /**
   * Search projects
   */
  @GetMapping("/projects/search")
  @Operation(summary = "Search projects", description = "Search available projects by keyword")
  public BaseResponse<PagedResponse<Project>> searchProjects(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return projectService.searchByDescription(keyword, page, size);
  }

  // ==================== CITY INFORMATION ====================

  /**
   * Get all cities
   */
  @GetMapping("/cities")
  @Operation(summary = "Get all cities", description = "Retrieve paginated list of all cities")
  public BaseResponse<PagedResponse<City>> getAllCities(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    PagedResponse<City> cities = cityService.getPage(page, size);
    return BaseResponse.success("Cities retrieved successfully", cities);
  }

  /**
   * Get city by ID
   */
  @GetMapping("/cities/{id}")
  @Operation(summary = "Get city by ID", description = "Retrieve city information by ID")
  public BaseResponse<City> getCityById(
      @Parameter(description = "City ID") @PathVariable Long id) {
    return cityService.getCityWithDetails(id);
  }

  /**
   * Search cities
   */
  @GetMapping("/cities/search")
  @Operation(summary = "Search cities", description = "Search cities by keyword")
  public BaseResponse<PagedResponse<City>> searchCities(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    return cityService.searchCities(keyword, page, size);
  }

  // ==================== SYSTEM INFORMATION ====================

  /**
   * Get system health
   */
  @GetMapping("/health")
  @Operation(summary = "System health check", description = "Check system health status")
  public BaseResponse<Object> getHealth() {
    Map<String, Object> health = new HashMap<>();
    health.put("status", "UP");
    health.put("timestamp", Instant.now());
    long uptimeMillis = ManagementFactory.getRuntimeMXBean().getUptime();
    health.put("uptime", Duration.ofMillis(uptimeMillis).toString());
    return BaseResponse.success("System is healthy", health);
  }

  /**
   * Get system version
   */
  @GetMapping("/version")
  @Operation(summary = "Get system version", description = "Get system version information")
  public BaseResponse<Object> getVersion() {
    Map<String, Object> versionInfo = new HashMap<>();
    versionInfo.put("application", "FFA Platform");
    versionInfo.put("version", "0.0.1-SNAPSHOT");
    versionInfo.put("buildTime", Instant.now());
    return BaseResponse.success("Version information retrieved", versionInfo);
  }
}
