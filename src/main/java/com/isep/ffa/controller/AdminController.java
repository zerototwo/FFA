package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.dto.CountryRequest;
import com.isep.ffa.dto.CountryResponse;
import com.isep.ffa.entity.*;
import com.isep.ffa.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.isep.ffa.security.SecurityUtils;
import java.util.List;

/**
 * Admin Controller
 * Provides REST API endpoints for admin operations
 * Base path: /ffaAPI/admin
 */
@RestController
@RequestMapping("/ffaAPI/admin")
@Tag(name = "Admin API", description = "Administrative operations for FFA platform")
public class AdminController {

  @Autowired
  private PersonService personService;

  @Autowired
  private CountryService countryService;

  @Autowired
  private EmbassyService embassyService;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private ApplicationService applicationService;

  @Autowired
  private CityService cityService;

  @Autowired
  private RoleService roleService;

  /**
   * Check if current user is admin
   */
  private boolean checkAdmin() {
    Long currentUserId = SecurityUtils.getCurrentUserId();
    if (currentUserId == null) {
      return false;
    }
    return SecurityUtils.isAdmin();
  }

  // ==================== PERSON MANAGEMENT ====================

  /**
   * Get all persons with pagination
   */
  @GetMapping("/persons")
  @Operation(summary = "Get all persons", description = "Retrieve paginated list of all persons")
  public BaseResponse<PagedResponse<Person>> getAllPersons(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<Person> persons = personService.getPage(page + 1, size);
    return BaseResponse.success("Persons retrieved successfully", persons);
  }

  /**
   * Get person by ID
   */
  @GetMapping("/persons/{id}")
  @Operation(summary = "Get person by ID", description = "Retrieve person information by ID")
  public BaseResponse<Person> getPersonById(
      @Parameter(description = "Person ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    Person person = personService.getById(id);
    if (person == null) {
      return BaseResponse.error("Person not found with ID: " + id, 404);
    }
    return BaseResponse.success("Person found", person);
  }

  /**
   * Create new person
   */
  @PostMapping("/persons")
  @Operation(summary = "Create person", description = "Create a new person")
  public BaseResponse<Person> createPerson(@RequestBody Person person) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return personService.createPerson(person);
  }

