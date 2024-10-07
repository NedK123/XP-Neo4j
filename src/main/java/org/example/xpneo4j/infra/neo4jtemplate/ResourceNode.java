package org.example.xpneo4j.infra.neo4jtemplate;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node
public class ResourceNode {
  @Id private String id;
  private String name;
  private String projectId;
  @DynamicLabels private Set<String> labels;

  @Builder.Default
  @Relationship(type = "CREATED_UNDER", direction = Relationship.Direction.OUTGOING)
  private Set<ResourceRelationship> createdUnderRelationships = Set.of();

  @Builder.Default
  @Relationship(type = "REPUSH_OF", direction = Relationship.Direction.OUTGOING)
  private Set<ResourceRelationship> repushOfRelationships = Set.of();
}
