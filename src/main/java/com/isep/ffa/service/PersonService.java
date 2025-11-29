package com.isep.ffa.service;

import com.isep.ffa.entity.Person;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

/**
 * Person Service Interface
 * Provides business logic for person management
 */
public interface PersonService extends BaseService<Person>, UserDetailsService {

  /**
   * Find person by login or email
   * 
   * @param loginOrEmail login name or email
   * @return person information
   */
  Optional<Person> findByLoginOrEmail(String loginOrEmail);

  /**
   * Find person by email
   * 
   * @param email person email
   * @return person information
   */
  BaseResponse<Person> findByEmail(String email);

  /**
   * Find person by login name
   * 
   * @param login person login name
   * @return person information
   */
  BaseResponse<Person> findByLogin(String login);

  /**
   * Find persons by role ID
   * 
   * @param roleId role ID
   * @return list of persons
   */
  BaseResponse<List<Person>> findByRoleId(Long roleId);

  /**
   * Find persons by city ID
   * 
   * @param cityId city ID
   * @return list of persons
   */
  BaseResponse<List<Person>> findByCityId(Long cityId);

  /**
   * Get paginated persons by role
   * 
   * @param roleId role ID
   * @param page   page number
   * @param size   page size
   * @return paginated persons
   */
  BaseResponse<PagedResponse<Person>> getPersonsByRole(Long roleId, int page, int size);

  /**
   * Get paginated persons by city
   * 
   * @param cityId city ID
   * @param page   page number
   * @param size   page size
   * @return paginated persons
   */
  BaseResponse<PagedResponse<Person>> getPersonsByCity(Long cityId, int page, int size);

  /**
   * Search persons by keyword
   * 
   * @param keyword search keyword
   * @param page    page number
   * @param size    page size
   * @return paginated search results
   */
  BaseResponse<PagedResponse<Person>> searchPersons(String keyword, int page, int size, Long roleId);

  /**
   * Create new person
   * 
   * @param person person information
   * @return created person
   */
  BaseResponse<Person> createPerson(Person person);

  /**
   * Update person information
   * 
   * @param person person information
   * @return updated person
   */
  BaseResponse<Person> updatePerson(Person person);

  /**
   * Delete person by ID
   * 
   * @param id person ID
   * @return operation result
   */
  BaseResponse<Boolean> deletePerson(Long id);

  /**
   * Activate person account
   * 
   * @param id person ID
   * @return operation result
   */
  BaseResponse<Boolean> activatePerson(Long id);

  /**
   * Deactivate person account
   * 
   * @param id person ID
   * @return operation result
   */
  BaseResponse<Boolean> deactivatePerson(Long id);

  BaseResponse<PagedResponse<Person>> getAllPersonsEnriched(int page, int size);
}
