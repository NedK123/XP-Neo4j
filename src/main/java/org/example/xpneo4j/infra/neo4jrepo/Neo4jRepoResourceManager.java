package org.example.xpneo4j.infra.neo4jrepo;

import static org.example.xpneo4j.infra.shared.ResourceFactory.*;

import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
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
    return null;
  }
}
