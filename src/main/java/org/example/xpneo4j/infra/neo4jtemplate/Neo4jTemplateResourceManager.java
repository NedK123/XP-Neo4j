package org.example.xpneo4j.infra.neo4jtemplate;

import static org.example.xpneo4j.infra.shared.QueryUtilities.*;
import static org.example.xpneo4j.infra.shared.ResourceFactory.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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

  @Override
  public RelativesResponse fetchRelatives(FetchRelativesRequest request) {
    QueryFragmentsAndParameters query = generateRelativesQuery(request);
    Set<ResourceNode> relatives =
        new HashSet<>(template.toExecutableQuery(ResourceNode.class, query).getResults());
    return buildResponse(relatives, request.getRelativeResourcesTypeFilter());
  }

  private static RelativesResponse buildResponse(
      Set<ResourceNode> relatives, Set<String> relativeResourcesTypeFilter) {
    Map<String, Set<RelativeResource>> map =
        relativeResourcesTypeFilter.stream()
            .collect(Collectors.toMap(s -> s, s -> new HashSet<>()));
    relatives.forEach(
        relative ->
            relative
                .getLabels()
                .forEach(
                    label -> {
                      if (map.containsKey(label)) {
                        map.get(label).add(buildRelativeResource(relative));
                      }
                    }));
    return RelativesResponse.builder().relatives(map).build();
  }

  private static RelativeResource buildRelativeResource(ResourceNode relative) {
    return RelativeResource.builder()
        .id(relative.getId())
        .name(relative.getName())
        .relationshipWithTargetResource(RelativeRelationship.builder().build())
        .build();
  }

  private QueryFragmentsAndParameters generateRelativesQuery(FetchRelativesRequest request) {
    return new QueryFragmentsAndParameters(
        generateFetchRelativesQuery(request),
        Map.of(
            "targetResourceId",
            request.getTargetResourceId(),
            "relativeResourcesTypeFilter",
            request.getRelativeResourcesTypeFilter()));
  }

  private String generateFetchRelativesQuery(FetchRelativesRequest request) {
    return fetchQuery("fetchResourceRelatives.cypher")
        .replace("$ancestorsGenerationLimit", String.valueOf(request.getAncestorsGenerationLimit()))
        .replace(
            "$relativesGenerationLimit", String.valueOf(request.getRelativesGenerationLimit()));
  }

  private QueryFragmentsAndParameters generateLineageQuery(FetchLineageRequest request) {
    return new QueryFragmentsAndParameters(
        generateFetchLineageQuery(),
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
        .types(
            target.getLabels().stream()
                .filter(Neo4jTemplateResourceManager::filterOutInternalLabels)
                .collect(Collectors.toSet()))
        .build();
  }

  private static boolean filterOutInternalLabels(String s) {
    return !s.equals("Resource") && !s.contains("Project") && !s.equals("BaseResource");
  }

  private String generateFetchLineageQuery() {
    return fetchQuery("fetchResourceLineage.cypher");
  }
}
