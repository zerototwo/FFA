package com.isep.ffa.security;

import com.isep.ffa.entity.Person;
import com.isep.ffa.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom User Details Service
 * Loads user details for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  private PersonService personService;

  public void setPersonService(PersonService personService) {
    this.personService = personService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Find person by login or email
    Person person = personService.findByLoginOrEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    return new CustomUserPrincipal(person);
  }

  /**
   * Custom User Principal
   * Implements UserDetails interface
   */
  public static class CustomUserPrincipal implements UserDetails {
    private Person person;

    public CustomUserPrincipal(Person person) {
      this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
      // Get authorities based on role
      if (person.getRole() != null && person.getRole().getName() != null) {
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + person.getRole().getName().toUpperCase()));
      }
      return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
      return person.getPassword();
    }

    @Override
    public String getUsername() {
      return person.getLogin() != null ? person.getLogin() : person.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
      return true;
    }

    @Override
    public boolean isAccountNonLocked() {
      return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      return true;
    }

    @Override
    public boolean isEnabled() {
      return person.getIsDeleted() == null || !person.getIsDeleted();
    }

    // Getter for Person entity
    public Person getPerson() {
      return person;
    }

    // Getter for Person ID
    public Long getPersonId() {
      return person.getId();
    }

    // Getter for Role
    public String getRole() {
      return person.getRole() != null ? person.getRole().getName() : null;
    }
  }
}
