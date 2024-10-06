package org.example.xpneo4j.core;

import java.util.Set;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchLineageRequest {
  @NonNull private String targetResourceId;
  @NonNull private String projectId;
  @Builder.Default private Set<RelationshipType> filterRelationshipTypes = Set.of();
  @Builder.Default private Set<String> filterResourceTypes = Set.of();
  @Builder.Default private Set<String> filterContexts = Set.of("default");
}
