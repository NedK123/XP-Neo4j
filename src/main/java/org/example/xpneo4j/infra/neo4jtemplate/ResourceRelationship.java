package org.example.xpneo4j.infra.neo4jtemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RelationshipProperties
public class ResourceRelationship {
  @GeneratedValue @RelationshipId private String id;
  private String context;
  @TargetNode private ResourceNode neighbor;
}
