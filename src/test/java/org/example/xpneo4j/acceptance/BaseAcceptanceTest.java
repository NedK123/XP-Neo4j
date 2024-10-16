package org.example.xpneo4j.acceptance;

import io.cucumber.spring.CucumberContextConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.example.xpneo4j.acceptance.data.creation.FirstUseCase;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
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
  protected ObjectMapper testContainersObjectMapper = new ObjectMapper();

  @DynamicPropertySource
  static void configureTestProperties(DynamicPropertyRegistry registry) {
    registry.add("myapp.persistence.strategy", () -> "neo4jTemplate");
    registry.add("spring.neo4j.uri", () -> neo4jContainer.getBoltUrl());
    registry.add("spring.data.neo4j.database", () -> TEST_DB_NAME);
    registry.add("spring.neo4j.authentication.username", () -> TEST_DB_USERNAME);
    registry.add("spring.neo4j.authentication.password", () -> neo4jContainer.getAdminPassword());
    registry.add("logging.level.org.springframework.data.neo4j.cypher=", () -> "ERROR");
  }

  protected void populateDatabase() {
    getDriver()
        .session()
        .writeTransaction(
            tx -> {
              tx.run("MATCH (n:Resource) DETACH DELETE n");
              return null;
            });
    importUseCase(1, 100);
    importUseCase(2, 100);
    importUseCase(3, 100);
  }

  private void importUseCase(int usecaseNumber, int importTimes) {
    if (usecaseNumber == 1) {
      for (int i = 0; i < importTimes; i++) {
        new FirstUseCase(mockMvc).importData();
      }
    }
  }

  private Driver getDriver() {
    return GraphDatabase.driver(
        neo4jContainer.getBoltUrl(), AuthTokens.basic(TEST_DB_USERNAME, TEST_DB_PASSWORD));
  }

  private static String fetchQuery(String queryFileName) {
    try {
      return new String(Files.readAllBytes(Paths.get("src/test/resources/data/" + queryFileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
