package org.example.xpneo4j.core;

import java.util.Map;
import java.util.Set;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativesResponse {
  //  @NonNull private String targetResourceId;
  @Builder.Default private Map<String, Set<RelativeResource>> relatives = Map.of();
}
