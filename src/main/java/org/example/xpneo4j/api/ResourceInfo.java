package org.example.xpneo4j.api;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xpneo4j.core.Relationships;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceInfo {
  private String resourceId;
  private Set<String> labels;
  private String context;
  private Set<Relationships> relationshipLabels;
}
