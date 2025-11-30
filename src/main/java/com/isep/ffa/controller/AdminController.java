package com.isep.ffa.controller;

import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.dto.CountryRequest;
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

  @Autowired
  private AnnouncementService announcementService;

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

  // ==================== ANNOUNCEMENT MANAGEMENT====================

  /**
   * Get all announcements (List)
   * URL: GET /ffaAPI/admin/announcements
   */
  @GetMapping("/announcements")
  @Operation(summary = "Get all announcements", description = "Retrieve paginated list of announcements")
  public BaseResponse<PagedResponse<Announcement>> getAnnouncements(
          @Parameter(description = "Search keyword") @RequestParam(required = false) String keyword,
          @Parameter(description = "Status filter") @RequestParam(required = false) String status,
          @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
          @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {

    if (!checkAdmin()) return BaseResponse.error("Admin access required", 403);
    return announcementService.searchAnnouncements(keyword, status, page + 1, size);
  }

  /**
   * Create announcement
   * URL: POST /ffaAPI/admin/announcements
   */
  @PostMapping("/announcements")
  @Operation(summary = "Create announcement", description = "Create a new announcement (Draft or Published)")
  public BaseResponse<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
    if (!checkAdmin()) return BaseResponse.error("Admin access required", 403);
    return announcementService.createAnnouncement(announcement);
  }

  /**
   * Update announcement
   * URL: PUT /ffaAPI/admin/announcements/{id}
   */
  @PutMapping("/announcements/{id}")
  @Operation(summary = "Update announcement")
  public BaseResponse<Announcement> updateAnnouncement(
          @PathVariable Long id,
          @RequestBody Announcement announcement) {
    if (!checkAdmin()) return BaseResponse.error("Admin access required", 403);
    announcement.setId(id);
    return announcementService.updateAnnouncement(announcement);
  }

  /**
   * Delete announcement
   * URL: DELETE /ffaAPI/admin/announcements/{id}
   */
  @DeleteMapping("/announcements/{id}")
  @Operation(summary = "Delete announcement")
  public BaseResponse<Boolean> deleteAnnouncement(@PathVariable Long id) {
    if (!checkAdmin()) return BaseResponse.error("Admin access required", 403);
    return announcementService.deleteAnnouncement(id);
  }

  /**
   * Publish announcement (Optional specific endpoint)
   * URL: POST /ffaAPI/admin/announcements/{id}/publish
   */
  @PostMapping("/announcements/{id}/publish")
  @Operation(summary = "Publish announcement")
  public BaseResponse<Boolean> publishAnnouncement(@PathVariable Long id) {
    if (!checkAdmin()) return BaseResponse.error("Admin access required", 403);
    return announcementService.publishAnnouncement(id);
  }

  // ==================== PERSON MANAGEMENT ====================

  @GetMapping("/persons")
  @Operation(summary = "Get all persons", description = "Retrieve paginated list of persons, optionally filtered by role")
  public BaseResponse<PagedResponse<Person>> getAllPersons(
          @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
          @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
          @Parameter(description = "Role ID filter") @RequestParam(required = false) Long roleId) {

    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }

    if (roleId != null && roleId > 0) {
      return personService.getPersonsByRole(roleId, page + 1, size);
    } else {
      return personService.getAllPersonsEnriched(page + 1, size);
    }
  }

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

  @PostMapping("/persons")
  @Operation(summary = "Create person", description = "Create a new person")
  public BaseResponse<Person> createPerson(@RequestBody Person person) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return personService.createPerson(person);
  }

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

  @DeleteMapping("/persons/{id}")
  @Operation(summary = "Delete person", description = "Delete person by ID")
  public BaseResponse<Boolean> deletePerson(
      @Parameter(description = "Person ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return personService.deletePerson(id);
  }

  @GetMapping("/persons/search")
  @Operation(summary = "Search persons", description = "Search persons by keyword with optional role filter")
  public BaseResponse<PagedResponse<Person>> searchPersons(
          @Parameter(description = "Search keyword") @RequestParam String keyword,
          @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
          @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
          @Parameter(description = "Role ID filter") @RequestParam(required = false) Long roleId) {

    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }

    return personService.searchPersons(keyword, page + 1, size, roleId);
  }

  // ==================== COUNTRY MANAGEMENT ====================

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

  @PostMapping("/countries")
  @Operation(summary = "Create country", description = "Create a new country")
  public BaseResponse<Country> createCountry(@RequestBody CountryRequest countryRequest) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    Country country = new Country();
    country.setName(countryRequest.getName());
    country.setPhoneNumberIndicator(countryRequest.getPhoneNumberIndicator());
    country.setContinentId(countryRequest.getContinentId());

    return countryService.createCountry(country);
  }

  @PutMapping("/countries/{id}")
  @Operation(summary = "Update country", description = "Update country information")
  public BaseResponse<Country> updateCountry(
      @Parameter(description = "Country ID") @PathVariable Long id,
      @RequestBody CountryRequest countryRequest) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    Country country = new Country();
    country.setId(id);
    country.setName(countryRequest.getName());
    country.setPhoneNumberIndicator(countryRequest.getPhoneNumberIndicator());
    country.setContinentId(countryRequest.getContinentId());

    return countryService.updateCountry(country);
  }

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

  @PostMapping("/embassies")
  @Operation(summary = "Create embassy", description = "Create a new embassy")
  public BaseResponse<Embassy> createEmbassy(@RequestBody Embassy embassy) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return embassyService.createEmbassy(embassy);
  }

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

  @GetMapping("/projects/search")
  @Operation(summary = "Search projects", description = "Search projects by keyword")
  public BaseResponse<PagedResponse<Project>> searchProjects(
      @Parameter(description = "Search keyword") @RequestParam String keyword,
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.searchByDescription(keyword, page, size);
  }

  @PostMapping("/projects")
  @Operation(summary = "Create project", description = "Create a new project")
  public BaseResponse<Project> createProject(@RequestBody Project project) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.createProject(project);
  }

  @PutMapping("/projects/{id}")
  @Operation(summary = "Update project", description = "Update project information")
  public BaseResponse<Project> updateProject(
      @Parameter(description = "Project ID") @PathVariable Long id,
      @RequestBody Project project) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    project.setId(id);
    return projectService.updateProject(project);
  }

  @DeleteMapping("/projects/{id}")
  @Operation(summary = "Delete project", description = "Delete project by ID")
  public BaseResponse<Boolean> deleteProject(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.deleteProject(id);
  }

  @GetMapping("/projects/{id:\\d+}")
  @Operation(summary = "Get project by ID", description = "Retrieve project information by ID")
  public BaseResponse<Project> getProjectById(
      @Parameter(description = "Project ID") @PathVariable Long id) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return projectService.getProjectWithDetails(id);
  }

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

  @GetMapping("/cities")
  @Operation(summary = "Get all cities", description = "Retrieve paginated list of all cities")
  public BaseResponse<PagedResponse<City>> getAllCities(
      @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    PagedResponse<City> cities = cityService.getPage(page, size);
    return BaseResponse.success("Cities retrieved successfully", cities);
  }

  @PostMapping("/cities")
  @Operation(summary = "Create city", description = "Create a new city")
  public BaseResponse<City> createCity(@RequestBody City city) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return cityService.createCity(city);
  }

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

  @GetMapping("/roles")
  @Operation(summary = "Get all roles", description = "Retrieve list of all roles")
  public BaseResponse<List<Role>> getAllRoles() {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return roleService.getAllRoles();
  }

  @PostMapping("/roles")
  @Operation(summary = "Create role", description = "Create a new role")
  public BaseResponse<Role> createRole(@RequestBody Role role) {
    if (!checkAdmin()) {
      return BaseResponse.error("Admin access required", 403);
    }
    return roleService.createRole(role);
  }

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
