package com.isep.ffa.util;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Collections;
import java.util.Scanner;

/**
 * MyBatis-Plus Code Generator
 * Used to automatically generate Entity, Mapper, Service, Controller and other
 * code
 */
public class CodeGenerator {

  public static void main(String[] args) {
    // Database connection configuration
    String url = "jdbc:postgresql://localhost:5432/testdb";
    String username = "admin";
    String password = "123456";

    // Project configuration
    String projectPath = System.getProperty("user.dir");
    String parentPackage = "com.isep.ffa";

    // Interactive table name input
    Scanner scanner = new Scanner(System.in);
    System.out.println(
        "Please enter the table names to generate (separate multiple tables with commas, enter 'all' to generate all tables):");
    String tableNames = scanner.nextLine();

    FastAutoGenerator.create(url, username, password)
        // Global configuration
        .globalConfig(builder -> {
          builder.author("FFA Development Team") // Set author
              .enableSwagger() // Enable swagger mode
              // .fileOverride(true) // Overwrite existing files - temporarily commented out
              .outputDir(projectPath + "/src/main/java") // Specify output directory
              .dateType(DateType.TIME_PACK) // Time strategy
              .commentDate("yyyy-MM-dd HH:mm:ss"); // Comment date
        })
        // Data source configuration - temporarily commented out, needs adjustment based
        // on MyBatis-Plus version
        // .dataSourceConfig(builder -> {
        // builder.setUrl(url)
        // .setUsername(username)
        // .setPassword(password)
        // .setDriverName("org.postgresql.Driver");
        // })
        // Package configuration
        .packageConfig(builder -> {
          builder.parent(parentPackage) // Set parent package name
              .moduleName("") // Set parent package module name
              .entity("entity") // Entity class package name
              .mapper("mapper") // Mapper package name
              .service("service") // Service package name
              .serviceImpl("service.impl") // ServiceImpl package name
              .controller("controller") // Controller package name
              .xml("mapper") // Mapper XML package name
              .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // Set
                                                                                                               // mapperXml
                                                                                                               // generation
                                                                                                               // path
        })
        // Strategy configuration
        .strategyConfig(builder -> {
          // Set table names to generate
          if ("all".equalsIgnoreCase(tableNames)) {
            builder.addInclude(".*"); // Generate all tables
          } else {
            builder.addInclude(tableNames.split(",")); // Generate specified tables
          }

          // Entity strategy configuration
          builder.entityBuilder()
              .enableLombok() // Enable lombok model
              .enableChainModel() // Enable chain model
              .enableRemoveIsPrefix() // Enable Boolean type field to remove is prefix
              .enableTableFieldAnnotation() // Enable field annotation generation when generating entities
              .enableActiveRecord() // Enable ActiveRecord mode
              .versionColumnName("version") // Optimistic lock column name
              .versionPropertyName("version") // Optimistic lock property name
              .logicDeleteColumnName("is_deleted") // Logical delete column name
              .logicDeletePropertyName("isDeleted") // Logical delete property name
              .naming(NamingStrategy.underline_to_camel) // Database table to entity naming strategy
              .columnNaming(NamingStrategy.underline_to_camel) // Database table field to entity naming strategy
              .addSuperEntityColumns("id", "creation_date", "last_modification_date", "creator_user",
                  "last_modificator_user", "is_deleted") // Add parent class common fields
              .addTableFills(new Column("creation_date", FieldFill.INSERT)) // Add table field fill
              .addTableFills(new Column("last_modification_date", FieldFill.INSERT_UPDATE)) // Add table field fill
              .idType(IdType.AUTO); // Global primary key type

          // Mapper strategy configuration
          builder.mapperBuilder()
              .enableMapperAnnotation() // Enable @Mapper annotation
              .enableBaseResultMap() // Enable BaseResultMap generation
              .enableBaseColumnList(); // Enable BaseColumnList

          // Service strategy configuration
          builder.serviceBuilder()
              .formatServiceFileName("%sService") // Format service interface file name
              .formatServiceImplFileName("%sServiceImpl"); // Format service implementation class file name

          // Controller strategy configuration
          builder.controllerBuilder()
              .enableHyphenStyle() // Enable camel case to hyphen conversion
              .enableRestStyle(); // Enable @RestController controller generation
        })
        // Use Freemarker engine template, default is Velocity engine
        .templateEngine(new FreemarkerTemplateEngine())
        .execute();

    System.out.println("Code generation completed!");
    scanner.close();
  }
}
