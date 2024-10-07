package org.example.xpneo4j.infra.neo4jrepo;

import java.util.Set;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepo extends Neo4jRepository<ResourceEntity, String> {

  @Query(
      """
                    MATCH (target:Resource {resourceId: $request.targetResourceId})
                    MERGE (neighbor:Resource)
//                    CALL apoc.create.addLabels(id(neighbor), `:#{allOf(#request.neighbor.labels)}`
)
                    """)
  void register(@Param("request") RegisterNeighborRequest request);

  @Query(
      """
                    MATCH (ancestor)-[r*]->(target)
                    WHERE target.resourceId = $id
                    RETURN ancestor
                    """)
  Set<ResourceEntity> findLineageByNodeId(String id);
}
