package org.example.xpneo4j.api;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xpneo4j.core.RelationshipType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NeighborRegistrationInfo {
  private String id;
  private String name;
  private Set<String> labels;
  @Builder.Default private String relationshipContext = "default";
  private RelationshipType relationshipLabel;
}
