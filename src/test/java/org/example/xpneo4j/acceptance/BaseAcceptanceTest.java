package org.example.xpneo4j.acceptance;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@CucumberContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class BaseAcceptanceTest {
  private static final String TEST_DB_NAME = "test-resources-database";
  private static final String TEST_DB_USERNAME = "neo4j";
  private static final String TEST_DB_PASSWORD = "notverysecret";

  @Container
  public static Neo4jContainer<?> neo4jContainer =
      new Neo4jContainer<>("neo4j:5.24.1-community-bullseye")
          .withAdminPassword(TEST_DB_PASSWORD)
          .withEnv("NEO4J_dbms_default__database", TEST_DB_NAME)
          .withReuse(true);

  static {
    neo4jContainer.start();
  }

  @Autowired protected MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @DynamicPropertySource
  static void configureTestProperties(DynamicPropertyRegistry registry) {
    registry.add("myapp.persistence.strategy", () -> "neo4jTemplate");
    registry.add("spring.neo4j.uri", () -> neo4jContainer.getBoltUrl());
    registry.add("spring.data.neo4j.database", () -> TEST_DB_NAME);
    registry.add("spring.neo4j.authentication.username", () -> TEST_DB_USERNAME);
    registry.add("spring.neo4j.authentication.password", () -> neo4jContainer.getAdminPassword());
  }
}
