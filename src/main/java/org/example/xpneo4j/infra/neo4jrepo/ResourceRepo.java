package org.example.xpneo4j.infra.neo4jrepo;

import java.util.Set;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepo extends Neo4jRepository<ResourceNode, String> {
  @Query(
      """
        MATCH (t:Resource {id: $targetResourceId})
        MATCH pathToAncestor = (t)-[:CREATED_UNDER*1..$ancestorsGenerationLimit]->(ancestor)
        WITH pathToAncestor, ancestor
        MATCH (ancestor)<-[:CREATED_UNDER*1..$relativesGenerationLimit]-(relative)
          WHERE ANY(label IN labels(relative) WHERE label IN $relativeResourcesTypeFilter)
        RETURN nodes(pathToAncestor) as targetLineage, collect(relative) as relatives
  """)
  ResourceFamily fetchRelatives(
      String targetResourceId,
      int ancestorsGenerationLimit,
      int relativesGenerationLimit,
      Set<String> relativeResourcesTypeFilter);
}
