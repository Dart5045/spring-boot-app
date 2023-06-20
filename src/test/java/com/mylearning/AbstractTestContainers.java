package com.mylearning;

import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

/*
 @SpringBootTest no usar esto porque carga application context
 y muchas otras cosas que van a hacer el test lento
*/
@Testcontainers
public abstract class AbstractTestContainers {

     @BeforeAll
     static void beforeAll(){
         Flyway flyway = Flyway
                 .configure()
                 .dataSource(
                 postgreSQLContainer.getJdbcUrl(),
                 postgreSQLContainer.getUsername(),
                 postgreSQLContainer.getPassword()
         ).load();
         flyway.migrate();
     }

    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer=
            new PostgreSQLContainer<>("postgres:latest").
                    withDatabaseName("mylearning-dao-unit-test").
                    withUsername("admin").
                    withPassword("password");

    @DynamicPropertySource
    public static void  registerDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasrouce.url",
                postgreSQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasrouce.username",
                postgreSQLContainer::getUsername
        );
        registry.add(
                "spring.datasrouce.password",
                postgreSQLContainer::getPassword
        );
    }

    private static DataSource getDataSource(){
        DataSourceBuilder builder = DataSourceBuilder
                .create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword());
        return builder.build();

    }
    protected static JdbcTemplate getJdbcTemplate(){
        return new JdbcTemplate(getDataSource());
    }

    protected  static final Faker FAKER = new Faker();
}
