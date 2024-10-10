package org.example.xpneo4j.core;

import java.util.Set;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NeighborRegistrationInfo {
  @NonNull private String id;
  @NonNull private String name;
  @Builder.Default private Set<String> additionalLabels = Set.of();
  @Builder.Default private String relationshipContext = "default";
  @NonNull private RelationshipType relationshipType;
  @Builder.Default private boolean inheritCreationConnectionFromTarget = false;
}
