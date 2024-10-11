package org.example.xpneo4j.infra.neo4jclient;

import static java.util.stream.StreamSupport.*;
import static org.example.xpneo4j.infra.shared.QueryUtilities.*;

import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.example.xpneo4j.core.*;
import org.example.xpneo4j.infra.neo4jrepo.ResourceFamily;
import org.neo4j.driver.types.Entity;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
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
  private static final String TARGET_RESOURCE_CUSTOM_LABELS = ":TargetCustomLabels";
  private static final String NEIGHBOR_CUSTOM_LABELS = ":NeighborCustomLabels";
  private static final String CUSTOM_LABELS = ":CustomLabels";
  private static final String RELATION_CUSTOM_LABEL = ":RelationCustomLabel";
  public static final String NEIGHBOR_ID_FIELD = "neighborId";
  public static final String NEIGHBOR_NAME_FIELD = "neighborName";
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
        .to(NEIGHBOR_ID_FIELD)
        .bind(request.getNeighbor().getName())
        .to(NEIGHBOR_NAME_FIELD)
        .bind(request.getProjectId())
        .to(PROJECT_ID_FIELD)
        .bind(request.getNeighbor().getRelationshipContext())
        .to(RELATIONSHIP_CONTEXT_FIELD)
        .run();

    if (request.getNeighbor().isInheritCreationConnectionFromTarget()) {
      neo4jClient
          .query(generateInheritTargetCreationConnectionForNeighbor())
          .bind(request.getTargetResourceId())
          .to(TARGET_RESOURCE_ID_FIELD)
          .bind(request.getNeighbor().getId())
          .to(NEIGHBOR_ID_FIELD)
          .run();
    }
  }

  @Override
  public LineageResponse fetchLineage(FetchLineageRequest request) {
    log.info("Fetching lineage for request={}", request);
    String query = generateFetchLineageQuery(request);
    return neo4jClient
        .query(query)
        .bind(request.getTargetResourceId())
        .to(TARGET_RESOURCE_ID_FIELD)
        .fetchAs(Path.class)
        .mappedBy((typeSystem, record) -> record.get("path").asPath())
        .one()
        .map(
            path ->
                buildFullLineage(
                    fetchNodeElementId(path, request.getTargetResourceId()),
                    toNodesMap(path),
                    toRelationshipsMap(path)))
        .orElse(LineageResponse.builder().build());
  }

  @Override
  public RelativesResponse fetchRelatives(FetchRelativesRequest request) {
//    Optional<RelativesResponse> family =
//        neo4jClient
//            .query(generateFetchRelativesQuery(request))
//            //            .bind("targetResourceId").to(request.getTargetResourceId())
//            //            .bind("relativeResourcesTypeFilter").to("['E']")
//            .fetchAs(ResourceFamily.class)
//            .one()
//            .map(
//                result -> {
//                  return RelativesResponse.builder().build();
//                });
    return RelativesResponse.builder().build();
  }

  private String generateFetchRelativesQuery(FetchRelativesRequest request) {
    return fetchQuery("fetchResourceRelatives.cypher")
        .replace("$ancestorsGenerationLimit", String.valueOf(request.getAncestorsGenerationLimit()))
        .replace("$relativesGenerationLimit", String.valueOf(request.getRelativesGenerationLimit()))
        .replace("$targetResourceId", "'" + request.getTargetResourceId() + "'")
        .replace("$relativeResourcesTypeFilter", "['E']");
  }

  private static String fetchNodeElementId(Path path, String resourceId) {
    return stream(path.nodes().spliterator(), false)
        .filter(node -> resourceId.equals(node.get("id").asString()))
        .findFirst()
        .map(Entity::elementId)
        .orElseThrow(RuntimeException::new);
  }

  private static Map<String, Relationship> toRelationshipsMap(Path path) {
    return stream(path.relationships().spliterator(), false)
        .collect(Collectors.toMap(Entity::elementId, relationship -> relationship));
  }

  private static Map<String, Node> toNodesMap(Path path) {
    return stream(path.nodes().spliterator(), false)
        .collect(Collectors.toMap(Entity::elementId, node -> node));
  }

  private static LineageResponse buildFullLineage(
      String targetNodeId, Map<String, Node> nodes, Map<String, Relationship> relationships) {
    var lineageMap = new HashMap<String, LineageResource>();
    relationships.forEach(
        (id, relationship) -> {
          var lineageResource =
              provisionLineageResource(nodes, lineageMap, relationship.startNodeElementId());
          lineageMap.putIfAbsent(lineageResource.getKey(), lineageResource.getValue());
          if (relationship.hasType(RelationshipType.CREATED_UNDER.name())) {
            var parent =
                provisionLineageResource(nodes, lineageMap, relationship.endNodeElementId());
            lineageMap.putIfAbsent(parent.getKey(), parent.getValue());
            lineageResource.getValue().setParent(parent.getValue());
          }
        });
    return LineageResponse.builder().targetResource(lineageMap.get(targetNodeId)).build();
  }

  private static Pair<String, LineageResource> provisionLineageResource(
      Map<String, Node> nodes, Map<String, LineageResource> map, String nodeId) {
    if (map.containsKey(nodeId)) {
      return Pair.of(nodeId, map.get(nodeId));
    } else {
      return buildLineageResource(nodes, nodeId);
    }
  }

  private static Pair<String, LineageResource> buildLineageResource(
      Map<String, Node> nodes, String nodeId) {
    Node node = nodes.get(nodeId);
    return Pair.of(node.elementId(), buildLineageResource(node));
  }

  private static LineageResource buildLineageResource(Node node) {
    return LineageResource.builder()
        .id(node.get("id").asString())
        .name(node.get("name").asString())
        .types(stream(node.labels().spliterator(), false).collect(Collectors.toSet()))
        .build();
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

  private static String generateInheritTargetCreationConnectionForNeighbor() {
    return fetchQuery("inheritTargetCreationConnectionForNeighbor.cypher");
  }

  private static String constructrelationshipType(RelationshipType type) {
    return ":%s".formatted(type.name());
  }
}
