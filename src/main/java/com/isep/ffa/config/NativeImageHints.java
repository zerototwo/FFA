package com.isep.ffa.config;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import com.isep.ffa.entity.*;

/**
 * Native Image Runtime Hints Configuration
 * Registers all necessary reflection, resource, and serialization hints for
 * GraalVM Native Image
 */
public class NativeImageHints implements RuntimeHintsRegistrar {

  @Override
  public void registerHints(@NonNull RuntimeHints hints, @Nullable ClassLoader classLoader) {
    // Register entity classes for reflection
    registerEntityClasses(hints);

    // Register PostgreSQL driver
    registerPostgreSQLDriver(hints, classLoader);

    // Register resource patterns
    registerResourcePatterns(hints);

    // Register serialization types
    registerSerializationTypes(hints);

    // Register JWT classes
    registerJWTClasses(hints, classLoader);
  }

  private void registerEntityClasses(RuntimeHints hints) {
    // Register all entity classes for reflection
    Class<?>[] entityClasses = {
        Application.class,
        Project.class,
        Person.class,
        User.class,
        Intervener.class,
        Role.class,
        DocumentType.class,
        DocumentsSubmitted.class,
        DocumentsNeedForProject.class,
        City.class,
        Country.class,
        Embassy.class,
        Alert.class,
        Message.class,
        Department.class,
        Continent.class,
        Institution.class,
        Region.class
    };

    for (Class<?> clazz : entityClasses) {
      hints.reflection().registerType(clazz, MemberCategory.values());
    }
  }

  private void registerPostgreSQLDriver(RuntimeHints hints, ClassLoader classLoader) {
    // Register PostgreSQL driver
    hints.reflection().registerTypeIfPresent(classLoader, "org.postgresql.Driver",
        hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));

    // Register PostgreSQL connection classes
    hints.reflection().registerTypeIfPresent(classLoader, "org.postgresql.core.ConnectionFactory",
        hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
  }

  private void registerResourcePatterns(RuntimeHints hints) {
    // MyBatis mapper XML files
    hints.resources().registerPattern("mapper/**/*.xml");
    hints.resources().registerPattern("**/*Mapper.xml");

    // Flyway migration scripts
    hints.resources().registerPattern("db/migration/*.sql");
    hints.resources().registerPattern("db/migration/**/*.sql");

    // Application configuration files
    hints.resources().registerPattern("application.yaml");
    hints.resources().registerPattern("application.yml");
    hints.resources().registerPattern("application.properties");

    // Swagger/OpenAPI resources
    hints.resources().registerPattern("META-INF/resources/**");
    hints.resources().registerPattern("static/**");
    hints.resources().registerPattern("public/**");
  }

  private void registerSerializationTypes(RuntimeHints hints) {
    // Java time types
    hints.serialization().registerType(java.time.LocalDate.class);
    hints.serialization().registerType(java.time.LocalDateTime.class);
    hints.serialization().registerType(java.time.LocalTime.class);
    hints.serialization().registerType(java.time.Instant.class);

    // Number types
    hints.serialization().registerType(java.math.BigDecimal.class);
    hints.serialization().registerType(java.math.BigInteger.class);
  }

  private void registerJWTClasses(RuntimeHints hints, ClassLoader classLoader) {
    // Register JWT classes for reflection
    hints.reflection().registerTypeIfPresent(classLoader, "io.jsonwebtoken.Jwts",
        hint -> hint.withMembers(MemberCategory.INVOKE_DECLARED_METHODS));
    hints.reflection().registerTypeIfPresent(classLoader, "io.jsonwebtoken.Claims",
        hint -> hint.withMembers(MemberCategory.DECLARED_FIELDS, MemberCategory.INVOKE_DECLARED_METHODS));
  }
}
