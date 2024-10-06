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
public class ResourceLineage {
  @Builder.Default private Set<Resource> resources = Set.of();
}
