package org.example.xpneo4j.core;

import java.util.Set;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchRelativesRequest {
  @NonNull private String targetResourceId;
  @Builder.Default private Set<String> relativeResourcesTypeFilter = Set.of();
  @Builder.Default private String relativeRelationshipContextFilter = "default";
}
