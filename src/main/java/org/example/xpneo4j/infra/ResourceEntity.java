package org.example.xpneo4j.infra;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node("Resource")
public class ResourceEntity {
    @Id
    private String resourceId;

    @Relationship("correlates_with")
    @Builder.Default
    private Set<ResourceRelationshipEntity> resourceRelationshipEntity = Set.of();

    @DynamicLabels
    private Set<String> labels;

}
