package ru.hse.pipo;

import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class CommonTestConfiguration {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("wms_test")
        .withUsername("test")
        .withPassword("test");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @LocalServerPort
    Integer port;

    RestClient restClient;

    static Random random = new Random();

    @BeforeEach
    void setup() {
        System.out.println("Server is running on port: " + port);
        restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port + "/api/v1")
            .build();
    }

    public static String generateString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static Long generateLong() {
        return Math.abs(random.nextLong());
    }
}
