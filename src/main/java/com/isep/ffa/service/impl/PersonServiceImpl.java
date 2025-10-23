package com.isep.ffa.service.impl;

import com.isep.ffa.entity.Person;
import com.isep.ffa.mapper.PersonMapper;
import com.isep.ffa.service.PersonService;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Person Service Implementation
 * Implements business logic for person management
 */
@Service
public class PersonServiceImpl extends BaseServiceImpl<PersonMapper, Person> implements PersonService {

  @Autowired
  private PersonMapper personMapper;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Delegate to CustomUserDetailsService
    CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
    customUserDetailsService.setPersonService(this);
    return customUserDetailsService.loadUserByUsername(username);
  }

  @Override
  public Optional<Person> findByLoginOrEmail(String loginOrEmail) {
    // TODO: Implement business logic
    // Try to find by login first, then by email
    Person personByLogin = personMapper.findByLogin(loginOrEmail);
    if (personByLogin != null) {
      return Optional.of(personByLogin);
    }

    Person personByEmail = personMapper.findByEmail(loginOrEmail);
    if (personByEmail != null) {
      return Optional.of(personByEmail);
    }

    return Optional.empty();
  }

  @Override
  public BaseResponse<Person> findByEmail(String email) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Person> findByLogin(String login) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<List<Person>> findByRoleId(Long roleId) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<List<Person>> findByCityId(Long cityId) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<PagedResponse<Person>> getPersonsByRole(Long roleId, int page, int size) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<PagedResponse<Person>> getPersonsByCity(Long cityId, int page, int size) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<PagedResponse<Person>> searchPersons(String keyword, int page, int size) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Person> createPerson(Person person) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Person> updatePerson(Person person) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Boolean> deletePerson(Long id) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Boolean> activatePerson(Long id) {
    // TODO: Implement business logic
    return null;
  }

  @Override
  public BaseResponse<Boolean> deactivatePerson(Long id) {
    // TODO: Implement business logic
    return null;
  }
}
