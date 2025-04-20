package org.example.xpneo4j.infra.neo4jdriver;

import static org.example.xpneo4j.infra.shared.QueryUtilities.*;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.example.xpneo4j.core.*;
import org.example.xpneo4j.infra.shared.QueryUtilities.RegisterDetachedResourceQuery;
import org.example.xpneo4j.infra.shared.QueryUtilities.RegisterNeighborResourceQuery;
import org.neo4j.driver.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "neo4jDriver")
public class Neo4jDriverResourceManager implements ResourceCreator, ResourceFetcher {
  @Autowired private Driver driver;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    log.info("Registering detached node for request={}", request);
    String query = RegisterDetachedResourceQuery.generate(request);
    driver
        .session()
        .executeWriteWithoutResult(
            tx ->
                tx.run(
                    query,
                    Map.of(
                        RegisterDetachedResourceQuery.RESOURCE_ID_FIELD,
                        request.getId(),
                        RegisterDetachedResourceQuery.RESOURCE_NAME_FIELD,
                        request.getName(),
                        RegisterDetachedResourceQuery.PROJECT_ID_FIELD,
                        request.getProjectId())));
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    log.info("Registering neighbor node for request={}", request);
    String query = RegisterNeighborResourceQuery.generate(request);
    driver.session().executeWriteWithoutResult(tx -> tx.run(query));
  }

  @Override
  public LineageResponse fetchLineage(FetchLineageRequest request) {
    return null;
  }

  @Override
  public RelativesResponse fetchRelatives(FetchRelativesRequest request) {
    return null;
  }
}
