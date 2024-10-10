package org.example.xpneo4j.infra.neo4jclient;

import static org.example.xpneo4j.infra.shared.QueryUtilities.*;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jClient")
public class Neo4jClientResourceManager implements ResourceCreator, ResourceFetcher {
  private static final String PROJECT_ID_FIELD = "projectId";
  private static final String RESOURCE_NAME_FIELD = "name";
  private static final String RESOURCE_ID_FIELD = "id";
  private static final String RELATIONSHIP_CONTEXT_FIELD = "context";
  private static final String TARGET_RESOURCE_ID_FIELD = "targetId";
  private static final String FILTER_CONTEXTS_FIELD = "filterContexts";
  private static final String TARGET_RESOURCE_CUSTOM_LABELS = ":TargetCustomLabels";
  private static final String NEIGHBOR_CUSTOM_LABELS = ":NeighborCustomLabels";
  private static final String CUSTOM_LABELS = ":CustomLabels";
  private static final String RELATION_CUSTOM_LABELS = ":RelationCustomLabels";
  private static final String RELATION_CUSTOM_LABEL = ":RelationCustomLabel";
  @Autowired private Neo4jClient neo4jClient;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
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
    log.info("Registering neighbor node for request={}", request);
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
  public LineageResponse fetchLineage(FetchLineageRequest request) {
    log.info("Fetching lineage for request={}", request);
    String query = generateFetchLineageQuery(request);
    Path path;
//    Set<LineageResource> resources =
//        new HashSet<>(
//            neo4jClient
//                .query(query)
//                .bind(request.getTargetResourceId())
//                .to(TARGET_RESOURCE_ID_FIELD)
//                .fetchAs(LineageResource.class)
//                .mappedBy(
//                    (typeSystem, record) -> {
//                      path = record.get("path").asPath();
//                      for (Node node : path.nodes()) {
//                        String id = node.get("id").asString();
//                        String name = node.get("name").asString();
//                        Set<String> labels = new HashSet<>();
//                        node.labels().forEach(labels::add);
//                        return LineageResource.builder().id(id).name(name).types(labels).build();
//                      }
//                      var node = record.get("n").asNode();
//                      String id = node.get("id").asString();
//                      String name = node.get("name").asString();
//                      Set<String> labels = new HashSet<>();
//                      node.labels().forEach(labels::add);
//                      return Resource.builder().id(id).name(name).labels(labels).build();
//                    })
//                .all());
    return LineageResponse.builder().build();
  }

  private String generateFetchLineageQuery(FetchLineageRequest request) {
    return fetchQuery("fetchResourceLineage.cypher");
  }

  private static String generateRegisterDetachedResourceQuery(
      RegisterDetachedResourceRequest request) {
    return fetchQuery("registerDetachedResource.cypher")
        .replace(
            CUSTOM_LABELS,
            constructResourceLabels(request.getProjectId(), request.getAdditionalLabels()));
  }

  private static String generateRegisterNeighborQuery(RegisterNeighborRequest request) {
    return fetchQuery("registerNeighborResource.cypher")
        .replace(
            TARGET_RESOURCE_CUSTOM_LABELS,
            constructResourceLabels(request.getProjectId(), Set.of()))
        .replace(
            NEIGHBOR_CUSTOM_LABELS,
            constructResourceLabels(
                request.getProjectId(), request.getNeighbor().getAdditionalLabels()))
        .replace(
            RELATION_CUSTOM_LABEL,
            constructrelationshipType(request.getNeighbor().getRelationshipType()));
  }

  private static String constructrelationshipType(RelationshipType type) {
    return ":%s".formatted(type.name());
  }
}
