package org.example.xpneo4j.infra.neo4jtemplate;

import java.util.Set;
import lombok.*;
import org.springframework.data.neo4j.core.schema.DynamicLabels;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Node("Resource")
public class ResourceNode {
  @Id @NonNull private String id;
  @NonNull private String name;
  @NonNull private String projectId;

  @Builder.Default @DynamicLabels private Set<String> labels = Set.of();

  @Builder.Default
  @Relationship(type = "CREATED_UNDER", direction = Relationship.Direction.OUTGOING)
  private Set<ResourceRelationship> createdUnderRelationships = Set.of();

  @Builder.Default
  @Relationship(type = "REPUSH_OF", direction = Relationship.Direction.OUTGOING)
  private Set<ResourceRelationship> repushOfRelationships = Set.of();
}
