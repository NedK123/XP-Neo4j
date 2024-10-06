package org.example.xpneo4j.infra.template;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "template")
public class Neo4jTemplateResourceManager implements ResourceCreator, ResourceFetcher {
  private static final String PROJECT_ID_FIELD = "projectId";
  private static final String RESOURCE_NAME_FIELD = "name";
  private static final String RESOURCE_ID_FIELD = "id";
  private static final String RELATIONSHIP_CONTEXT_FIELD = "context";
  private static final String TARGET_RESOURCE_ID_FIELD = "targetId";
  private static final String FILTER_CONTEXTS_FIELD = "filterContexts";
  @Autowired private Neo4jClient neo4jClient;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    String query = generateRegisterDetachedResourceQuery(request);
    neo4jClient
        .query(query)
        .bind(request.getId())
        .to(RESOURCE_ID_FIELD)
        .bind(request.getName())
        .to(RESOURCE_NAME_FIELD)
        .bind(request.getProjectId())
        .to(PROJECT_ID_FIELD)
        .run();
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    String query = generateRegisterNeighborQuery(request);
    neo4jClient
        .query(query)
        .bind(request.getTargetResourceId())
        .to(TARGET_RESOURCE_ID_FIELD)
        .bind(request.getNeighbor().getId())
        .to(RESOURCE_ID_FIELD)
        .bind(request.getNeighbor().getName())
        .to(RESOURCE_NAME_FIELD)
        .bind(request.getProjectId())
        .to(PROJECT_ID_FIELD)
        .bind(request.getNeighbor().getRelationshipContext())
        .to(RELATIONSHIP_CONTEXT_FIELD)
        .run();
  }

  @Override
  public ResourceLineage fetchLineage(FetchLineageRequest request) {
    String query = generateFetchLineageQuery(request);
    Set<Resource> resources =
        new HashSet<>(
            neo4jClient
                .query(query)
                .bind(request.getTargetResourceId())
                .to(TARGET_RESOURCE_ID_FIELD)
                .bind(request.getFilterContexts())
                .to(FILTER_CONTEXTS_FIELD)
                .fetchAs(Resource.class)
                .mappedBy(
                    (typeSystem, record) -> {
                      var node = record.get("n").asNode();
                      String id = node.get("id").asString();
                      String name = node.get("name").asString();
                      Set<String> labels = new HashSet<>();
                      node.labels().forEach(labels::add);
                      return Resource.builder().id(id).name(name).labels(labels).build();
                    })
                .all());
    return ResourceLineage.builder().resources(resources).build();
  }

  private String generateFetchLineageQuery(FetchLineageRequest request) {
    return fetchQuery("fetchResourceLineage.cypher")
        .replace(":TargetCustomLabels", constructResourceLabels(request.getProjectId(), Set.of()))
        .replace(
            ":NeighborCustomLabels",
            constructResourceDisjunctionLabels(request.getFilterResourceTypes()));
  }

  private static String generateRegisterDetachedResourceQuery(
      RegisterDetachedResourceRequest request) {
    return fetchQuery("registerDetachedResource.cypher")
        .replace(
            ":CustomLabels",
            constructResourceLabels(request.getProjectId(), request.getAdditionalLabels()));
  }

  private static String generateRegisterNeighborQuery(RegisterNeighborRequest request) {
    return fetchQuery("registerNeighborResource.cypher")
        .replace(":TargetCustomLabels", constructResourceLabels(request.getProjectId(), Set.of()))
        .replace(
            ":NeighborCustomLabels",
            constructResourceLabels(request.getProjectId(), request.getNeighbor().getLabels()))
        .replace(
            ":RelationCustomLabel",
            constructRelationshipLabel(request.getNeighbor().getRelationshipLabel()));
  }

  private static String constructResourceLabels(String projectId, Set<String> additionalLabels) {
    StringBuilder labelBuilder =
        new StringBuilder(":Resource:Project_%s".formatted(projectId.replaceAll("-", "_")));
    for (String label : additionalLabels) {
      labelBuilder.append(":").append(label);
    }
    return labelBuilder.toString();
  }

  private static String constructResourceDisjunctionLabels(Set<String> labels) {
    StringBuilder labelBuilder = new StringBuilder();
    labelBuilder.append(":");
    for (String label : labels) {
      labelBuilder.append(label);
      labelBuilder.append("|");
    }
    labelBuilder.deleteCharAt(labelBuilder.length() - 1);
    return labelBuilder.toString();
  }

  private static String constructRelationshipLabel(RelationshipType type) {
    return ":%s".formatted(type.name());
  }

  private static String fetchQuery(String queryFileName) {
    try {
      return new String(
          Files.readAllBytes(Paths.get("src/main/resources/persistence/" + queryFileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
