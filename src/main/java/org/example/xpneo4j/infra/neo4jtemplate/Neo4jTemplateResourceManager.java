package org.example.xpneo4j.infra.neo4jtemplate;

import static org.example.xpneo4j.infra.QueryUtilities.*;

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
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jtemplate")
public class Neo4jTemplateResourceManager implements ResourceCreator, ResourceFetcher {
  private static final String TARGET_RESOURCE_CUSTOM_LABELS = ":TargetCustomLabels";
  private static final String NEIGHBOR_CUSTOM_LABELS = ":NeighborCustomLabels";
  private static final String RELATION_CUSTOM_LABELS = ":RelationCustomLabels";
  private static final String TARGET_RESOURCE_ID_FIELD = "targetId";
  private static final String FILTER_CONTEXTS_FIELD = "filterContexts";

  @Autowired private Neo4jTemplate template;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
    template.save(
        generateResource(
            request.getId(),
            request.getName(),
            request.getProjectId(),
            request.getAdditionalLabels()));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    template
        .findById(request.getTargetResourceId(), ResourceNode.class)
        .ifPresent(
            targetResource -> {
              ResourceRelationship relationship = generateRelationship(request);
              switch (request.getNeighbor().getRelationshipLabel()) {
                case CREATED_UNDER ->
                    targetResource.getCreatedUnderRelationships().add(relationship);
                case REPUSH_OF -> targetResource.getRepushOfRelationships().add(relationship);
              }
              log.info("Hello {}", targetResource);
              template.save(targetResource);
            });
  }

  @Override
  public ResourceLineage fetchLineage(FetchLineageRequest request) {
    log.info("Fetching lineage for request={}", request);
    QueryFragmentsAndParameters queryFragmentsAndParameters =
        new QueryFragmentsAndParameters(
            generateFetchLineageQuery(request),
            Map.of(
                TARGET_RESOURCE_ID_FIELD,
                request.getTargetResourceId(),
                FILTER_CONTEXTS_FIELD,
                request.getFilterContexts()));

    Set<Resource> resources =
        template
            .toExecutableQuery(ResourceNode.class, queryFragmentsAndParameters)
            .getResults()
            .stream()
            .map(
                r ->
                    Resource.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .labels(r.getLabels())
                        .build())
            .collect(Collectors.toSet());

    return ResourceLineage.builder().resources(resources).build();
  }

  private String generateFetchLineageQuery(FetchLineageRequest request) {
    return fetchQuery("fetchResourceLineage.cypher")
        .replace(
            TARGET_RESOURCE_CUSTOM_LABELS,
            constructResourceLabels(request.getProjectId(), Set.of()))
        .replace(
            RELATION_CUSTOM_LABELS,
            constructDisjunctionLabels(
                request.getFilterRelationshipTypes().stream()
                    .map(RelationshipType::name)
                    .collect(Collectors.toSet())))
        .replace(
            NEIGHBOR_CUSTOM_LABELS, constructDisjunctionLabels(request.getFilterResourceTypes()));
  }

  private static ResourceNode generateResource(
      String id, String name, String projectId, Set<String> additionalLabels) {
    Set<String> labels = new HashSet<>();
    labels.add("Resource");
    labels.add("Project_%s".formatted(projectId.replaceAll("-", "_")));
    labels.addAll(additionalLabels);
    return ResourceNode.builder().id(id).name(name).projectId(projectId).labels(labels).build();
  }

  private ResourceRelationship generateRelationship(RegisterNeighborRequest request) {
    return ResourceRelationship.builder()
        .context(request.getNeighbor().getRelationshipContext())
        .neighbor(
            generateResource(
                request.getNeighbor().getId(),
                request.getNeighbor().getName(),
                request.getProjectId(),
                request.getNeighbor().getLabels()))
        .build();
  }
}
