package org.example.xpneo4j.infra.neo4jrepo;

import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepo extends Neo4jRepository<ResourceNode, String> {}
