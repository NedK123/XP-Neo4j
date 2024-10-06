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
public class FetchLineageRequest {
  private String targetResourceId;
  private String projectId;
  @Builder.Default private Set<String> filterResourceTypes = Set.of();
  @Builder.Default private Set<String> filterContexts = Set.of();
}
