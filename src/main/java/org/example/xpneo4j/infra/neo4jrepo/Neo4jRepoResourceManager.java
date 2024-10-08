package org.example.xpneo4j.infra.neo4jrepo;

import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceRelationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jrepo")
public class Neo4jRepoResourceManager implements ResourceCreator, ResourceFetcher {

  @Autowired private ResourceRepo repo;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
    repo.save(
        generateResource(
            request.getId(),
            request.getName(),
            request.getProjectId(),
            request.getAdditionalLabels()));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    repo.findById(request.getTargetResourceId())
        .ifPresent(
            targetResource -> {
              ResourceRelationship relationship = generateRelationship(request);
              switch (request.getNeighbor().getRelationshipLabel()) {
                case CREATED_UNDER ->
                    targetResource.getCreatedUnderRelationships().add(relationship);
                case REPUSH_OF -> targetResource.getRepushOfRelationships().add(relationship);
              }
              repo.save(targetResource);
            });
  }

  @Override
  public ResourceLineage fetchLineage(FetchLineageRequest request) {
    return null;
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

  private static ResourceNode generateResource(
      String id, String name, String projectId, Set<String> additionalLabels) {
    Set<String> labels = new HashSet<>();
    labels.add("Project_%s".formatted(projectId.replaceAll("-", "_")));
    labels.addAll(additionalLabels);
    return ResourceNode.builder().id(id).name(name).projectId(projectId).labels(labels).build();
  }
}
