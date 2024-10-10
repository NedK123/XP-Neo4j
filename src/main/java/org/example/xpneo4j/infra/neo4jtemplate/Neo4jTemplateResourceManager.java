package org.example.xpneo4j.infra.neo4jtemplate;

import static org.example.xpneo4j.infra.shared.QueryUtilities.*;
import static org.example.xpneo4j.infra.shared.ResourceFactory.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.neo4j.core.Neo4jTemplate;
import org.springframework.data.neo4j.repository.query.QueryFragmentsAndParameters;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jTemplate")
public class Neo4jTemplateResourceManager implements ResourceCreator, ResourceFetcher {
  private static final String TARGET_RESOURCE_ID_FIELD = "targetId";

  @Autowired private Neo4jTemplate template;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
    template.save(generateResource(request));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    template
        .findById(request.getTargetResourceId(), ResourceNode.class)
        .ifPresent(targetResource -> template.save(generateNeighbor(targetResource, request)));
  }

  @Override
  public LineageResponse fetchLineage(FetchLineageRequest request) {
    log.info("Fetching lineage for request={}", request);
    QueryFragmentsAndParameters query = generateLineageQuery(request);
    var resources =
        template.toExecutableQuery(ResourceNode.class, query).getResults().stream()
            .collect(Collectors.toMap(ResourceNode::getId, resourceNode -> resourceNode));
    return buildLineage(request.getTargetResourceId(), resources);
  }

  private QueryFragmentsAndParameters generateLineageQuery(FetchLineageRequest request) {
    return new QueryFragmentsAndParameters(
        generateFetchLineageQuery(request),
        Map.of(TARGET_RESOURCE_ID_FIELD, request.getTargetResourceId()));
  }

  private LineageResponse buildLineage(
      String targetResourceId, Map<String, ResourceNode> resources) {
    Map<String, LineageResource> map = new HashMap<>();
    resources.forEach(
        (id, resource) -> {
          map.putIfAbsent(id, buildLineageResource(resource));
          if (resource.getCreatedUnderRelationship() != null) {
            ResourceNode parent = resource.getCreatedUnderRelationship().getNeighbor();
            map.putIfAbsent(parent.getId(), buildLineageResource(parent));
            map.get(resource.getId()).setParent(map.get(parent.getId()));
          }
        });
    return LineageResponse.builder().targetResource(map.get(targetResourceId)).build();
  }

  private static LineageResource buildLineageResource(ResourceNode target) {
    return LineageResource.builder()
        .id(target.getId())
        .name(target.getName())
        .types(target.getLabels())
        .build();
  }

  private String generateFetchLineageQuery(FetchLineageRequest request) {
    return fetchQuery("fetchResourceLineage.cypher");
  }
}
