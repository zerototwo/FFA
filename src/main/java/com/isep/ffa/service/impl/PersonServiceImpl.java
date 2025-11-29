package com.isep.ffa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.util.StringUtils;

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

    /**
     * Helper method to populate the Role object for a Person
     * This resolves the "No Role" display issue in frontend
     */
    private void enrichPerson(Person person) {
        if (person == null) {
            return;
        }

        // Manually fetch and set Role if roleId is present
        if (person.getRoleId() != null) {
            Role role = roleMapper.selectById(person.getRoleId());
            person.setRole(role);
        }

        // Ensure isDeleted is not null
        if (person.getIsDeleted() == null) {
            person.setIsDeleted(false);
        }
    }

    /**
     * Helper method to populate Role objects for a list of Persons
     */
    private void enrichPersons(List<Person> persons) {
        if (persons != null) {
            persons.forEach(this::enrichPerson);
        }
    }

    /**
     * Helper method to handle role mapping before save/update
     * Maps nested role.id (from frontend) to person.roleId (database field)
     */
    private void handleRoleMapping(Person person) {
        // If frontend sends nested role object { id: 1 }, map it to roleId field
        if (person.getRole() != null && person.getRole().getId() != null) {
            person.setRoleId(person.getRole().getId());
        }
    }

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

    @Override
    public PagedResponse<Person> getPage(int page, int size) {
        Page<Person> pageReq = new Page<>(page, size);
        // Order by ID descending to see newest users first
        Page<Person> result = page(pageReq, new QueryWrapper<Person>().orderByDesc("id"));

        // Critical fix: Manually populate roles for the list
        enrichPersons(result.getRecords());

        return PagedResponse.of(
                result.getRecords(),
                (int) result.getCurrent() - 1,
                (int) result.getSize(),
                result.getTotal(),
                (int) result.getPages()
        );
    }

    @Override
    public BaseResponse<Person> getById(Long id) {
        Person person = super.getById(id);
        if (person != null) {
            enrichPerson(person); // Populate role for single retrieval
        }
        return BaseResponse.success("Person found", person);
    }

    @Override
    public BaseResponse<Person> findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return BaseResponse.error("Email is required", 400);
        }
        Person person = personMapper.findByEmail(email.trim());
        if (person == null) {
            return BaseResponse.error("Person not found with email: " + email, 404);
        }
        enrichPerson(person);
        return BaseResponse.success("Person found", person);
    }

    @Override
    public BaseResponse<Person> findByLogin(String login) {
        if (login == null || login.trim().isEmpty()) {
            return BaseResponse.error("Login is required", 400);
        }
        Person person = personMapper.findByLogin(login.trim());
        if (person == null) {
            return BaseResponse.error("Person not found with login: " + login, 404);
        }
        enrichPerson(person);
        return BaseResponse.success("Person found", person);
    }

    @Override
    public BaseResponse<List<Person>> findByRoleId(Long roleId) {
        if (roleId == null) {
            return BaseResponse.error("Role ID is required", 400);
        }
        List<Person> persons = personMapper.findByRoleId(roleId);
        if (persons == null || persons.isEmpty()) {
            return BaseResponse.success("No persons found with role ID: " + roleId, List.of());
        }
        enrichPersons(persons);
        return BaseResponse.success("Persons found", persons);
    }

    @Override
    public BaseResponse<List<Person>> findByCityId(Long cityId) {
        if (cityId == null) {
            return BaseResponse.error("City ID is required", 400);
        }
        List<Person> persons = personMapper.findByCityId(cityId);
        if (persons == null || persons.isEmpty()) {
            return BaseResponse.success("No persons found with city ID: " + cityId, List.of());
        }
        enrichPersons(persons);
        return BaseResponse.success("Persons found", persons);
    }

    @Override
    public BaseResponse<PagedResponse<Person>> getPersonsByRole(Long roleId, int page, int size) {
        if (roleId == null) {
            return BaseResponse.error("Role ID is required", 400);
        }
        int safePage = Math.max(page, 1);
        int safeSize = size <= 0 ? 10 : size;

        List<Person> allPersons = personMapper.findByRoleId(roleId);
        if (allPersons == null) {
            allPersons = List.of();
        }

        // Manual pagination
        int start = (safePage - 1) * safeSize;
        int end = Math.min(start + safeSize, allPersons.size());
        List<Person> pagedList = start < allPersons.size() ? allPersons.subList(start, end) : List.of();

        enrichPersons(pagedList);

        int totalPages = (int) Math.ceil((double) allPersons.size() / safeSize);
        PagedResponse<Person> pagedResponse = PagedResponse.of(
                pagedList,
                safePage - 1,
                safeSize,
                allPersons.size(),
                totalPages);

        return BaseResponse.success("Persons retrieved successfully", pagedResponse);
    }

    @Override
    public BaseResponse<PagedResponse<Person>> getPersonsByCity(Long cityId, int page, int size) {
        if (cityId == null) {
            return BaseResponse.error("City ID is required", 400);
        }
        int safePage = Math.max(page, 1);
        int safeSize = size <= 0 ? 10 : size;

        List<Person> allPersons = personMapper.findByCityId(cityId);
        if (allPersons == null) {
            allPersons = List.of();
        }

        // Manual pagination
        int start = (safePage - 1) * safeSize;
        int end = Math.min(start + safeSize, allPersons.size());
        List<Person> pagedList = start < allPersons.size() ? allPersons.subList(start, end) : List.of();

        enrichPersons(pagedList);

        int totalPages = (int) Math.ceil((double) allPersons.size() / safeSize);
        PagedResponse<Person> pagedResponse = PagedResponse.of(
                pagedList,
                safePage - 1,
                safeSize,
                allPersons.size(),
                totalPages);

        return BaseResponse.success("Persons retrieved successfully", pagedResponse);
    }

    @Override
    public BaseResponse<PagedResponse<Person>> searchPersons(String keyword, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = size <= 0 ? 10 : size;

        Page<Person> pageReq = new Page<>(safePage, safeSize);
        QueryWrapper<Person> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            String searchKeyword = "%" + keyword.trim() + "%";
            queryWrapper.and(wrapper -> wrapper
                    .like("first_name", searchKeyword)
                    .or()
                    .like("last_name", searchKeyword)
                    .or()
                    .like("email", searchKeyword)
                    .or()
                    .like("login", searchKeyword)
            );
        }
        
        // Ensure not deleted
        queryWrapper.eq("is_deleted", false);

        Page<Person> result = page(pageReq, queryWrapper);

        // Critical fix: Enrich search results with roles
        enrichPersons(result.getRecords());

        return BaseResponse.success("Persons found", PagedResponse.of(
                result.getRecords(),
                (int) result.getCurrent() - 1,
                (int) result.getSize(),
                result.getTotal(),
                (int) result.getPages()
        ));
    }

    @Override
    public BaseResponse<Person> createPerson(Person person) {
        if (person == null) {
            return BaseResponse.error("Invalid user data provided", 400);
        }

        // Critical fix: Handle role mapping from frontend input
        handleRoleMapping(person);

        // Check duplicate login
        if (person.getLogin() != null && personMapper.findByLogin(person.getLogin()) != null) {
            return BaseResponse.error("Login already in use", 409);
        }

        // Check duplicate email
        if (person.getEmail() != null && personMapper.findByEmail(person.getEmail()) != null) {
            return BaseResponse.error("Email already in use", 409);
        }

        // Assign default role if still not provided
        if (person.getRoleId() == null) {
            Role defaultRole = roleMapper.selectOne(
                    new QueryWrapper<Role>().eq("name", Constants.ROLE_USER));
            if (defaultRole != null) {
                person.setRoleId(defaultRole.getId());
            } else {
                // Fallback or error if default role not found
            }
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
            return BaseResponse.error("ID required for update", 400);
        }
        
        // Critical fix: Ensure roleId is updated from input
        handleRoleMapping(person);

        Person existing = personMapper.selectById(person.getId());
        if (existing == null) {
            return BaseResponse.error("User not found", 404);
        }

        // Handle password update: only update if new password provided
        if (person.getPassword() != null && !person.getPassword().isBlank()) {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        } else {
            person.setPassword(existing.getPassword());
        }

        boolean updated = updateById(person);
        if (!updated) {
            return BaseResponse.error("Failed to update user", 500);
        }

        // Re-fetch to return complete updated object with relations
        Person updatedPerson = personMapper.selectById(person.getId());
        enrichPerson(updatedPerson);
        
        return BaseResponse.success("User updated successfully", updatedPerson);
    }

    @Override
    public BaseResponse<Boolean> deletePerson(Long id) {
        if (id == null) {
            return BaseResponse.error("Person ID is required", 400);
        }
        boolean deleted = removeById(id);
        if (!deleted) {
            return BaseResponse.error("Failed to delete person", 500);
        }
        return BaseResponse.success("Person deleted successfully", true);
    }

    @Override
    public BaseResponse<Boolean> activatePerson(Long id) {
        if (id == null) {
            return BaseResponse.error("Person ID is required", 400);
        }
        boolean restored = lambdaUpdate()
                .eq(Person::getId, id)
                .set(Person::getIsDeleted, false)
                .update();
                
        if (!restored) {
            return BaseResponse.error("Failed to activate person or person not found", 500);
        }
        return BaseResponse.success("Person activated successfully", true);
    }

    @Override
    public BaseResponse<Boolean> deactivatePerson(Long id) {
        if (id == null) {
            return BaseResponse.error("Person ID is required", 400);
        }
        boolean deactivated = lambdaUpdate()
                .eq(Person::getId, id)
                .set(Person::getIsDeleted, true)
                .update();
                
        if (!deactivated) {
            return BaseResponse.error("Failed to deactivate person", 500);
        }
        return BaseResponse.success("Person deactivated successfully", true);
    }
}
