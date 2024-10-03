package org.example.xpneo4j.infra;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ResourceRepo extends Neo4jRepository<ResourceEntity, String> {

//    @Query("MATCH (target:Resource)<-[r:correlates_with*]-(ancestor:Resource) " +
//            "WHERE target.resourceId = $resourceId RETURN target, collect(r), collect(ancestor)")
//    LineageProjection findLineage(String resourceId);

    @Query("MATCH (ancestor)-[r*]->(target) WHERE target.resourceId = $id RETURN ancestor")
    Set<ResourceEntity> findLineageByNodeId(String id);
}
