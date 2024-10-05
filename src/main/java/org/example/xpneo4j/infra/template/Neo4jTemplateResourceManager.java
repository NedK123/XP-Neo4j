package org.example.xpneo4j.infra.template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.ResourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "myapp.persistence.persistence.strategy", havingValue = "template")
public class Neo4jTemplateResourceManager implements ResourceCreator {
  @Autowired private Neo4jClient neo4jClient;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    String query =
        fetchQuery("registerDetachedResource.cypher")
            .replace(":CustomLabel", constructLabels(request));
    System.out.println(query);
    neo4jClient
        .query(query)
        .bind(request.getId())
        .to("id")
        .bind(request.getName())
        .to("name")
        .bind(request.getProjectId())
        .to("projectId")
        .run();
  }

  private static String constructLabels(RegisterDetachedResourceRequest request) {
    StringBuilder labelBuilder =
        new StringBuilder(":Resource:Project_%s".formatted(request.getProjectId()));
    for (String label : request.getAdditionalLabels()) {
      labelBuilder.append(":").append(label);
    }
    return labelBuilder.toString();
  }

  private static String fetchQuery(String queryFileName) {
    try {
      return new String(
          Files.readAllBytes(Paths.get("src/main/resources/persistence/" + queryFileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    System.out.println(neo4jClient);
  }
}
