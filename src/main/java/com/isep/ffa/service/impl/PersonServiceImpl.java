package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.isep.ffa.dto.BaseResponse;
import com.isep.ffa.dto.PagedResponse;
import com.isep.ffa.entity.Person;
import com.isep.ffa.entity.Role;
import com.isep.ffa.mapper.PersonMapper;
import com.isep.ffa.mapper.RoleMapper;
import com.isep.ffa.security.CustomUserDetailsService;
import com.isep.ffa.service.PersonService;
import com.isep.ffa.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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

  @Autowired
  private RoleMapper roleMapper;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Person person = findByLoginOrEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new CustomUserDetailsService.CustomUserPrincipal(person);
  }

  @Override
  public Optional<Person> findByLoginOrEmail(String loginOrEmail) {
    Person personByLogin = personMapper.findByLogin(loginOrEmail);
    if (personByLogin != null) {
      enrichPerson(personByLogin);
      return Optional.of(personByLogin);
    }

    Person personByEmail = personMapper.findByEmail(loginOrEmail);
    if (personByEmail != null) {
      enrichPerson(personByEmail);
      return Optional.of(personByEmail);
    }

    return Optional.empty();
  }

  private void enrichPerson(Person person) {
    if (person == null) {
      return;
    }

    if (person.getRoleId() != null) {
      Role role = roleMapper.selectById(person.getRoleId());
      person.setRole(role);
    }

    if (person.getIsDeleted() == null) {
      person.setIsDeleted(false);
    }
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
    if (person == null) {
      return BaseResponse.error("Invalid user data provided", 400);
    }

    // Check duplicate login
    if (person.getLogin() != null && personMapper.findByLogin(person.getLogin()) != null) {
      return BaseResponse.error("Login already in use", 409);
    }

    // Check duplicate email
    if (person.getEmail() != null && personMapper.findByEmail(person.getEmail()) != null) {
      return BaseResponse.error("Email already in use", 409);
    }

    // Assign default role if not provided
    if (person.getRoleId() == null) {
      Role defaultRole = roleMapper.selectOne(
          new QueryWrapper<Role>().eq("name", Constants.ROLE_USER));
      if (defaultRole == null) {
        return BaseResponse.error("Default role USER is not configured", 500);
      }
      person.setRoleId(defaultRole.getId());
      person.setRole(defaultRole);
    } else {
      Role role = roleMapper.selectById(person.getRoleId());
      person.setRole(role);
    }

    person.setIsDeleted(false);

    if (person.getPassword() != null) {
      person.setPassword(passwordEncoder.encode(person.getPassword()));
    }

    // Normalise organisation information
    if (person.getOrganizationType() == null) {
      if (person.getOrganizationId() != null) {
        person.setOrganizationType("EMBASSY");
      } else if (person.getOrganizationName() != null && !person.getOrganizationName().isBlank()) {
        person.setOrganizationType("OTHER");
      }
    }
    if (person.getOrganizationName() != null && person.getOrganizationName().isBlank()) {
      person.setOrganizationName(null);
    }

    boolean saved = save(person);
    if (!saved) {
      return BaseResponse.error("Failed to create user", 500);
    }

    enrichPerson(person);
    return BaseResponse.success("User registered successfully", person);
  }

  @Override
  public BaseResponse<Person> updatePerson(Person person) {
    if (person == null || person.getId() == null) {
      return BaseResponse.error("Invalid user data provided", 400);
    }

    Person existing = personMapper.selectById(person.getId());
    if (existing == null) {
      return BaseResponse.error("User not found", 404);
    }

    if (person.getPassword() != null && !person.getPassword().isBlank()) {
      person.setPassword(passwordEncoder.encode(person.getPassword()));
    } else {
      person.setPassword(existing.getPassword());
    }

    boolean updated = updateById(person);
    if (!updated) {
      return BaseResponse.error("Failed to update user", 500);
    }

    enrichPerson(person);
    return BaseResponse.success("User updated successfully", person);
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
