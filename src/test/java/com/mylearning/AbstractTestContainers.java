package com.mylearning;

import org.flywaydb.core.Flyway;
import org.junit.BeforeClass;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/*
 @SpringBootTest no usar esto porque carga application context
 y muchas otras cosas que van a hacer el test lento
*/
@Testcontainers
public abstract class AbstractTestContainers {

     @BeforeClass
     static void beforeAll(){
         Flyway flyway = Flyway.configure().dataSource(
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
}
