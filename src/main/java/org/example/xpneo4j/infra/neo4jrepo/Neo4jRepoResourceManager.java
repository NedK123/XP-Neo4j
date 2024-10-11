package org.example.xpneo4j.infra.neo4jrepo;

import static org.example.xpneo4j.infra.shared.ResourceFactory.*;

import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jRepo")
public class Neo4jRepoResourceManager implements ResourceCreator, ResourceFetcher {

  @Autowired private ResourceRepo repo;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
    repo.save(generateResource(request));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    repo.findById(request.getTargetResourceId())
        .ifPresent(targetResource -> repo.save(generateNeighbor(targetResource, request)));
  }

  @Override
  public LineageResponse fetchLineage(FetchLineageRequest request) {
    ResourceNode targetResourceNode =
        repo.findById(request.getTargetResourceId()).orElseThrow(RuntimeException::new);
    LineageResource targetLineageResource = buildLineageResource(targetResourceNode);
    buildLineage(targetLineageResource, targetResourceNode);
    return LineageResponse.builder().targetResource(targetLineageResource).build();
  }

  @Override
  public RelativesResponse fetchRelatives(FetchRelativesRequest request) {
//    ResourceFamily resourceFamily =
//        repo.fetchRelatives(
//            request.getTargetResourceId(),
//            request.getAncestorsGenerationLimit(),
//            request.getRelativesGenerationLimit(),
//            request.getRelativeResourcesTypeFilter());
    return null;
  }

  private static void buildLineage(
      LineageResource targetLineageResource, ResourceNode resourceNode) {
    LineageResource currentLineageResource = targetLineageResource;
    ResourceNode currentResource = resourceNode.getCreationNeighbor().orElse(null);
    while (currentResource != null) {
      currentLineageResource.setParent(buildLineageResource(currentResource));
      currentResource = currentResource.getCreationNeighbor().orElse(null);
      currentLineageResource = currentLineageResource.getParent();
    }
  }

  private static LineageResource buildLineageResource(ResourceNode resourceNode) {
    return LineageResource.builder()
        .id(resourceNode.getId())
        .name(resourceNode.getName())
        .types(resourceNode.getLabels())
        .build();
  }
}
