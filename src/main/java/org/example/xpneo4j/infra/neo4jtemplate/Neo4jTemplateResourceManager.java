package org.example.xpneo4j.infra.neo4jtemplate;

import static org.example.xpneo4j.infra.shared.QueryUtilities.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.example.xpneo4j.infra.shared.ResourceFactory;
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
    template.save(ResourceFactory.generateResource(request));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    template
        .findById(request.getTargetResourceId(), ResourceNode.class)
        .ifPresent(
            targetResource -> {
              ResourceRelationship relationship =
                  ResourceFactory.generateRelationshipWithNeighbor(request);
              targetResource.addRelationshipWithNeighbor(
                  relationship, request.getNeighbor().getRelationshipLabel());
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
            .map(Neo4jTemplateResourceManager::buildResource)
            .collect(Collectors.toSet());

    return ResourceLineage.builder().resources(resources).build();
  }

  private static Resource buildResource(ResourceNode node) {
    return Resource.builder()
        .id(node.getId())
        .name(node.getName())
        .labels(node.getLabels())
        .build();
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
}
