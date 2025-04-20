package org.example.xpneo4j.infra.neo4jdriver;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Neo4jDriverConfig {

  @Bean
  public Driver getDriver(
      @Value("${spring.data.neo4j.uri}") String uri,
      @Value("${spring.data.neo4j.authentication.username}") String username,
      @Value("${spring.data.neo4j.authentication.password}") String password) {
    return GraphDatabase.driver(uri, AuthTokens.basic(username, password));
  }
}
