package org.example.xpneo4j.infra;

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
@Node("Resource")
public class ResourceEntity {
  @Id private String resourceId;

  @Relationship("CORRELATES_WITH")
  @Builder.Default
  private Set<ResourceRelationshipEntity> resourceRelationshipEntity = Set.of();

  @DynamicLabels private Set<String> labels;
}
