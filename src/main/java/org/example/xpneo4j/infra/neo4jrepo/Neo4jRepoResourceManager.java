package org.example.xpneo4j.infra.neo4jrepo;

import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.ResourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "myapp.persistence.strategy", havingValue = "sdn")
public class Neo4jRepoResourceManager implements ResourceCreator {

  @Autowired private ResourceRepo repo;

  @Override
  public void register(RegisterDetachedResourceRequest request) {
    System.out.println(repo);
  }

  @Override
  public void register(RegisterNeighborRequest request) {
    System.out.println(repo);
  }
}