  /**
   * Update person
   */
  @PutMapping("/persons/{id}")
  @Operation(summary = "Update person", description = "Update person information")
  public BaseResponse<Person> updatePerson(
      @Parameter(description = "Person ID") @PathVariable Long id,
      @RequestBody Person person) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    person.setId(id);
    return personService.updatePerson(person);
  }

  /**
   * Delete person
   */
  @DeleteMapping("/persons/{id}")
  @Operation(summary = "Delete person", description = "Delete person by ID")
  public BaseResponse<Boolean> deletePerson(
      @Parameter(description = "Person ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return personService.deletePerson(id);
  }

  /**
   * Search persons
   */
  @GetMapping("/persons/search")
  @Operation(summary = "Search persons", description = "Search persons by keyword")
  public BaseResponse<PagedResponse<Person>> searchPersons(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return personService.searchPersons(keyword, page + 1, size);
  }

  // ==================== COUNTRY MANAGEMENT ====================

  /**
   * Get all countries
   */
  @GetMapping("/countries")
  @Operation(summary = "Get all countries", description = "Retrieve paginated list of all countries")
  public BaseResponse<PagedResponse<Country>> getAllCountries(
      @Parameter(description = "Page number") @RequestParam(defaultValue = "1") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<Country> pagedResponse = countryService.getPage(page, size);
    return BaseResponse.success("Countries retrieved successfully", pagedResponse);
  }

  /**
   * Create new country
   */
  @PostMapping("/countries")
  @Operation(summary = "Create country", description = "Create a new country")
  public BaseResponse<Country> createCountry(@RequestBody CountryRequest countryRequest) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    // Convert DTO to Entity
    Country country = new Country();
    country.setName(countryRequest.getName());
    country.setPhoneNumberIndicator(countryRequest.getPhoneNumberIndicator());
    country.setContinentId(countryRequest.getContinentId());

    return countryService.createCountry(country);
  }

  /**
   * Update country
   */
  @PutMapping("/countries/{id}")
  @Operation(summary = "Update country", description = "Update country information")
  public BaseResponse<Country> updateCountry(
      @Parameter(description = "Country ID") @PathVariable Long id,
      @RequestBody CountryRequest countryRequest) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    // Convert DTO to Entity
    Country country = new Country();
    country.setId(id);
    country.setName(countryRequest.getName());
    country.setPhoneNumberIndicator(countryRequest.getPhoneNumberIndicator());
    country.setContinentId(countryRequest.getContinentId());

    return countryService.updateCountry(country);
  }

  /**
   * Delete country
   */
  @DeleteMapping("/countries/{id}")
  @Operation(summary = "Delete country", description = "Delete country by ID")
  public BaseResponse<Boolean> deleteCountry(
      @Parameter(description = "Country ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return countryService.deleteCountry(id);
  }

  // ==================== EMBASSY MANAGEMENT ====================

  /**
   * Get all embassies
   */
  @GetMapping("/embassies")
  @Operation(summary = "Get all embassies", description = "Retrieve paginated list of all embassies")
  public BaseResponse<PagedResponse<Embassy>> getAllEmbassies(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<Embassy> embassies = embassyService.getPage(page + 1, size);
    return BaseResponse.success("Embassies retrieved successfully", embassies);
  }

  /**
   * Create new embassy
   */
  @PostMapping("/embassies")
  @Operation(summary = "Create embassy", description = "Create a new embassy")
  public BaseResponse<Embassy> createEmbassy(@RequestBody Embassy embassy) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return embassyService.createEmbassy(embassy);
  }

  /**
   * Update embassy
   */
  @PutMapping("/embassies/{id}")
  @Operation(summary = "Update embassy", description = "Update embassy information")
  public BaseResponse<Embassy> updateEmbassy(
      @Parameter(description = "Embassy ID") @PathVariable Long id,
      @RequestBody Embassy embassy) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    embassy.setId(id);
    return embassyService.updateEmbassy(embassy);
  }

  /**
   * Delete embassy
   */
  @DeleteMapping("/embassies/{id}")
  @Operation(summary = "Delete embassy", description = "Delete embassy by ID")
  public BaseResponse<Boolean> deleteEmbassy(
      @Parameter(description = "Embassy ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return embassyService.deleteEmbassy(id);
  }

  // ==================== PROJECT MANAGEMENT ====================

  /**
   * Get all projects
   */
  @GetMapping("/projects")
  @Operation(summary = "Get all projects", description = "Retrieve paginated list of all projects")
  public BaseResponse<PagedResponse<Project>> getAllProjects(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<Project> projects = projectService.getPage(page + 1, size);
    return BaseResponse.success("Projects retrieved successfully", projects);
  }

  /**
   * Get project by ID
   */
  @GetMapping("/projects/{id}")
  @Operation(summary = "Get project by ID", description = "Retrieve project information by ID")
  public BaseResponse<Project> getProjectById(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.getProjectWithDetails(id);
  }

  /**
   * Define project winner
   */
  @PostMapping("/projects/{id}/winner")
  @Operation(summary = "Define project winner", description = "Define winner for a project")
  public BaseResponse<Boolean> defineProjectWinner(
      @Parameter(description = "Project ID") @PathVariable Long id,
      @Parameter(description = "Winner user ID") @RequestParam Long winnerUserId) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.defineWinner(id, winnerUserId);
  }

  /**
   * Get project statistics
   */
  @GetMapping("/projects/{id}/statistics")
  @Operation(summary = "Get project statistics", description = "Get statistics for a project")
  public BaseResponse<Object> getProjectStatistics(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.getProjectStatistics(id);
  }

  // ==================== APPLICATION MANAGEMENT ====================

  /**
   * Get all applications
   */
  @GetMapping("/applications")
  @Operation(summary = "Get all applications", description = "Retrieve paginated list of all applications")
  public BaseResponse<PagedResponse<Application>> getAllApplications(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<Application> applications = applicationService.getPage(page + 1, size);
    return BaseResponse.success("Applications retrieved successfully", applications);
  }

  /**
   * Get applications by project
   */
  @GetMapping("/projects/{projectId}/applications")
  @Operation(summary = "Get applications by project", description = "Retrieve applications for a specific project")
  public BaseResponse<PagedResponse<Application>> getApplicationsByProject(
      @Parameter(description = "Project ID") @PathVariable Long projectId,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return applicationService.getApplicationsByProject(projectId, page + 1, size);
  }

  // ==================== CITY MANAGEMENT ====================

  /**
   * Get all cities
   */
  @GetMapping("/cities")
  @Operation(summary = "Get all cities", description = "Retrieve paginated list of all cities")
  public BaseResponse<PagedResponse<City>> getAllCities(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<City> cities = cityService.getPage(page + 1, size);
    return BaseResponse.success("Cities retrieved successfully", cities);
  }

  /**
   * Create new city
   */
  @PostMapping("/cities")
  @Operation(summary = "Create city", description = "Create a new city")
  public BaseResponse<City> createCity(@RequestBody City city) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return cityService.createCity(city);
  }

  /**
   * Update city
   */
  @PutMapping("/cities/{id}")
  @Operation(summary = "Update city", description = "Update city information")
  public BaseResponse<City> updateCity(
      @Parameter(description = "City ID") @PathVariable Long id,
      @RequestBody City city) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    city.setId(id);
    return cityService.updateCity(city);
  }

  /**
   * Delete city
   */
  @DeleteMapping("/cities/{id}")
  @Operation(summary = "Delete city", description = "Delete city by ID")
  public BaseResponse<Boolean> deleteCity(
      @Parameter(description = "City ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return cityService.deleteCity(id);
  }

  // ==================== ROLE MANAGEMENT ====================

  /**
   * Get all roles
   */
  @GetMapping("/roles")
  @Operation(summary = "Get all roles", description = "Retrieve list of all roles")
  public BaseResponse<List<Role>> getAllRoles() {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return roleService.getAllRoles();
  }

  /**
   * Create new role
   */
  @PostMapping("/roles")
  @Operation(summary = "Create role", description = "Create a new role")
  public BaseResponse<Role> createRole(@RequestBody Role role) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return roleService.createRole(role);
  }

  /**
   * Update role
   */
  @PutMapping("/roles/{id}")
  @Operation(summary = "Update role", description = "Update role information")
  public BaseResponse<Role> updateRole(
      @Parameter(description = "Role ID") @PathVariable Long id,
      @RequestBody Role role) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    role.setId(id);
    return roleService.updateRole(role);
  }

  /**
   * Delete role
   */
  @DeleteMapping("/roles/{id}")
  @Operation(summary = "Delete role", description = "Delete role by ID")
  public BaseResponse<Boolean> deleteRole(
      @Parameter(description = "Role ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return roleService.deleteRole(id);
  }
}
