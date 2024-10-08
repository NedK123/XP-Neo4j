package org.example.xpneo4j.core;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
