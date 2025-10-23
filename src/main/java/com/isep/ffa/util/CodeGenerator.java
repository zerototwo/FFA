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
 * MyBatis-Plus代码生成器
 * 用于自动生成Entity、Mapper、Service、Controller等代码
 */
public class CodeGenerator {

  public static void main(String[] args) {
    // 数据库连接配置
    String url = "jdbc:postgresql://localhost:5432/testdb";
    String username = "admin";
    String password = "123456";

    // 项目配置
    String projectPath = System.getProperty("user.dir");
    String parentPackage = "com.isep.ffa";

    // 交互式输入表名
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入要生成的表名（多个表用逗号分隔，输入all生成所有表）：");
    String tableNames = scanner.nextLine();

    FastAutoGenerator.create(url, username, password)
        // 全局配置
        .globalConfig(builder -> {
          builder.author("FFA Development Team") // 设置作者
              .enableSwagger() // 开启swagger模式
              // .fileOverride(true) // 覆盖已生成文件 - 暂时注释掉
              .outputDir(projectPath + "/src/main/java") // 指定输出目录
              .dateType(DateType.TIME_PACK) // 时间策略
              .commentDate("yyyy-MM-dd HH:mm:ss"); // 注释日期
        })
        // 数据源配置 - 暂时注释掉，需要根据MyBatis-Plus版本调整
        // .dataSourceConfig(builder -> {
        // builder.setUrl(url)
        // .setUsername(username)
        // .setPassword(password)
        // .setDriverName("org.postgresql.Driver");
        // })
        // 包配置
        .packageConfig(builder -> {
          builder.parent(parentPackage) // 设置父包名
              .moduleName("") // 设置父包模块名
              .entity("entity") // 实体类包名
              .mapper("mapper") // Mapper包名
              .service("service") // Service包名
              .serviceImpl("service.impl") // ServiceImpl包名
              .controller("controller") // Controller包名
              .xml("mapper") // Mapper XML包名
              .pathInfo(Collections.singletonMap(OutputFile.xml, projectPath + "/src/main/resources/mapper")); // 设置mapperXml生成路径
        })
        // 策略配置
        .strategyConfig(builder -> {
          // 设置需要生成的表名
          if ("all".equalsIgnoreCase(tableNames)) {
            builder.addInclude(".*"); // 生成所有表
          } else {
            builder.addInclude(tableNames.split(",")); // 生成指定表
          }

          // Entity策略配置
          builder.entityBuilder()
              .enableLombok() // 开启lombok模型
              .enableChainModel() // 开启链式模型
              .enableRemoveIsPrefix() // 开启Boolean类型字段移除is前缀
              .enableTableFieldAnnotation() // 开启生成实体时生成字段注解
              .enableActiveRecord() // 开启ActiveRecord模式
              .versionColumnName("version") // 乐观锁字段名
              .versionPropertyName("version") // 乐观锁属性名
              .logicDeleteColumnName("is_deleted") // 逻辑删除字段名
              .logicDeletePropertyName("isDeleted") // 逻辑删除属性名
              .naming(NamingStrategy.underline_to_camel) // 数据库表映射到实体的命名策略
              .columnNaming(NamingStrategy.underline_to_camel) // 数据库表字段映射到实体的命名策略
              .addSuperEntityColumns("id", "creation_date", "last_modification_date", "creator_user",
                  "last_modificator_user", "is_deleted") // 添加父类公共字段
              .addTableFills(new Column("creation_date", FieldFill.INSERT)) // 添加表字段填充
              .addTableFills(new Column("last_modification_date", FieldFill.INSERT_UPDATE)) // 添加表字段填充
              .idType(IdType.AUTO); // 全局主键类型

          // Mapper策略配置
          builder.mapperBuilder()
              .enableMapperAnnotation() // 开启@Mapper注解
              .enableBaseResultMap() // 启用BaseResultMap生成
              .enableBaseColumnList(); // 启用BaseColumnList

          // Service策略配置
          builder.serviceBuilder()
              .formatServiceFileName("%sService") // 格式化service接口文件名称
              .formatServiceImplFileName("%sServiceImpl"); // 格式化service实现类文件名称

          // Controller策略配置
          builder.controllerBuilder()
              .enableHyphenStyle() // 开启驼峰转连字符
              .enableRestStyle(); // 开启生成@RestController控制器
        })
        // 使用Freemarker引擎模板，默认的是Velocity引擎
        .templateEngine(new FreemarkerTemplateEngine())
        .execute();

    System.out.println("代码生成完成！");
    scanner.close();
  }
}
