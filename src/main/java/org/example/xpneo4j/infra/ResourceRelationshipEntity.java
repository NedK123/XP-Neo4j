package org.example.xpneo4j.infra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class ResourceRelationshipEntity {
    @RelationshipId
    private String id;

    @TargetNode
    private ResourceEntity target;

    private String context;
}
