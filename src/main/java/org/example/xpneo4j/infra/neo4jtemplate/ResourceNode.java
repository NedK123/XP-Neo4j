package org.example.xpneo4j.infra.neo4jtemplate;

import java.util.Set;
import lombok.*;
import org.example.xpneo4j.core.RelationshipType;
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

  @Relationship(type = "CREATED_UNDER", direction = Relationship.Direction.OUTGOING)
  private ResourceRelationship createdUnderRelationship;

  @Relationship(type = "REPRODUCTION_OF", direction = Relationship.Direction.OUTGOING)
  private ResourceRelationship reproductionOfRelationship;

  @Builder.Default
  @Relationship(type = "USED_BY", direction = Relationship.Direction.OUTGOING)
  private Set<ResourceRelationship> usedByRelationships = Set.of();

  public void addRelationshipWithNeighbor(
      ResourceRelationship relationship, RelationshipType type) {
    switch (type) {
      case CREATED_UNDER -> createdUnderRelationship = relationship;
      case REPRODUCTION_OF -> reproductionOfRelationship = relationship;
      case USED_BY -> usedByRelationships.add(relationship);
    }
  }
}
